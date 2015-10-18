package com.charliescorecard;

public class pojoFetcherLog {
	private String _route_id, _ts_started, _ts_last_update, _message;

	public pojoFetcherLog( ) {
		_route_id = null;
		_ts_started = null;
		_ts_last_update = null;
		_message = null;		
	}
	public pojoFetcherLog( String route_id, String ts_started, String ts_last_update, String message ) {
		_route_id = route_id;
		_ts_started = ts_started;
		_ts_last_update = ts_last_update;
		_message = message;		
	}
	
    public String getroute_id() { return _route_id; }
    public void setroute_id(String route_id) { _route_id = route_id; }
    
    public String getts_started() { return _ts_started; }
    public void setts_started(String ts_started) { _ts_started = ts_started; }
    
    public String getts_last_update() { return _ts_last_update; }
    public void setts_last_update(String ts_last_update) { _ts_last_update = ts_last_update; }
    
    public String getmessage() { return _message; }
    public void setmessage(String message) { _message = message; }
    
}
