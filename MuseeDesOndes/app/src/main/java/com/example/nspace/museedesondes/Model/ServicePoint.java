package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;

/**
 * Created by michal on 2/6/2016.
 */
@JsonTypeName("ServicePoint")
public class ServicePoint extends Node{

    private Label label;

    public ServicePoint(@JsonProperty("id") int id,
                        @JsonProperty("floor") FloorPlan floor,
                        @JsonProperty("edge") ArrayList edge,
                        @JsonProperty("coordinate") Coordinate coordinate,
                        @JsonProperty("label") Label  label) {
        super(id, floor, edge, coordinate);

        this.label = label;//Label.getEnum(label);
    }

    public Label getLabel() {
        return label;
    }
}
