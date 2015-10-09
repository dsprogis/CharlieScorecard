package com.charliescorecard;

public class pojo_x_route_mode {
	private String _route_type, _route_mode;

	public pojo_x_route_mode( ) {
		_route_type = null;
		_route_mode = null;
	}
	public pojo_x_route_mode( String route_type, String route_mode ) {
		_route_type = route_type;
		_route_mode = route_mode;
	}
	
    public String getroute_type() { return _route_type; }
    public void setroute_type(String route_type) { _route_type = route_type; }
    
    public String getroute_mode() { return _route_mode; }
    public void setroute_mode(String route_mode) { _route_mode = route_mode; }
    
}
