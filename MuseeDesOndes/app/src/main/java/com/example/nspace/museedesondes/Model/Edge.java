package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
public class Edge {

    private int weight;
    private Node start;
    private Node end;
    private int startID;
    private int endID;

    public Edge(@JsonProperty("weight") int weight,
                @JsonProperty("start") int startID,
                @JsonProperty("end") int endID) {
        this.weight = weight;
        this.startID = startID;
        this.endID = endID;
    }


    public int getWeight() {
        return weight;
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
