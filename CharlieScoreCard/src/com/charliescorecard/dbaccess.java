package com.charliescorecard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class dbaccess {
    private Connection connect = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
    private final String driver = "com.mysql.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/mbta";
    private final String user = "mbta";
    private final String password = "mbta";
    
	public dbaccess( ) {
	}
	
	
	public String getTrackedRoutes() {
		List<pojoTrackedRoutes> routes = new ArrayList<pojoTrackedRoutes>();;
		
	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      ps = connect.prepareStatement("SELECT * FROM mbta.tracked_routes" );
	      rs=ps.executeQuery();
	      
          while( rs.next() ) {
        	  routes.add( new pojoTrackedRoutes( rs.getString("mode_name"), rs.getString("route_id"), rs.getString("date_started"), rs.getString("date_last_touched") ) );
          }
	    }catch(SQLException se) {
	        se.printStackTrace();	        //Handle errors for JDBC
	    }catch(Exception e) {
	        e.printStackTrace();	        //Handle errors for Class.forName
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in dbaccess.getTrackedRoutes().");	
	         }
	    }
		
	    ObjectWriter ow = new ObjectMapper().writer(); //.withDefaultPrettyPrinter();
    	String jsonRoutes = null;
	    try {
	    	jsonRoutes = ow.writeValueAsString( routes );
	    }catch(JsonProcessingException e){
	        e.printStackTrace();
	    }
	    return jsonRoutes;	    
	}
	
	/*
	 * This version of getTrackedRoutes is a lot like the one above but it returns a List<String>.
	 * I thought I could overload the method because the return signature is different but system complained so I added "Str" to end.
	 * TODO : try to consolidate with routine above in the future.
	 */
	public List<String> getTrackedRouteStr() {
		List<String> routes = new ArrayList<String>();;
		
	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      ps = connect.prepareStatement("SELECT route_id FROM mbta.tracked_routes" );
	      rs=ps.executeQuery();
	      
          while( rs.next() ) {
        	  routes.add( rs.getString("route_id") );
          }
	    }catch(SQLException se) {
	        se.printStackTrace();	        //Handle errors for JDBC
	    }catch(Exception e) {
	        e.printStackTrace();	        //Handle errors for Class.forName
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in dbaccess.getTrackedRoutes().");	
	         }
	    }
		
	    return routes;	    
	}
	
	public String getTransportationModes() {
		List<pojo_x_route_mode> routes = new ArrayList<pojo_x_route_mode>();;
		
	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      ps = connect.prepareStatement("SELECT * FROM mbta.x_route_mode" );
	      rs=ps.executeQuery();
	      
          while( rs.next() ) {
        	  routes.add( new pojo_x_route_mode( rs.getString("route_type"), rs.getString("route_mode") ) );
          }
	    }catch(SQLException se) {
	        se.printStackTrace();	        //Handle errors for JDBC
	    }catch(Exception e) {
	        e.printStackTrace();	        //Handle errors for Class.forName
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in dbaccess.getTransportationModes().");	
	         }
	    }
		
	    ObjectWriter ow = new ObjectMapper().writer(); //.withDefaultPrettyPrinter();
    	String jsonRoutes = null;
	    try {
	    	jsonRoutes = ow.writeValueAsString( routes );
	    }catch(JsonProcessingException e){
	        e.printStackTrace();
	    }
	    return jsonRoutes;	    
	}

	public String getRoutesByType( String routeType ) {
		List<pojo_t_routes> routes = new ArrayList<pojo_t_routes>();;
		
	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      ps = connect.prepareStatement("SELECT * FROM mbta.t_routes where route_type=\"" + routeType + "\" order by cast(route_sort_order as signed)" );
	      rs=ps.executeQuery();
	      
          while( rs.next() ) {
        	  routes.add( new pojo_t_routes(
        			  rs.getString("route_id"),
        			  rs.getString("agency_id"),
        			  rs.getString("route_short_name"),
        			  rs.getString("route_long_name"),
        			  rs.getString("route_desc"),
        			  rs.getString("route_type"),
        			  rs.getString("route_url"),
        			  rs.getString("route_color"),
        			  rs.getString("route_text_color"),
        			  rs.getString("route_sort_order") ) );
          }
          
	    }catch(SQLException se) {
	        se.printStackTrace();	        //Handle errors for JDBC
	    }catch(Exception e) {
	        e.printStackTrace();	        //Handle errors for Class.forName
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in dbaccess.getRoutesByType().");	
	         }
	    }
		
	    ObjectWriter ow = new ObjectMapper().writer(); //.withDefaultPrettyPrinter();
    	String jsonRoutes = null;
	    try {
	    	jsonRoutes = ow.writeValueAsString( routes );
	    }catch(JsonProcessingException e){
	        e.printStackTrace();
	    }
	    return jsonRoutes;	    
	}

	
	public String getDirectionsByRoute( String routeID ) {
		List<pojo_t_directions> routes = new ArrayList<pojo_t_directions>();;
		
	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      ps = connect.prepareStatement("SELECT distinct(direction_id), trip_headsign FROM mbta.t_trips where route_id =\"" + routeID + "\" order by direction_id" );
	      rs=ps.executeQuery();
	      
          while( rs.next() ) {
        	  routes.add( new pojo_t_directions(
        			  rs.getString("direction_id"),
        			  rs.getString("trip_headsign") ) );
          }
          
	    }catch(SQLException se) {
	        se.printStackTrace();	        //Handle errors for JDBC
	    }catch(Exception e) {
	        e.printStackTrace();	        //Handle errors for Class.forName
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in dbaccess.getRoutesByType().");	
	         }
	    }
		
	    ObjectWriter ow = new ObjectMapper().writer(); //.withDefaultPrettyPrinter();
    	String jsonRoutes = null;
	    try {
	    	jsonRoutes = ow.writeValueAsString( routes );
	    }catch(JsonProcessingException e){
	        e.printStackTrace();
	    }
	    return jsonRoutes;	
   
	}
	
	
	public String getServiceByRouteAndDirection( String routeID, String direction ) {
		List<pojo_t_services> routes = new ArrayList<pojo_t_services>();;
		
	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      ps = connect.prepareStatement("SELECT service_id, shape_id, count(*) " +
	    		  							"FROM mbta.t_trips " +
	    		  							"WHERE route_id =\"" + routeID + "\"" +
	    		  							"AND direction_id =\"" + direction + "\"" +
	    		  							"group BY service_id;");
	      rs=ps.executeQuery();
	      
          while( rs.next() ) {
        	  routes.add( new pojo_t_services(
        			  rs.getString("service_id"),
        			  rs.getString("shape_id"),
        			  rs.getString(3) ) );
          }
          
	    }catch(SQLException se) {
	        se.printStackTrace();	        //Handle errors for JDBC
	    }catch(Exception e) {
	        e.printStackTrace();	        //Handle errors for Class.forName
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in dbaccess.getRoutesByType().");	
	         }
	    }
		
	    ObjectWriter ow = new ObjectMapper().writer(); //.withDefaultPrettyPrinter();
    	String jsonRoutes = null;
	    try {
	    	jsonRoutes = ow.writeValueAsString( routes );
	    }catch(JsonProcessingException e){
	        e.printStackTrace();
	    }
	    return jsonRoutes;			
   
	}


	public String getShapeBounds( String shapeID ) {
    	List<pojo_coordinate> minmax = new ArrayList<pojo_coordinate>();
	    String query = null;
		
	    System.out.println("in getShapeBounds( )");

	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      query = "SELECT min_lat, min_lon, max_lat, max_lon " +
	    		  "FROM mbta.x_shape_bounds " +
	    		  "WHERE shape_id = \"" + shapeID + "\"";
	      ps = connect.prepareStatement( query );
	      rs=ps.executeQuery();
	      if( rs.next() ) {
	    	  minmax.add( new pojo_coordinate( rs.getString("min_lat"), rs.getString("min_lon") ) );
	    	  minmax.add( new pojo_coordinate( rs.getString("max_lat"), rs.getString("max_lon") ) );
          }
	    }catch(SQLException se) {
	        se.printStackTrace();	        //Handle errors for JDBC
	    }catch(Exception e) {
	        e.printStackTrace();	        //Handle errors for Class.forName
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in getShapeBounds().");	
	         }
	    }

	    ObjectWriter ow = new ObjectMapper().writer(); //.withDefaultPrettyPrinter();
    	String jsonCoords = null;
	    try {
	    	jsonCoords = ow.writeValueAsString( minmax );
	    }catch(JsonProcessingException e){
	        e.printStackTrace();
	    }

	    return jsonCoords;	
	}
	
	public String getRouteShape( String shapeID ) {
    	List<pojo_coordinate> minmax = new ArrayList<pojo_coordinate>();
	    String query = null;
		
	    System.out.println("in getRouteShape()");

	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      query = "SELECT shape_pt_lat, shape_pt_lon FROM mbta.t_shapes " +
	    		  "WHERE shape_id = \"" + shapeID + "\" " +
	    		  "ORDER BY cast(shape_pt_sequence as signed)";
	      ps = connect.prepareStatement( query );
	      rs=ps.executeQuery();
          while( rs.next() ) {
        	  minmax.add( new pojo_coordinate( rs.getString("shape_pt_lat"), rs.getString("shape_pt_lon") ) );
          }
	    }catch(SQLException se) {
	        se.printStackTrace();	        //Handle errors for JDBC
	    }catch(Exception e) {
	        e.printStackTrace();	        //Handle errors for Class.forName
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in getRouteShape().");	
	         }
	    }

	    ObjectWriter ow = new ObjectMapper().writer(); //.withDefaultPrettyPrinter();
    	String jsonCoords = null;
	    try {
	    	jsonCoords = ow.writeValueAsString( minmax );
	    }catch(JsonProcessingException e){
	        e.printStackTrace();
	    }
	    
	    jsonCoords = jsonCoords.replace(":\"", ": ");	// Remove quotes around VALUES
	    jsonCoords = jsonCoords.replace("\",", " ,");	// Remove quotes around VALUES
	    jsonCoords = jsonCoords.replace("\"}", " }");	// Remove quotes around VALUES
	    
	    return jsonCoords;	
	}
	
	
	public String getShapeStops( String shapeID ) {
    	List<pojo_coordinate> minmax = new ArrayList<pojo_coordinate>();
	    String query = null;
		
	    System.out.println("in getServiceStops()");

	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      query = "SELECT stop_lat, stop_lon " +
					"FROM mbta.x_shape_join_stops shape " +
					"INNER JOIN mbta.t_stops stops " +
					"ON shape.stop_id = stops.stop_id " +
					"WHERE shape.shape_id = \"" + shapeID + "\" " +
					"ORDER BY cast(shape.stop_sequence as signed)";
	      ps = connect.prepareStatement( query );
	      rs=ps.executeQuery();
	      System.out.println( query );
          while( rs.next() ) {
        	  minmax.add( new pojo_coordinate( rs.getString("stop_lat"), rs.getString("stop_lon") ) );
          }
	    }catch(SQLException se) {
	        se.printStackTrace();	        //Handle errors for JDBC
	    }catch(Exception e) {
	        e.printStackTrace();	        //Handle errors for Class.forName
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in getServiceStops().");	
	         }
	    }

	    ObjectWriter ow = new ObjectMapper().writer(); //.withDefaultPrettyPrinter();
    	String jsonCoords = null;
	    try {
	    	jsonCoords = ow.writeValueAsString( minmax );
	    }catch(JsonProcessingException e){
	        e.printStackTrace();
	    }

//	    jsonCoords = jsonCoords.replace("\"", "");		// Remove ALL quotes
	    
//	    jsonCoords = jsonCoords.replace("{\"", "{");	// Remove quotes around KEYS
//	    jsonCoords = jsonCoords.replace("\":", ":");	// Remove quotes around KEYS
//	    jsonCoords = jsonCoords.replace(",\"", ",");	// Remove quotes around KEYS
	    
	    jsonCoords = jsonCoords.replace(":\"", ": ");	// Remove quotes around VALUES
	    jsonCoords = jsonCoords.replace("\",", " ,");	// Remove quotes around VALUES
	    jsonCoords = jsonCoords.replace("\"}", " }");	// Remove quotes around VALUES
	    
	    return jsonCoords;	
	}
	

	public boolean addRouteToScheduler( String modeID, String routeID ) {
	    String query = null;
		
	    System.out.println("in addRouteToScheduler()");

	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      Statement st = connect.createStatement();
	      query = "INSERT INTO tracked_routes (mode_name, route_id) " +
	    		  "VALUES ( (SELECT route_mode from x_route_mode WHERE route_type = \"" + modeID + "\"), \"" + routeID + "\" )";
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
					System.out.println("MySQL Exception in addRouteToScheduler().");	
					return false;
	         }
	    }
	    return true;	
	}
	
	public String getFetcherStatus() {
		List<pojoFetcherLog> fetchers = new ArrayList<pojoFetcherLog>();;
		
	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
//	      ps = connect.prepareStatement("SELECT * FROM mbta.fetcher_log" );
	      ps = connect.prepareStatement("SELECT fetcher_log.route_id, fetcher_log.ts_started, fetcher_log.ts_last_update, fetcher_log.message " +
	    		  "FROM fetcher_log " + 
	    		  "INNER JOIN " +
	    		  	"(SELECT route_id, MAX(ts_last_update) as ts " +
	    		  	"FROM fetcher_log " +
	    		  	"GROUP BY route_id) maxt " +
	    		  "ON (fetcher_log.route_id = maxt.route_id AND fetcher_log.ts_last_update = maxt.ts);" );
	      rs=ps.executeQuery();
	      
          while( rs.next() ) {
        	  fetchers.add( new pojoFetcherLog( rs.getString("route_id"), rs.getString("ts_started"), rs.getString("ts_last_update"), rs.getString("message") ) );
          }
	    }catch(SQLException se) {
	        se.printStackTrace();	        //Handle errors for JDBC
	    }catch(Exception e) {
	        e.printStackTrace();	        //Handle errors for Class.forName
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in dbaccess.getFetcherStatus().");	
	         }
	    }
		
	    ObjectWriter ow = new ObjectMapper().writer(); //.withDefaultPrettyPrinter();
    	String jsonRoutes = null;
	    try {
	    	jsonRoutes = ow.writeValueAsString( fetchers );
	    }catch(JsonProcessingException e){
	        e.printStackTrace();
	    }
	    return jsonRoutes;	    
	}
	

	
	
	/* ******************************************************************************
	 * Service thread pool
	 */
	public boolean setSetting( String key, String value ) {
	    String query = null;
		
//	    System.out.println("in setSetting()");

	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      query = "UPDATE settings SET v = ?, event_date = now() WHERE k = ?";
//	      System.out.println( query );
	      ps = connect.prepareStatement( query );
	      ps.setString(1,  value );
	      ps.setString(2,  key );
	      ps.executeUpdate();

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
					System.out.println("MySQL Exception in setSetting().");	
					return false;
	         }
	    }
	    return true;	
	}

	public String getSetting( String key ) {
	    String query = null;
	    String value = null;
		
//	    System.out.println("in getSetting()");

	    try {
	      Class.forName( driver );
	      connect = DriverManager.getConnection( url, user, password );
	      query = "SELECT v FROM settings WHERE k = \"" + key + "\"";
//	      System.out.println( query );
	      ps = connect.prepareStatement( query );
	      rs=ps.executeQuery();
	      if( rs.next() ) {
	    	  value = rs.getString("v");
          }
	      
	    }catch(SQLException se) {
	        se.printStackTrace();
	        return null;
	    }catch(Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	      try {
	          if (connect != null) {
	             connect.close();
	           }
	         } catch (Exception e) {
					System.out.println("MySQL Exception in setSetting().");	
					return null;
	         }
	    }
	    return value;	
	}

	
}
