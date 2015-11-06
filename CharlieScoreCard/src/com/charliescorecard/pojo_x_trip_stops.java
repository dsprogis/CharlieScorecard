package com.charliescorecard;

public class pojo_x_trip_stops {

	private String _trip_id, _arrival_time, _departure_time, _stop_id, _stop_sequence, _stop_lat, _stop_lon;

	public pojo_x_trip_stops( ) {
		_trip_id = null;
		_arrival_time = null;
		_departure_time = null;
		_stop_id = null;
		_stop_sequence = null;
		_stop_lat = null;
		_stop_lon = null;
	}
	public pojo_x_trip_stops( String trip_id, String arrival_time, String departure_time, String stop_id, String stop_sequence, String stop_lat, String stop_lon ) {
		_trip_id = trip_id;
		_arrival_time = arrival_time;
		_departure_time = departure_time;
		_stop_id = stop_id;
		_stop_sequence = stop_sequence;
		_stop_lat = stop_lat;
		_stop_lon = stop_lon;
	}
	
    public String gettrip_id() { return _trip_id; }
//    public void settrip_id(String trip_id) { _trip_id = trip_id; }

    //  TODO - I don't think I need setters but I might add later
    
    public String getarrival_time() { return _arrival_time; }
    public String getdeparture_time() { return _departure_time; }
    public String getstop_id() { return _stop_id; }
    public String getstop_sequence() { return _stop_sequence; }
    public String getstop_lat() { return _stop_lat; }
    public String getstop_lon() { return _stop_lon; }
    
}


