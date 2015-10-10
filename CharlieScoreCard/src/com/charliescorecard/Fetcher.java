package com.charliescorecard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Fetcher extends Thread { // implements Runnable {
    private Connection connect = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
    private final String driver = "com.mysql.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/mbta";
    private final String user = "mbta";
    private final String password = "mbta";
    
	private String _RouteID;
	private int _logID;
	private boolean _bRunning = false;
	
	public Fetcher( String RouteID ) {
		_RouteID = RouteID;
		_logID = 0;
	}
	
    public void run(){
    	_bRunning = true;
    	logNewFetcher( _RouteID );
    	while( true == _bRunning )
    	{
    		System.out.println( "Fetcher running, Route ID = " + _RouteID );
    		try {
    			sleep(5000);
    		} catch (InterruptedException ex) {
    			// TODO : handle interrupt
    		}
        	logUpdateFetcher( );
    	}
    }
    
    public void finish(){
    	System.out.println( "Fetcher stoping, Route ID = " + _RouteID );
    	logUpdateFetcher( );
    	_bRunning = false;
    }
    
    /************************************************************************************
     * logNewFetcher creates a log entry with a start date.
     * Logs will be used to constrain analytics so that there don't appear to be outages when logs not collected.
     */
	private int logNewFetcher( String routeID ) {
	    String query = null;
		
	    System.out.println("in logNewFetcher()");

	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      Statement st = connect.createStatement();
	      query = "INSERT INTO fetcher_log (route_id, ts_started, ts_last_update) VALUES ( \"" + routeID + "\", now(), now() )";
	      st.executeUpdate( query );
	      
	      ps = connect.prepareStatement("SELECT LAST_INSERT_ID()" );
	      rs=ps.executeQuery();
	      if (rs.next()) {
	    	  _logID = rs.getInt( "last_insert_id()" );            // This promises to be thread-safe because it is scoped to DB connection
	      }

	      System.out.println( query );

	    }catch(SQLException se) {
	        se.printStackTrace();
	        return 0;
	    }catch(Exception e) {
	        e.printStackTrace();
	        return 0;
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in logNewFetcher().");	
					return 0;
	         }
	    }
	    
	    return 0;	
	}

	private boolean logUpdateFetcher( ) {
	    String query = null;
		
	    System.out.println("in logUpdateFetcher()");

	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      Statement st = connect.createStatement();
	      query = "UPDATE fetcher_log SET ts_last_update = now() WHERE log_id = " + _logID;
	      st.executeUpdate( query );
	      
	      System.out.println( query );

	    }catch(SQLException se) {
	        se.printStackTrace();
	        return false;
	    }catch(Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in logUpdateFetcher().");	
					return false;
	         }
	    }
	    return true;	
	}

  }