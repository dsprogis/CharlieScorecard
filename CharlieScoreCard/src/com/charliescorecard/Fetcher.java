package com.charliescorecard;

public class Fetcher implements Runnable {
	private boolean _bFetching;
	
    public void run(){
       System.out.println("Fetcher created");
       // TODO : will want to pull durable _bFetching status from DB?  For now, start as false
       _bFetching = false;
    }
    
    public boolean startFetching() {
    	_bFetching = true;
    	return true;	// TODO : return status will become relevant when managing state in DB
    }
    public boolean stopFetching() {
    	_bFetching = false;
    	return true;	// TODO : return status will become relevant when managing state in DB
    }
    public boolean isFetching() {
    	return _bFetching;
    }
    
  }
