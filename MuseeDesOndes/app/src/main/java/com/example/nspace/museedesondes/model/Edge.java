package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by michal on 1/28/2016.
 */
public class Edge {

    private int distance;
    private Node start;
    private Node end;
    private int startID;
    private int endID;

    public Edge(@JsonProperty("startNode") int startID,
                @JsonProperty("endNode") int endID,
                @JsonProperty("distance") int distance) {
        this.startID = startID;
        this.endID = endID;
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public int getStartID() {
        return startID;
    }

    public int getEndID() {
        return endID;
    }

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

}
