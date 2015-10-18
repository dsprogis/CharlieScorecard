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
	   			
	   			System.out.println( "__FetchManager Running: Checking Threads" );
	   			thread_count = fetchers.size();
	   			if( 0 != thread_count ) {						// If threads have been started, check count and status
	   				route_count = get_db_route_count();
	   				if( thread_count == route_count ) {			// 		If no routes have been added or subtracted, just check health 
	   					// TODO: Loop List & check isAlive()
	   				} else {									// 		Else if routes have been added or subtracted, add/remove threads
	   					// TODO: Find diffs and supplement
	   				}
	   			} else {										// Else if threads not started, Start all fetcher threads
	   				startFetching();
	   			}
	   			
	   			sleep(60000);	//Default to 60 seconds (60000)
	   			
	   		} catch (InterruptedException ex) {
	   			System.out.println( "__FetchManager Interupted, stopping any Fetcher threads" );
	   			stopFetching();
	   			_bManagerAlive = false;
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
    	
    	Fetcher f;
    	for (int i = 0; i < fetchers.size(); i++) {
			f = fetchers.get(i);
			f.interrupt();
		}
		
    	return true;	// TODO : return status will become relevant when managing state in DB
    }

    
  }
