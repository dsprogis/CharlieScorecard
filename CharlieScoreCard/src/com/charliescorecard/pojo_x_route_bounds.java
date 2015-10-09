package com.charliescorecard;

public class pojo_x_route_bounds {
	private String _route_id, _min_lat, _max_lat, _min_lng, _max_lng;

	public pojo_x_route_bounds( ) {
		_route_id = null;
		_min_lat = null;
		_max_lat = null;
		_min_lng = null;
		_max_lng = null;
	}
	public pojo_x_route_bounds( String route_id, String min_lat, String max_lat, String min_lng, String max_lng ) {
		_route_id = route_id;
		_min_lat = min_lat;
		_max_lat = max_lat;
		_min_lng = min_lng;
		_max_lng = max_lng;
	}
	
    public String getroute_id() { return _route_id; }
    public void setroute_id(String route_id) { _route_id = route_id; }
	
    public String getmin_lat() { return _min_lat; }
    public void setmin_lat(String min_lat) { _min_lat = min_lat; }
	
    public String getmax_lat() { return _max_lat; }
    public void setmax_lat(String max_lat) { _max_lat = max_lat; }
	
    public String getmin_lng() { return _min_lng; }
    public void setmin_lng(String min_lng) { _min_lng = min_lng; }
	
    public String getmax_lng() { return _max_lng; }
    public void setmax_lng(String max_lng) { _max_lng = max_lng; }
	
}
