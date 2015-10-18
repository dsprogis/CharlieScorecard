package com.charliescorecard;

import java.util.List;
import com.fasterxml.jackson.annotation.*;

public class feedRoute {

	private String id, revision;	// for Ektorp
	
	String route_id, route_name, route_type, mode_name;
    List<feedDirection> direction;
    
    @JsonProperty("_id")
    public String getId() {
            return id;
    }
    @JsonProperty("_id")
    public void setId(String s) {
            id = s;
    }
    @JsonProperty("_rev")
    public String getRevision() {
            return revision;
    }
    @JsonProperty("_rev")
    public void setRevision(String s) {
            revision = s;
    }
    
    public String getRoute_id() {
        return route_id;
    }
    public void setRoute_id( String route_id ) {
        this.route_id = route_id;
    }
    public String getRoute_name() {
        return route_name;
    }
    public void setRoute_name( String route_name ) {
        this.route_name = route_name;
    }
    public String getRoute_type() {
        return route_type;
    }
    public void setRoute_type( String route_type ) {
        this.route_type = route_type;
    }
    public String getMode_name() {
        return mode_name;
    }
    public void setMode_name( String mode_name ) {
        this.mode_name = mode_name;
    }
    
    public List<feedDirection> getDirection() {
        return direction;
    }
    public void setDirection( List<feedDirection> direction ) {
        this.direction = direction;
    }

}

