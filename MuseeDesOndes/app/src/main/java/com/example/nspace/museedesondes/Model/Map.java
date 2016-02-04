package com.example.nspace.museedesondes.Model;

import android.app.Activity;

import com.example.nspace.museedesondes.Utility.JsonHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
public class Map {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private ArrayList<Storyline> storylines;

    private ArrayList<PointOfInterest> pointOfInterests;


    private static Map instance = null;

    private Map(@JsonProperty("node") ArrayList<Node> nodes, @JsonProperty("edge") ArrayList<Edge> edges, @JsonProperty("storyline") ArrayList<Storyline> storylines) {

        //load json
        //create nodes
        //create edges
        //create Storyline
        this.nodes = nodes;
        this.edges = edges;
        this.storylines = storylines;
        this.pointOfInterests = new ArrayList<>();
    }

    public static Map getInstance(Activity activity) {
        if (instance == null) {
            String mapSource = JsonHelper.loadJSON("map.json", activity);
            System.out.println(mapSource);
            ObjectMapper mapper = new ObjectMapper();
            try {
                instance = mapper.readValue(mapSource, Map.class);
                if (instance != null)
                    filterNodes();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return instance;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }


    private static void filterNodes() {

        for (Node node : instance.getNodes()) {
            if (node instanceof PointOfInterest) {
                instance.pointOfInterests.add((PointOfInterest) node);
            }
        }
    }

    public ArrayList<PointOfInterest> getPointOfInterest() {
        return pointOfInterests;
    }


    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Storyline> getStorylines() {
        return storylines;
    }
}
