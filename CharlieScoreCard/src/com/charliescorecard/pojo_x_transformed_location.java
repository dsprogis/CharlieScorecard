package com.charliescorecard;

public class pojo_x_transformed_location {
	private String _route_id, _trip_id, _trip_name, _direction_id, _stop_id, _stop_sequence, _vehicle_id, _stop_lat, _stop_lon, _actual_timestamp, _scheduled_timestamp, _time_diff;

	public pojo_x_transformed_location( ) {
		_route_id = null;
		_trip_id = null;
		_trip_name = null;
		_direction_id = null;
		_stop_id = null;
		_stop_sequence = null;
		_vehicle_id = null;
		_stop_lat = null;
		_stop_lon = null;
		_actual_timestamp = null;
		_scheduled_timestamp = null;
		_time_diff = null;
	}
	public pojo_x_transformed_location( String route_id, String trip_id, String trip_name, String direction_id, String stop_id, String stop_sequence, String vehicle_id, String stop_lat, String stop_lon, String actual_timestamp, String scheduled_timestamp, String time_diff ) {
		_route_id = route_id;
		_trip_id = trip_id;
		_trip_name = trip_name;
		_direction_id = direction_id;
		_stop_id = stop_id;
		_stop_sequence = stop_sequence;
		_vehicle_id = vehicle_id;
		_stop_lat = stop_lat;
		_stop_lon = stop_lon;
		_actual_timestamp = actual_timestamp;
		_scheduled_timestamp = scheduled_timestamp;
		_time_diff = time_diff;
	}
	
    public String getroute_id() { return _route_id; }
    public void setroute_id(String route_id) { _route_id = route_id; }

    public String gettrip_id() { return _trip_id; }
    public void settrip_id(String trip_id) { _trip_id = trip_id; }

    public String gettrip_name() { return _trip_name; }
    public void settrip_name(String trip_name) { _trip_name = trip_name; }

    public String getdirection_id() { return _direction_id; }
    public void setdirection_id(String direction_id) { _direction_id = direction_id; }

    public String getstop_id() { return _stop_id; }
    public void setstop_id(String stop_id) { _stop_id = stop_id; }

    public String getstop_sequence() { return _stop_sequence; }
    public void setstop_sequence(String stop_sequence) { _stop_sequence = stop_sequence; }
    
    public String getvehicle_id() { return _vehicle_id; }
    public void setvehicle_id(String vehicle_id) { _vehicle_id = vehicle_id; }

    public String getstop_lat() { return _stop_lat; }
    public void setstop_lat(String stop_lat) { _stop_lat = stop_lat; }

    public String getstop_lon() { return _stop_lon; }
    public void setstop_lon(String stop_lon) { _stop_lon = stop_lon; }

    public String getactual_timestamp() { return _actual_timestamp; }
    public void setactual_timestamp(String actual_timestamp) { _actual_timestamp = actual_timestamp; }

    public String getscheduled_timestamp() { return _scheduled_timestamp; }
    public void setscheduled_timestamp(String scheduled_timestamp) { _scheduled_timestamp = scheduled_timestamp; }

    public String gettime_diff() { return _time_diff; }
    public void settime_diff(String time_diff) { _time_diff = time_diff; }

}