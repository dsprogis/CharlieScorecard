package com.charliescorecard;

import java.util.ArrayList;
import java.util.List;


public class FetchManager extends Thread {
	private boolean _bManagerAlive = true;
	private List<Fetcher> fetchers = new ArrayList<Fetcher>();
	private dbaccess dba = null;
	
    public void run(){
    	System.out.println("__FetchManager started");
    	dba = new dbaccess();
    	int thread_count;
    	int route_count;
    	
    	while( _bManagerAlive )
    	{
	   		try {
	   			sleep(1000);
	   		} catch (InterruptedException ex) {
	   			System.out.println( "__FetchManager Interupted" );
	   			// TODO : handle interrupt
	   			// Update DB so that all Fetchers quit
	   			_bManagerAlive = false;
	   		}

	   		String fetcher_runstate = dba.getSetting( "fetchers" );
   			System.out.println( "__FetchManager fetchers runstate = " + fetcher_runstate );
	   		if( fetcher_runstate.equals("running") )
	   		{
	   			System.out.println( "__FetchManager Running: Checking Threads" );
	   			thread_count = fetchers.size();
	   			if( 0 != thread_count ) {						// If threads have been started, check count and status
	   				route_count = get_db_route_count();
	   				if( thread_count == route_count ) {			// 		If no routes have been added or subtracted, just check health 
	   					// Loop List & check isAlive()
	   				} else {									// 		Else if routes have been added or subtracted, add/remove threads
	   					// Find diffs and supplement
	   				}
	   			} else {										// Else if threads not started, Start all fetcher threads
	   				startFetching();
	   			}
	   			
	   		} else {
	   			System.out.println( "__FetchManager on Standby - not checking fetch threads because should not be running" );
	   			thread_count = fetchers.size();
	   			if( 0 != thread_count ) {						// If threads have NOT been stopped, stop them
	   				stopFetching();
	   			}
	   			
	   		}

	   	}

    }

    private int get_db_route_count() {
    	return new dbaccess().getTrackedRouteStr().size();
    }
    
    private boolean startFetching() {
    	Fetcher f = null;
    	
    	List<String> listRoutes = new dbaccess().getTrackedRouteStr();
    	
    	for ( String route : listRoutes ) {
	    	f = new Fetcher( route );
	    	f.start();
	    	fetchers.add( f );
		}
    	return true;	// TODO : return status will become relevant when managing state in DB
    }
    private boolean stopFetching() {

		for ( Fetcher f : fetchers ) {
			f.finish();
	    	f = null;
		}
    	return true;	// TODO : return status will become relevant when managing state in DB
    }

    
  }
