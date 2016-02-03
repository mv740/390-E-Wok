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
        @JsonSubTypes.Type(value=PointOfInterest.class, name="PointOfInterest")
})
public class Node {
    private int id;
    private int floor;
    private ArrayList edge;
    private int coordinate_X;
    private int coordinate_Y;

    public Node(@JsonProperty("id") int id, @JsonProperty("floor")int floor, @JsonProperty("edge") ArrayList edge, @JsonProperty("coordinate.x") int coordinate_X, @JsonProperty("coordinate.y")int coordinate_Y) {

        this.id = id;
        this.floor = floor;
        this.edge = edge;
        this.coordinate_X = coordinate_X;
        this.coordinate_Y = coordinate_Y;
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

    public int getCoordinate_X() {
        return coordinate_X;
    }

    public int getCoordinate_Y() {
        return coordinate_Y;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", floor=" + floor +
                ", edge=" + edge +
                ", coordinate_X=" + coordinate_X +
                ", coordinate_Y=" + coordinate_Y +
                '}';
    }
}
