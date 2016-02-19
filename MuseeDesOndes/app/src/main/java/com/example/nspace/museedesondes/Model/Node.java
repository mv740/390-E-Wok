package com.example.nspace.museedesondes.Model;

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
    private FloorPlan floorPlan;
    private double x;
    private double y;

    public Node(@JsonProperty("id") int id,
                @JsonProperty("floorPlan") FloorPlan floorPlan,
                @JsonProperty("x") double x,
                @JsonProperty("y") double y) {

        this.id = id;
        this.floorPlan = floorPlan;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public FloorPlan getFloorPlan() {
        return floorPlan;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
