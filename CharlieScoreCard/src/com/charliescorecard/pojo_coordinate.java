package com.charliescorecard;

public class pojo_coordinate {

	private String _lat, _lng;

	public pojo_coordinate( String lat, String lng ) {
		_lat = lat;
		_lng = lng;
	}
	
    public String getLat() { return _lat; }
    public void setLat( String lat ) { _lat = lat; }

    public String getLng() { return _lng; }
    public void setLng( String lng ) { _lng = lng; }

}
