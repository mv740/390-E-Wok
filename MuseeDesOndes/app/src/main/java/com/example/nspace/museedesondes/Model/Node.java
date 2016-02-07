package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
@JsonTypeName("Node")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value=PointOfInterest.class, name="PointOfInterest"),
        @JsonSubTypes.Type(value=BuildingPoint.class, name="BuildingPoint")
})
public class Node {
    private int id;
    private int floor;
    private ArrayList edge;
    private Coordinate coordinate;

    public Node(@JsonProperty("id") int id, @JsonProperty("floor")int floor, @JsonProperty("edge") ArrayList edge, @JsonProperty("coordinate") Coordinate coordinate) {

        this.id = id;
        this.floor = floor;
        this.edge = edge;
        this.coordinate = coordinate;
    }

    public int getId() {
        return id;
    }

    public int getFloor() {
        return floor;
    }

    public ArrayList getEdge() {
        return edge;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
