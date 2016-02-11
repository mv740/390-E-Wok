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
    private ArrayList<StoryLine> storyLines;
    private ArrayList<PointOfInterest> pointOfInterests;
    private ArrayList<ServicePoint> servicePoints;
    private ArrayList<TransitionPoint> transitionPoints;

    private static Map instance = null;

    private Map(@JsonProperty("node") ArrayList<Node> nodes, @JsonProperty("edge") ArrayList<Edge> edges, @JsonProperty("storyLines") ArrayList<StoryLine> storyLines) {

        //load json
        //create nodes
        //create edges
        //create StoryLine
        this.nodes = nodes;
        this.edges = edges;
        this.storyLines = storyLines;
        this.pointOfInterests = new ArrayList<>();
        this.servicePoints = new ArrayList<>();
        this.transitionPoints = new ArrayList<>();
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
            } else if (node instanceof ServicePoint) {
                instance.servicePoints.add((ServicePoint) node);
            } else if( node instanceof TransitionPoint)
            {
                instance.transitionPoints.add((TransitionPoint)node);
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

    public ServicePoint searchServicePointById(int id) {
        for (ServicePoint servicePoint : servicePoints) {
            if (servicePoint.getId() == id) {
                return servicePoint;
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

    public ArrayList<StoryLine> getStoryLines() {
        return storyLines;
    }

    public ArrayList<PointOfInterest> getPointOfInterests() {
        return pointOfInterests;
    }

    public ArrayList<ServicePoint> getServicePoints() {
        return servicePoints;
    }

    public ArrayList<TransitionPoint> getTransitionPoints() {
        return transitionPoints;
    }
}
