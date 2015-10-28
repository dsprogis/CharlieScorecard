package com.charliescorecard;

public class pojo_x_current_trip {
	private String _route_id, _trip_id, _trip_name, _shape_id, _vehicle_id, _vehicle_lat, _vehicle_lon, _vehicle_timestamp;

	public pojo_x_current_trip( ) {
		_route_id = null;
		_trip_id = null;
		_trip_name = null;
		_shape_id = null;
		_vehicle_id = null;
		_vehicle_lat = null;
		_vehicle_lon = null;
		_vehicle_timestamp = null;
	}
	public pojo_x_current_trip( String route_id, String trip_id, String trip_name, String shape_id, String vehicle_id, String vehicle_lat, String vehicle_lon, String vehicle_timestamp ) {
		_route_id = route_id;
		_trip_id = trip_id;
		_trip_name = trip_name;
		_shape_id = shape_id;
		_vehicle_id = vehicle_id;
		_vehicle_lat = vehicle_lat;
		_vehicle_lon = vehicle_lon;
		_vehicle_timestamp = vehicle_timestamp;
	}
	
    public String getroute_id() { return _route_id; }
    public void setroute_id(String route_id) { _route_id = route_id; }

    public String gettrip_id() { return _trip_id; }
    public void settrip_id(String trip_id) { _trip_id = trip_id; }

    public String gettrip_name() { return _trip_name; }
    public void settrip_name(String trip_name) { _trip_name = trip_name; }

    public String getshape_id() { return _shape_id; }
    public void setshape_id(String shape_id) { _shape_id = shape_id; }

    public String getvehicle_id() { return _vehicle_id; }
    public void setvehicle_id(String vehicle_id) { _vehicle_id = vehicle_id; }

    public String getvehicle_lat() { return _vehicle_lat; }
    public void setvehicle_lat(String vehicle_lat) { _vehicle_lat = vehicle_lat; }

    public String getvehicle_lon() { return _vehicle_lon; }
    public void setvehicle_lon(String vehicle_lon) { _vehicle_lon = vehicle_lon; }

    public String getvehicle_timestamp() { return _vehicle_timestamp; }
    public void setvehicle_timestamp(String vehicle_timestamp) { _vehicle_timestamp = vehicle_timestamp; }

}