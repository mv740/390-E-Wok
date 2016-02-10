package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;

/**
 * Created by michal on 2/9/2016.
 */
@JsonTypeName("TransitionPoint")
public class TransitionPoint extends Node {

    private TransitionType type;

    public TransitionPoint(@JsonProperty("id") int id,
                           @JsonProperty("floor") int floor,
                           @JsonProperty("edge") ArrayList edge,
                           @JsonProperty("coordinate") Coordinate coordinate,
                           @JsonProperty("transitionType") String transitionType) {
        super(id, floor, edge, coordinate);
        this.type = TransitionType.getEnum(transitionType);
    }
    public TransitionType getType() {
        return type;
    }
}
