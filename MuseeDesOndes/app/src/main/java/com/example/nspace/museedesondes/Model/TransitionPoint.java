package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;

/**
 * Created by michal on 2/9/2016.
 */
@JsonTypeName("TransitionPoint")
public class TransitionPoint extends Node {

    private Label type;

    public TransitionPoint(@JsonProperty("id") int id,
                           @JsonProperty("floorPlan") FloorPlan floor,
                           @JsonProperty("edge") ArrayList edge,
                           @JsonProperty("coordinate") Coordinate coordinate,
                           @JsonProperty("transitionType") String transitionType) {
        super(id, floor, edge, coordinate);
        this.type = Label.getEnum(transitionType);
    }
    public Label getType() {
        return type;
    }
}
