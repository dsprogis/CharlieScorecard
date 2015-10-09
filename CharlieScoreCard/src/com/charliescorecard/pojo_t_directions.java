package com.charliescorecard;

/*
 * This Class is used to contain the Direction portion of t_trips.
 * t_trips is de-normalized from (1) trips, (2) directions, and (3) services
 * Directions are outbound and inbound
 * Services are weekday, Saturday, Sunday and there appear to be others as well 
 */

public class pojo_t_directions {
		private String _direction_id;
		private String _trip_headsign;
		
		public pojo_t_directions( ) {
			_direction_id = null;
			_trip_headsign = null;
		}
		public pojo_t_directions( String direction_id, String trip_headsign ) {
			_direction_id = direction_id;
			_trip_headsign = trip_headsign;
		}
		
	    public String getdirection_id() { return _direction_id; }
	    public void setdirection_id(String direction_id) { _direction_id = direction_id; }
	
	    public String gettrip_headsign() { return _trip_headsign; }
	    public void settrip_headsign(String trip_headsign) { _trip_headsign = trip_headsign; }
	
}
