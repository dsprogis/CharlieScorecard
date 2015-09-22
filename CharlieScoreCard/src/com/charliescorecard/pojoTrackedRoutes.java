package com.charliescorecard;

public class pojoTrackedRoutes {
	private String _mode_name, _route_id, _date_started, _date_last_touched;

	public pojoTrackedRoutes( ) {
		_mode_name = null;
		_route_id = null;
		_date_started = null;
		_date_last_touched = null;		
	}
	public pojoTrackedRoutes( String mode_name, String route_id, String date_started, String date_last_touched ) {
		_mode_name = mode_name;
		_route_id = route_id;
		_date_started = date_started;
		_date_last_touched = date_last_touched;
	}
	
    public String getmode_name() { return _mode_name; }
    public void setmode_name(String mode_name) { _mode_name = mode_name; }
    
    public String getroute_id() { return _route_id; }
    public void setroute_id(String route_id) { _route_id = route_id; }
    
    public String getdate_started() { return _date_started; }
    public void setdate_started(String date_started) { _date_started = date_started; }
       
    public String getdate_last_touched() { return _date_last_touched; }
    public void setdate_last_touched(String date_last_touched) { _date_started = date_last_touched; }
        
}
