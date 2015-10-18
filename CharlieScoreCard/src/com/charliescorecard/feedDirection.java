package com.charliescorecard;

import java.util.List;

public class feedDirection {
    String direction_id, direction_name;
    List<feedTrip> trip;
    
    public String getDirection_id() {
        return direction_id;
    }
    public void setDirection_id( String direction_id ) {
        this.direction_id = direction_id;
    }
    public String getDirection_name() {
        return direction_name;
    }
    public void setDirection_name( String direction_name ) {
        this.direction_name = direction_name;
    }
    
    public List<feedTrip> getTrip() {
        return trip;
    }
    public void setTrip( List<feedTrip> trip ) {
        this.trip = trip;
    }
}
