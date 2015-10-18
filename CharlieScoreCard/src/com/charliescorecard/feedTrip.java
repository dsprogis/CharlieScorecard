package com.charliescorecard;

public class feedTrip {
    String trip_id, trip_name, trip_headsign;
    feedVehicle vehicle;
    
    public String getTrip_id() {
        return trip_id;
    }
    public void setTrip_id( String trip_id ) {
        this.trip_id = trip_id;
    }
    public String getTrip_name() {
        return trip_name;
    }
    public void setTrip_name( String trip_name ) {
        this.trip_name = trip_name;
    }
    public String getTrip_headsign() {
        return trip_headsign;
    }
    public void setTrip_headsign( String trip_headsign ) {
        this.trip_headsign = trip_headsign;
    }
    public feedVehicle getVehicle() {
        return vehicle;
    }
    public void setVehicle( feedVehicle vehicle ) {
        this.vehicle = vehicle;
    }
    
    
}
