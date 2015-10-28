package com.charliescorecard;

public class pojo_x_heat_trip {
	private String _day, _hour, _value;

	public pojo_x_heat_trip( ) {
		_day = null;
		_hour = null;
		_value = null;

	}
	public pojo_x_heat_trip( String day, String hour, String value ) {
		_day = day;
		_hour = hour;
		_value = value;
	}
	
    public String getday() { return _day; }
    public void setday(String day) { _day = day; }
	
    public String gethour() { return _hour; }
    public void sethour(String hour) { _hour = hour; }
	
    public String getvalue() { return _value; }
    public void setvalue(String value) { _value = value; }
	
}
