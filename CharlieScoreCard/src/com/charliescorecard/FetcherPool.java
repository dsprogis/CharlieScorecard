package com.charliescorecard;

import java.util.ArrayList;
import java.util.List;


public class FetcherPool implements Runnable {
	private boolean _bFetching;
	private List<Fetcher> fetchers = new ArrayList<Fetcher>();
	
    public void run(){
       System.out.println("FetcherPool running");
       // TODO : will want to pull durable _bFetching status from DB?  For now, start as false
       _bFetching = false;
    }
    
    public boolean startFetching() {
    	_bFetching = true;
    	Fetcher f = null;
    	
    	List<String> listRoutes = new dbaccess().getTrackedRouteStr();
    	
    	for ( String route : listRoutes ) {
	    	f = new Fetcher( route );
	    	f.start();
	    	fetchers.add( f );
		}
    	
    	return true;	// TODO : return status will become relevant when managing state in DB
    }
    public boolean stopFetching() {
    	_bFetching = false;

		for ( Fetcher f : fetchers ) {
			f.finish();
	    	f = null;
		}
    	

    	return true;	// TODO : return status will become relevant when managing state in DB
    }
    public boolean isFetching() {
    	return _bFetching;
    }
    
    
  }
