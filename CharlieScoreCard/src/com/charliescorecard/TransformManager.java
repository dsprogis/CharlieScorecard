package com.charliescorecard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransformManager extends Thread {
	private boolean _bTransformer = true;
	private dbaccess dba = null;
	private List<Transformer> activeTransformers = new ArrayList<Transformer>();
	Iterator<Transformer> it = null;

    public void run(){
    	List<pojo_x_raw_location> rawLogs = null;
		pojo_x_raw_location curLog = null;
    	Transformer currentTranformer;

		System.out.println("__Transformer started");
    	dba = new dbaccess();
    	
    	while( _bTransformer )
    	{
	   		try {
	   			
	   			System.out.println( "__Transformer Running: polling for new raw_location rows" );

	   			// Fetch up to NNN rows of unprocessed raw log data & change status to processed (add processed date)
	   			rawLogs = dba.getRawLogs( 25 );
	   			
	   			for( int i=0; i<rawLogs.size(); i++ )
	   			{
	   				curLog = rawLogs.get(i);
		   			System.out.println( "__Row = " + i + ", routeID = " + curLog.getroute_id() + ", trip_id = " + curLog.gettrip_id() );
	   			
		   			currentTranformer = findTransformer( curLog.gettrip_id(), curLog.getvehicle_timestamp() );
		   			if( null == currentTranformer ) {  				// If row is part of new trip then add new Transformer and update.
		   				currentTranformer = new Transformer( curLog.getroute_id(), curLog.gettrip_id(), curLog.gettrip_name(), curLog.getvehicle_timestamp() );
		   				// UPDATE for now but may want to call a different method to confirm decide where we are in trip
		   				// ... might want to ignore partial trips because we start logging in the middle
		   				currentTranformer.update( curLog );
		   				// We might not want to add trips unless we are starting at the beginning.
		   				// ... this might be a conditional add
		   				// ... save a list of trips to ignore ... for now?
		   				activeTransformers.add( currentTranformer );
		   				
		   			} else { 					   					// Else if row is part of existing trip then update with new log data
		   				currentTranformer.update( curLog );
		   			}
		   			
	   			} // for loop
	   			
	   			// Run process to detect when trips are completed
	   			System.out.println( "__ Transformer Count: " + activeTransformers.size() );
  				it = activeTransformers.iterator();
  				while( it.hasNext() ) {
  					Transformer t = it.next();
  					if( t.isComplete() ) {
  			   			System.out.println( "__ Deleting Transformer for trip: " + t.gettrip_id() );
  						it.remove();
  					} else {
  			   			System.out.println( "__ Preserving Transformer for trip: " + t.gettrip_id() );
  					}
  				}

  				dba.markRawLogs( rawLogs );

	   			sleep(2000);	//Default to 60 seconds (60000)
	   			System.out.println( "__Transformer sleeping ..." );
//	   			_bTransformer = false;		// TODO - delete this temporary stop
	   			
	   		} catch (InterruptedException ex) {
	   			System.out.println( "__Transformer Interupted" );
	   			// TODO:  Must make sure that transformed trips are not left partial - do I delete partials or finish them?
	   			_bTransformer = false;
	   		}

	   	}
    	

    }

    private Transformer findTransformer( String trip_id, String timestamp ) {
    	Transformer current;
    	if( null!=activeTransformers ) {
    		for( int i=0; i<activeTransformers.size(); i++) {
    			current = activeTransformers.get(i);
    			if ( trip_id.equals(current.gettrip_id()) && current.isSameDate(timestamp) ) {
    				return current;
    			}
    		}
    	}
    	return null;
    }

    
  }

