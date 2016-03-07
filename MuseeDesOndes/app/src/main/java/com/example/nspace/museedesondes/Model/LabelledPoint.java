package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/9/2016.
 */
@JsonTypeName("LabelledPoint")
public class LabelledPoint extends Node {

    private Label label;

    public LabelledPoint(@JsonProperty("id") int id,
                         @JsonProperty("floorID") int floorID,
                         @JsonProperty("x") double x,
                         @JsonProperty("y") double y,
                         @JsonProperty("label") Label label) {
        super(id, floorID, x, y);
        this.label = label;//Label.getEnum(label);
    }
    public Label getLabel() {
        return label;
    }
}
