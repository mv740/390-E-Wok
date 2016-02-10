package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
public class Edge {

    private int id;
    private int weight;
    private ArrayList<Node> node;

    public Edge(@JsonProperty("id")int id,@JsonProperty("weight") int weight, @JsonProperty("node")ArrayList node) {
        this.id = id;
        this.weight = weight;
        this.node = node;
    }

    public int getId() {
        return id;
    }

    public int getWeight() {
        return weight;
    }

    public ArrayList getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id=" + id +
                ", weight=" + weight +
                ", node=" + node +
                '}';
    }
}
