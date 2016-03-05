package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Label")
public enum Label {
    //setting directly the enum using jackson
    @JsonProperty("RAMP")RAMP,
    @JsonProperty("STAIRS")STAIRS,
    @JsonProperty("ELEVATOR")ELEVATOR,
    @JsonProperty("NONE")NONE,
    @JsonProperty("WASHROOM")WASHROOM,
    @JsonProperty("EXIT")EXIT,
    @JsonProperty("ENTRANCE")ENTRANCE,
    @JsonProperty("EMERGENCY_EXIT")EMERGENCY_EXIT;
}
