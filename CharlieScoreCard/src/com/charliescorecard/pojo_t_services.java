package com.charliescorecard;
/*
 * This Class is used to contain the Service portion of t_trips.
 * t_trips is de-normalized from (1) trips, (2) directions, and (3) services
 * Directions are outbound and inbound
 * Services are weekday, Saturday, Sunday and there appear to be others as well 
 * Shape_id is the geocoded shape of the route.
 */

public class pojo_t_services {
	private String _service_id;
	private String _shape_id;
	private String _trip_count;
	
	public pojo_t_services( ) {
		_service_id = null;
		_shape_id = null;
		_trip_count = null;
	}
	public pojo_t_services( String service_id, String shape_id, String trip_count ) {
		_service_id = service_id;
		_shape_id = shape_id;
		_trip_count = trip_count;
	}
	
    public String getservice_id() { return _service_id; }
    public void setservice_id(String service_id) { _service_id = service_id; }

    public String getshape_id() { return _shape_id; }
    public void setshape_id(String shape_id) { _shape_id = shape_id; }
	
    public String gettrip_count() { return _trip_count; }
    public void settrip_count(String trip_count) { _trip_count = trip_count; }
	
}
