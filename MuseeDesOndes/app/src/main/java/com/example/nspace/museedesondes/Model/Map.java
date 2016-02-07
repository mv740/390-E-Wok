package com.example.nspace.museedesondes.Model;

import android.content.Context;

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
    private ArrayList<BuildingPoint> buildingPoints;

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
        this.buildingPoints = new ArrayList<>();
    }

    public static Map getInstance(Context context) {
        if (instance == null) {
            String mapSource = JsonHelper.loadJSON("map.json", context);
            ObjectMapper mapper = new ObjectMapper();
            try {
                instance = mapper.readValue((mapSource), Map.class);
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
            } else if (node instanceof BuildingPoint) {
                instance.buildingPoints.add((BuildingPoint) node);
            }
        }
    }

    public PointOfInterest searchPoiById(int id) {
        for (PointOfInterest poi : pointOfInterests) {
            if (poi.getId() == id) {
                return poi;
            }
        }
        return null;
    }

    public BuildingPoint searchBuildingPointById(int id) {
        for (BuildingPoint buildingPoint : buildingPoints) {
            if (buildingPoint.getId() == id) {
                return buildingPoint;
            }
        }
        return null;
    }

    public Node searchNodeById(int id) {

        for (Node node : nodes) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    public Edge searchEdgeById(int id) {

        for (Edge edge : edges) {
            if (edge.getId() == id) {
                return edge;
            }
        }
        return null;
    }

    public ArrayList getEdgesOfNodeById(int id) {
        return searchNodeById(id).getEdge();
    }


    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public ArrayList<Storyline> getStorylines() {
        return storylines;
    }

    public ArrayList<PointOfInterest> getPointOfInterests() {
        return pointOfInterests;
    }

    public ArrayList<BuildingPoint> getBuildingPoints() {
        return buildingPoints;
    }
}
