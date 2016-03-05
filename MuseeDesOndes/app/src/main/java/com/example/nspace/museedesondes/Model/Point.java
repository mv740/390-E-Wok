package com.example.nspace.museedesondes.model;

import com.example.nspace.museedesondes.deserializer.POIDeserialize;
import com.example.nspace.museedesondes.deserializer.LabelledPointDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 2/18/2016.
 */
@JsonTypeName("Point")
public class Point {

    @JsonDeserialize(using = POIDeserialize.class)
    private List<PointOfInterest> poi;
    @JsonDeserialize(using = LabelledPointDeserialize.class)
    private List<LabelledPoint> pot;

    public Point(@JsonProperty("poi") ArrayList<PointOfInterest> poi, @JsonProperty("pot") ArrayList<LabelledPoint> pot) {
        this.poi = poi;
        this.pot = pot;
    }

    public List<PointOfInterest> getPoi() {
        return poi;
    }

    public List<LabelledPoint> getPot() {
        return pot;
    }
}
