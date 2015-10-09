package com.charliescorecard;

public class pojo_t_routes {
	private String _route_id, _agency_id, _route_short_name, _route_long_name, _route_desc, _route_type, _route_url, _route_color, _route_text_color, _route_sort_order;

	public pojo_t_routes( ) {
		_route_id = null;
		_agency_id = null;
		_route_short_name = null;
		_route_long_name = null;
		_route_desc = null;
		_route_type = null;
		_route_url = null;
		_route_color = null;
		_route_text_color = null;
		_route_sort_order = null;
	}
	public pojo_t_routes( String route_id, String agency_id, String route_short_name, String route_long_name, String route_desc, String route_type, String route_url, String route_color, String route_text_color, String route_sort_order ) {
		_route_id = route_id;
		_agency_id = agency_id;
		_route_short_name = route_short_name;
		_route_long_name = route_long_name;
		_route_desc = route_desc;
		_route_type = route_type;
		_route_url = route_url;
		_route_color = route_color;
		_route_text_color = route_text_color;
		_route_sort_order = route_sort_order;
	}
	
    public String getroute_id() { return _route_id; }
    public void setroute_id(String route_id) { _route_id = route_id; }
	
    public String getagency_id() { return _agency_id; }
    public void setagency_id(String agency_id) { _agency_id = agency_id; }
	
    public String getroute_short_name() { return _route_short_name; }
    public void setroute_short_name(String route_short_name) { _route_short_name = route_short_name; }
	
    public String getroute_long_name() { return _route_long_name; }
    public void setroute_long_name(String route_long_name) { _route_long_name = route_long_name; }
	
    public String getroute_desc() { return _route_desc; }
    public void setroute_desc(String route_desc) { _route_desc = route_desc; }
	
    public String getroute_type() { return _route_type; }
    public void setroute_type(String route_type) { _route_type = route_type; }
	
    public String getroute_url() { return _route_url; }
    public void setroute_url(String route_url) { _route_url = route_url; }
	
    public String getroute_color() { return _route_color; }
    public void setroute_color(String route_color) { _route_color = route_color; }
	
    public String getroute_text_color() { return _route_text_color; }
    public void setroute_text_color(String route_text_color) { _route_text_color = route_text_color; }
	
    public String getroute_sort_order() { return _route_sort_order; }
    public void setroute_sort_order(String route_sort_order) { _route_sort_order = route_sort_order; }
	
}
