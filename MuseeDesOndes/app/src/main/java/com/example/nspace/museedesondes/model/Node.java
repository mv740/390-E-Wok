package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.*;

/**
 * Created by michal on 1/28/2016.
 */
@JsonTypeName("Node")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PointOfInterest.class, name = "PointOfInterest"),
        @JsonSubTypes.Type(value = LabelledPoint.class, name = "LabelledPoint")
})
public class Node {
    private int id;
    private int floorID;
    private double x;
    private double y;

    public Node(@JsonProperty("id") int id,
                @JsonProperty("floorID") int floorID,
                @JsonProperty("x") double x,
                @JsonProperty("y") double y) {

        this.id = id;
        this.floorID = floorID;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public int getFloorID() {
        return floorID;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
