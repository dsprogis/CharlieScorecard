package com.charliescorecard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    
    String str = new String();
	
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
	        //Handle errors for JDBC
	        se.printStackTrace();
	    }catch(Exception e) {
	        //Handle errors for Class.forName
	        e.printStackTrace();
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
	
	
}
