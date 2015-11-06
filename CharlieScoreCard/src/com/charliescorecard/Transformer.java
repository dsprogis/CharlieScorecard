package com.charliescorecard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/* ************************************************************************************
 * Transformer class is loaded with scheduled trip stops (time & location) from DB based on trip_id
 * 
 * 
 */
public class Transformer {
	private static double distance_allowance = 0.001;		// 0.001 is about 120 yards
	private String _route_id = null;
	private String _trip_id = null;
	private String _trip_name = null;
	private List<pojo_x_trip_stops> _stops = null;
	private dbaccess dba = new dbaccess();
	private Ping _secondlastPing = null;					// for estimating final stop
	private Ping _lastPing = null;
	private int _CompleteCheck = 0;

	public Transformer( String route_id, String trip_id, String trip_name  ) {
		_route_id = route_id;
		_trip_id = trip_id;
		_trip_name = trip_name;
		_stops = dba.GetTripStops( trip_id );
	}
	
	public String gettrip_id( ) {
		return _trip_id;
	}
	
	public boolean isComplete() {
		_CompleteCheck += 1;
		if( _CompleteCheck < 5 ) {			// If the counter hasn't expired then don't give up
	    	System.out.println( "__ __ __ isComplete: _trip_id=" + _trip_id + ", _CompleteCheck=" + _CompleteCheck );
			return false;			
		}

		if( null == _secondlastPing || null == _lastPing ) {
	    	System.out.println( "__ __ __ isComplete: counter has expired but there is insufficient history to estimate a final stop - just end it" );
			return true;
		}
											// If the vehicle did not move over the last two pings then we can't estimate the last stop time
		if( _lastPing._locVehicle.getLat() == _secondlastPing._locVehicle.getLat()
				&& _lastPing._locVehicle.getLng() == _secondlastPing._locVehicle.getLng() ) {
	    	System.err.println( "__ __ __ isComplete: vehicle has not moved (this code should never have been reached!!!" );
			return true;
		}
		
		if( _lastPing._segmentNum == (_stops.size()-1) ) {		// If the last ping was in the last segment, estimate the final stop time
			pojo_x_trip_stops laststop = _stops.get( _stops.size()-1 );
			double distCurToLastPing = distance( _lastPing._locVehicle, _secondlastPing._locVehicle );
			long timeCurToLastPing = _lastPing._vehicle_timestamp - _secondlastPing._vehicle_timestamp;  // TODO: Error checking.  For now, assume not-negative
			double pace = timeCurToLastPing/distCurToLastPing;
			if( pace > 250000 || pace < 0 ) {
		    	System.err.println( "__ __ __ Trip:" + _trip_id + ", something wrong with last-stop pace: " + pace );
		    	return true;
			}
			ComputeStopAndInsert( _lastPing, laststop, pace, false );
		}
		return true;
	}
	
	public void update( pojo_x_raw_location curLog ) {
		_CompleteCheck = 0;		// reset the counter because we just got another ping
		List<Ping> pings = new ArrayList<Ping>();
		Ping curPing = null;
													// Which segment in the trip are we within?  Segments numbered by their "Last" stop
		int numberSegments = _stops.size() -1;  	// There are 9 trip segments between 10 stops so reduce the segment threshold to stops-1
		for( int i=0; i< numberSegments; i++) {		// The array and counter are 0-based whereas stop_sequence is 1-based
			curPing = between( i+1, _stops.get(i), _stops.get(i+1), curLog );
			if( null != curPing ) {
	        	System.out.println( "__ __ Trip: " + _trip_id + ", segment hit between stops " + _stops.get(i).getstop_sequence() + " and " + _stops.get(i+1).getstop_sequence() );
				pings.add( curPing );
			}
		}

		switch ( pings.size() ) {
		case 0:
        	System.out.println( "__ __ Trip: " + _trip_id + ", no hits.  Consider increasing the distance_allowance fudge-factor" );
			return;
			
		case 1:
        	System.out.println( "__ __ Trip: " + _trip_id + ", one hit." );
        	curPing = pings.get(0);		// reuse curPing to carry ping data
			break;
			
		default:
        	System.out.println( "__ __ Trip: " + _trip_id + ", more than one hit - finding the best fit (shortest)." );
        	// If more than one segment, find the best fit which should have the shortest legs
        	if( pings.size() > 3) {
            	System.out.println( "__ __ Trip: " + _trip_id + ", " + pings.size() + " hits.  Consider reducing the distance_allowance fudge-factor" );
        	}
        	int indexShortest = 0;
    		for( int i=1; i < pings.size(); i++) {
    			if( pings.get(i)._distLastNext < pings.get(indexShortest)._distLastNext ) {
    				indexShortest = i;
    			}
    		}
        	curPing = pings.get(indexShortest);		// reuse curPing to carry ping data
			break;
		}

		// If the vehicle has not moved since the last ping then ...
		if( null != _lastPing && 0.0 == distance( curPing._locVehicle, _lastPing._locVehicle ) ) {
			// If the vehicle is in the first segment (has not left the depot) then replace _lastPing with _curPing because the trip has not started
			if( 1 == curPing._segmentNum ) {
				_secondlastPing = _lastPing;
				_lastPing = curPing;
			} else {
				// ElseIf the vehicle is not in the first segment, ignore this ping because it adds no value and will corrupt distance calculations
	        	System.out.println( "__ __ Trip: " + _trip_id + "vehicle has not moved - ignoring ping event" );
			}
        	return;
		}
		
		if( null != _lastPing ) {								// If this Transformer is not new, then we can calculate stops from last ping

			int numStopsBetweenPings = curPing._segmentNum - _lastPing._segmentNum;
			
			if( 0 != numStopsBetweenPings ) {					// unless we are in a new segment, we have not passed any stops and there is nothing to record

				long timeCurToLastPing = curPing._vehicle_timestamp - _lastPing._vehicle_timestamp;	// TODO: Error checking.  For now, assume not-negative
				double distCurToLastPing = distance( curPing._locVehicle, _lastPing._locVehicle );	// For now, don't worry about bus turning corners - use straightline distance
				double pace = timeCurToLastPing/distCurToLastPing;
				
				for( int i=0; i<numStopsBetweenPings; i++) {	// Estimate stop-time for each of the stops we have passed,  TODO: ASSUMED stop_sequence are contiguous (no missing numbers)
						// Get coordinates of stop
					pojo_x_trip_stops stop = _stops.get( _lastPing._segmentNum +i );
					ComputeStopAndInsert( curPing, stop, pace, true );
				}
				
				if( 1 == _lastPing._segmentNum ) {				// Estimate time leaving depot if the ping comes from the first segment (TODO: might happen more than once with reducing accuracy but punt for now)
					pojo_x_trip_stops stop = _stops.get( 0 );
					ComputeStopAndInsert( curPing, stop, pace, true );
				}
				
			}
			
		}

		_secondlastPing = _lastPing;		// We need this value to estimate last stop
		_lastPing = curPing;
		
		return;
	}
	
	private boolean ComputeStopAndInsert( Ping curPing, pojo_x_trip_stops targetStop, double pace, boolean bBackward ) {
		if( pace > 250000 || pace < 0 ) {
	    	System.err.println( "__ __ __ something wrong with mid-trip pace: " + pace );
	    	return false;
		}
		// Compute incremental distance
		double distCurToTargetStop = distance( curPing._locVehicle, new pojo_coordinate( targetStop.getstop_lat(), targetStop.getstop_lon() ) );
		// Compute incremental time
		long timeCurToTargetStop = Math.round(distCurToTargetStop * pace);
		long actualStopTime = 0;
		if(bBackward) {							// I tried to get clever here but it was simply unreadable - opting for simplicity
			// For stops behind curPing, estimate actual stop time by subtracting incremental time
			actualStopTime = curPing._vehicle_timestamp - timeCurToTargetStop;
		} else {
			// For stops behind curPing, estimate actual stop time by adding incremental time
			actualStopTime = curPing._vehicle_timestamp + timeCurToTargetStop;
		}
		// Convert format of scheduled time to epoch time
		long midnight = EpochMidnight( curPing._vehicle_timestamp );	// Use Ping day as basis for epoch time
		long time = TimeInSeconds(targetStop.getarrival_time());			// Add the scheduled time
		long offset = timezone("EST", curPing._vehicle_timestamp);		// Adjust for timezone (and DST)
		long scheduledArrivalTime = midnight + time + offset;
		
    	System.out.println( "__ __ __ distCurToTargetStop=" + distCurToTargetStop + ", pace=" + pace + ", timeCurToTargetStop=" + timeCurToTargetStop );
    	System.out.println( "__ __ __ stoptime: actual=" + actualStopTime + ", scheduled=" + scheduledArrivalTime + ", diff=" + (actualStopTime-scheduledArrivalTime) );
		
		pojo_x_transformed_location vehicleAtStop = new pojo_x_transformed_location( _route_id, _trip_id,  _trip_name,  targetStop.getstop_id(), targetStop.getstop_sequence(), curPing._vehicle_id, Long.toString(actualStopTime), Long.toString(scheduledArrivalTime), Long.toString(scheduledArrivalTime-actualStopTime) );
		return dba.InsertTransformedRoute( vehicleAtStop );
	}
	

	// If the distance between the bus and two stops of the segment is reasonable, return a Ping object with combined distances and other details
	//   otherwise return null
	private Ping between( int segmentNum, pojo_x_trip_stops stopLast, pojo_x_trip_stops stopNext, pojo_x_raw_location curLog ) {
		pojo_coordinate last = new pojo_coordinate( stopLast.getstop_lat(), stopLast.getstop_lon() );
		pojo_coordinate next = new pojo_coordinate( stopNext.getstop_lat(), stopNext.getstop_lon() );
		pojo_coordinate cur = new pojo_coordinate( curLog.getvehicle_lat(), curLog.getvehicle_lon() );
		
		double distLast = distance(last, cur);
		double distNext = distance(cur, next);
		double segment_length = distance(last, next);
		
		// The two legs, distance to lastStop and distance to nextStop, shouldn't be much more than the distance between stops ...
		if ( (distLast + distNext ) <= (segment_length + distance_allowance)  ) {
			return new Ping( segmentNum, cur, distLast, distNext, curLog.getvehicle_id(), curLog.getvehicle_timestamp() );
		}
		return null;
	}
	
	// Return the distance between any two points
	private double distance( pojo_coordinate pt1, pojo_coordinate pt2 ) {
		double lat = (Double.parseDouble(pt2.getLat())-Double.parseDouble(pt1.getLat()));
		lat = lat*lat;
		double lng = (Double.parseDouble(pt2.getLng())-Double.parseDouble(pt1.getLng()));
		lng = lng*lng;
		return Math.sqrt(lat+lng);
	}

	private long EpochMidnight( long epochDate ) {
		long remainder = epochDate % 86400;			// Get the number of seconds since start of day by modulus seconds-in-day
		return epochDate - remainder;				// Return the date stripped of the modulus part of the day
	}
	
	private long TimeInSeconds( String sTime ) {
		String delim = "[:]";
		String[] token = sTime.split(delim);
		int hours = Integer.parseInt(token[0]);
		int minutes = Integer.parseInt(token[1]);
		int seconds = Integer.parseInt(token[2]);
		return (hours * 3600) + (minutes * 60) + seconds;
	}
	
	private long timezone(String zone, long epochDate) {
		long adjustment = 0;	// -(Calendar.get(Calendar.ZONE_OFFSET) + Calendar.get(Calendar.DST_OFFSET)) / 1000;
//		TimeZone tz = TimeZone.getTimeZone(zone);
		Date d = new Date(epochDate*1000);
//		boolean inDS = tz.inDaylightTime( d );

 		switch (zone) {
		case "EST":
//			if( inDS ) 		// if summer savings
				adjustment = 4*3600;		// EDT
//			else
//				adjustment = 5*3600;		// EST	
			break;
		default:
	    	System.err.println( "Time conversion error in timezone() adjustment." );
			break;
		}

		return adjustment;
	}
	
	private class Ping {
		pojo_coordinate _locVehicle = null;	// Keep a copy of the vehicle's reported location
		double _distLast = 0.0;				// distance of leg from-stop to current bus location (ping location)
		double _distNext = 0.0;				// distance of leg current bus location to to-stop
		double _distLastNext = 0.0;			// the combined distances of distLast and distNext
		int _segmentNum = -1;				// the segment that the ping is in
		String _vehicle_id = null;				// UID of vehicle
		long _vehicle_timestamp = 0;		// epoc timestamp of bus at reported location
		Ping( int segmentNum, pojo_coordinate locVehicle, double distLast, double distNext, String vehicle_id, String vehicle_timestamp ) {
			_segmentNum = segmentNum;
			_locVehicle = locVehicle;
			_distLast = distLast;
			_distNext = distNext;
			_distLastNext = distLast + distNext;
			_vehicle_id = vehicle_id;
			_vehicle_timestamp = Long.parseLong( vehicle_timestamp );  // TODO: error checking in case not number
		}
	}

}
