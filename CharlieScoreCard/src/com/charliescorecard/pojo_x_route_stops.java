package com.charliescorecard;

import java.util.List;
/*
 * This Class is used to create a simpler join table between t_trips and t_stops so that t_stop_times is not necessary
 */
public class pojo_x_route_stops {
	private String _route_id;
	private String _stop_sequence;
	private List<String> _stops;
	
	public pojo_x_route_stops( ) {
		_route_id = null;
		_stop_sequence = null;
		_stops = null;
	}
	public pojo_x_route_stops( String route_id, String stop_sequence, List<String> stops ) {
		_route_id = route_id;
		_stop_sequence = stop_sequence;
		_stops = stops;
	}
	
    public String getroute_id() { return _route_id; }
    public void setroute_id(String route_id) { _route_id = route_id; }
	
    public String getstop_sequence() { return _stop_sequence; }
    public void setstop_sequence(String stop_sequence) { _stop_sequence = stop_sequence; }
	
    public List<String> getstops() { return _stops; }
    public void setstops( List<String> stops ) {  _stops = stops; }
    
}
