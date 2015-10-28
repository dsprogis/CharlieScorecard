package com.charliescorecard;

import com.google.gson.*;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Fetcher extends Thread { // implements Runnable {
	public static final int MYSQL_DUPLICATE_PK = 1062;
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
	private String _jsonFeed = null;
	private BigInteger bigIntLast;
	private boolean _bExtendedSleep = false;
	
	public Fetcher( String RouteID ) {
		_RouteID = RouteID;
		_logID = 0;
	}
	
    public void run(){
    	
    	// RUN ONCE WHEN STARTED
    	_bRunning = true;
    	logNewFetcher( _RouteID );
		bigIntLast = new BigInteger( "0" );
    	
    	// MAIN LOOP - RUN REPEATEDLY UNTIL INTERUPTED OR _bRunning STATE CHANGE
    	while( true == _bRunning )
    	{
    		System.out.println( "__ __ Fetcher running, Route ID = " + _RouteID + ", _LogID = " + _logID );
    		try {

    			if( true == _bExtendedSleep ) {
        			sleep(60000);	// Default to 60000 = 60 seconds
    	        	_bExtendedSleep = false;
    			}
    // Fetch the data for this route
    			String fetchQuery = "http://realtime.mbta.com/developer/api/v2/vehiclesbyroute?api_key=k_wFTD1RnkidAgHIvkbdgw&route=" + _RouteID + "&format=json";
    			System.out.println( fetchQuery );
    			_jsonFeed = readUrl2( fetchQuery );
// 				System.out.println( "__ __ " + _jsonFeed );
    			
 	// Objectify the JSON
			    Gson gson = new Gson();
			    feedRoute route = gson.fromJson( _jsonFeed, feedRoute.class );

	// Hash an ID and Revision (there will only every be one version so no collisions)
			    MessageDigest m = MessageDigest.getInstance("MD5");
			    m.reset();
			    m.update( _jsonFeed.getBytes() );
			    byte[] digest = m.digest();
			    BigInteger bigInt = new BigInteger( 1, digest );
			    String hashtext = bigInt.toString(16);
			    while(hashtext.length() < 32 ){		// Pad to full 32 chars.
			      hashtext = "0"+hashtext;
			    }
		        hashtext = "1-"+hashtext;
//				System.out.println( "__ __ " + hashtext );

				route.setId( hashtext );
			    route.setRevision( hashtext );
	    
	//	If this fetch is not the same as the last, then add the Bus Status update to the database    
			    if( 0 != bigInt.compareTo( bigIntLast ) ) {
			    	addFeedData( route );						// Add to running log
			    	updateCurrentLocations( route );			// Update current locations on route
					bigIntLast = bigInt;
			    }
			    else {
					System.out.println("__ __ Fetch skipped because it is a repeat");			    	
			    }

	        	logUpdateFetcher( "Fetching..." );
    			sleep(30000);	// Default to 30000 = 30 seconds
    			
    		} catch (FileNotFoundException ioEx) {
    			
    	    	System.out.println( "__ __ Fetch data not returned for, Route ID = " + _RouteID + " *** Assume feed is down ***" );
	        	logUpdateFetcher( "Fetching, but feed appears down for route=" + _RouteID );
	        	// Don't stop, but slow down the loop
	        	_bExtendedSleep = true; 
	        	
    	    } catch (IOException ioEx) {
    	    	
    	    	System.err.println( "__ __ ***** Failure to fetch data *****, Route ID = " + _RouteID + "\n" );
    	    	String eMsg = ioEx.getMessage();
                if( eMsg.contains("401 for URL") ) {
                    System.err.println( "__ __ ***** Access Key is no longer authorized *****\n" );
        	    	logUpdateFetcher( "Fetch stopped, MBTA Access Key is no longer authorized\n" );
                } else {
                    System.err.println( "__ __ " + eMsg + "\n" );
                }
    	    	System.out.println( "__ __ Fetcher stopping, Route ID = " + _RouteID );
    			_bRunning = false;

    	    } catch (InterruptedException ex) {
    	    	
    	    	System.out.println( "__ __ Fetcher stopping, Route ID = " + _RouteID  + ", _LogID = " + _logID );
    	    	logUpdateFetcher( "Fetch interupted, probably by user" );
    			_bRunning = false;
    			
    		} catch (Exception ex) {
    			
    			// TODO : handle exception
    			
    		}
    		
    	}
    }

    
    /************************************************************************************
     * logNewFetcher creates a log entry with a start date.
     * Logs will be used to constrain analytics so that there don't appear to be outages when logs not collected.
     */
	private int logNewFetcher( String routeID ) {
	    String query = null;
		
	    System.out.println("__ __ In logNewFetcher()");

	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      Statement st = connect.createStatement();
	      query = "INSERT INTO fetcher_log (route_id, ts_started, ts_last_update) VALUES ( \"" + routeID + "\", now(), now() )";
	      st.executeUpdate( query );

	      // Get the Unique Log entry so we can keep it up-to-date with this thread
	      ps = connect.prepareStatement("SELECT LAST_INSERT_ID()" );
	      rs=ps.executeQuery();
	      if (rs.next()) {
	    	  _logID = rs.getInt( "last_insert_id()" );            // This promises to be thread-safe because it is scoped to DB connection
	      }

//	      System.out.println( "__ __ " + query );

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
					System.out.println("__ __ MySQL Exception in logNewFetcher().");	
					return 0;
	         }
	    }
	    
	    return 0;	
	}

	private boolean logUpdateFetcher( String message ) {
	    String query = null;
		
//	    System.out.println("__ __ In logUpdateFetcher()");

	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      Statement st = connect.createStatement();
	      // TODO : use safer format to Update
	      query = "UPDATE fetcher_log SET ts_last_update = now(), message = \"" + message + "\" WHERE log_id = " + _logID;
	      st.executeUpdate( query );
	      
//	      System.out.println( "__ __ " + query );

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
					System.out.println("__ __ MySQL Exception in logUpdateFetcher().");	
					return false;
	         }
	    }
	    return true;	
	}

	
	private static String readUrl2(String urlString) throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[8192];

		try {

			URL url = new URL( urlString );
			URLConnection con = url.openConnection();
			InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			int len = 0;
			while ((len = in.read(buf)) != -1) {
			    baos.write(buf, 0, len);
			}
			String body = new String(baos.toByteArray(), encoding);
			System.out.println(body);
			return body;

	    } catch (FileNotFoundException  ioEx) {
	    	// Display exception
	    	String eMsg = ioEx.getMessage();
            System.err.println( "__ __ File not found while trying to read URL: " + eMsg );
	    	throw ioEx;
	    } catch (EOFException  ioEx) {
	    	// Display exception
	    	String eMsg = ioEx.getMessage();
            System.err.println( "__ __ End of file reached while trying to read URL: " + eMsg );
	    	throw ioEx;
	    } catch (IOException ioEx) {
	    	// Display exception
	    	String eMsg = ioEx.getMessage();
            System.err.println( "__ __ IOException encountered while trying to read URL: " + eMsg );
	    	throw ioEx;
	    }
	}
	
	private int addFeedData( feedRoute route ) throws Exception {
	    int status = 0;

//	    System.out.println("__ __ In Fetcher:addFeedData()");

	    try {
			Class.forName( driver );
			connect = DriverManager.getConnection( url, user, password );

			ps = connect.prepareStatement("insert into raw_location (route_id, trip_id, trip_name, vehicle_id, vehicle_lat, vehicle_lon, vehicle_timestamp) values (?, ?, ?, ?, ?, ?, ?)" );

	      // Parameters start with 1
			for ( feedDirection direction : route.direction ) {
				for ( feedTrip trip : direction.trip ) {
					ps.setString( 1, route.route_id );
					ps.setString( 2, trip.trip_id );
					ps.setString( 3, trip.trip_name );
					ps.setString( 4, trip.vehicle.vehicle_id );
					ps.setString( 5, trip.vehicle.vehicle_lat );
					ps.setString( 6, trip.vehicle.vehicle_lon );
					ps.setString( 7, trip.vehicle.vehicle_timestamp );
					try{
						System.out.println( "__ __ " + ps );
						status = ps.executeUpdate();
//						System.out.println( "__ __ " + status );
					} catch( SQLException e ){
	    	    	    if(e.getErrorCode() == MYSQL_DUPLICATE_PK ){
	    					System.err.println("__ __ MySQL Duplicate feed entry into raw_location.");	
	    	    	    }
	    	    	}
				}
			}
	    } catch (Exception e) {
	      throw e;
	    } finally {
	    	try {
	    		if (connect != null) {
	    			connect.close();
	    		}
	    	} catch (Exception e) {
	    		System.out.println("__ __ MySQL Exception.");	
	    	}
	    }
	    return status;
	}
	
	private int updateCurrentLocations( feedRoute route ) throws Exception {
	    int status = 0;

	    try {
			Class.forName( driver );
			connect = DriverManager.getConnection( url, user, password );
			
			// Delete existing current trips where route_id = route
			ps = connect.prepareStatement("delete from current_location where route_id = \"" + route.route_id +"\"" );
			ps.executeUpdate();

			// Add existing current trips where route_id = route
			ps = connect.prepareStatement("insert into current_location (route_id, trip_id, trip_name, vehicle_id, vehicle_lat, vehicle_lon, vehicle_timestamp, shape_id) VALUES (?, ?, ?, ?, ?, ?, ?, (select shape_id from t_trips where trip_id = ?))" );
			for ( feedDirection direction : route.direction ) {
				for ( feedTrip trip : direction.trip ) {
					ps.setString( 1, route.route_id );
					ps.setString( 2, trip.trip_id );
					ps.setString( 3, trip.trip_name );
					ps.setString( 4, trip.vehicle.vehicle_id );
					ps.setString( 5, trip.vehicle.vehicle_lat );
					ps.setString( 6, trip.vehicle.vehicle_lon );
					ps.setString( 7, trip.vehicle.vehicle_timestamp );
					ps.setString( 8, trip.trip_id );
					try{
//						System.out.println( "__ __ " + ps );
						status = ps.executeUpdate();
					} catch( SQLException e ){
	    	    	    if(e.getErrorCode() == MYSQL_DUPLICATE_PK ){
	    					System.err.println("__ __ MySQL Duplicate feed entry into current_location.");	
	    	    	    }
	    	    	}
				}
			}
	    } catch (Exception e) {
	      throw e;
	    } finally {
	    	try {
	    		if (connect != null) {
	    			connect.close();
	    		}
	    	} catch (Exception e) {
	    		System.out.println("__ __ MySQL Exception.");	
	    	}
	    }
	    return status;
	}
	
	
	
  }