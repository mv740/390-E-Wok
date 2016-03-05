package com.example.nspace.museedesondes.model;

import com.example.nspace.museedesondes.deserializer.POIDeserialize;
import com.example.nspace.museedesondes.deserializer.LabelledPointDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

/**
 * Created by michal on 2/18/2016.
 */
@JsonTypeName("Point")
public class Point {

    @JsonDeserialize(using = POIDeserialize.class)
    private ArrayList<PointOfInterest> poi;
    @JsonDeserialize(using = LabelledPointDeserialize.class)
    private ArrayList<LabelledPoint> pot;

    public Point(@JsonProperty("poi") ArrayList<PointOfInterest> poi, @JsonProperty("pot") ArrayList<LabelledPoint> pot) {
        this.poi = poi;
        this.pot = pot;
    }

    public ArrayList<PointOfInterest> getPoi() {
        return poi;
    }

    public ArrayList<LabelledPoint> getPot() {
        return pot;
    }
}
