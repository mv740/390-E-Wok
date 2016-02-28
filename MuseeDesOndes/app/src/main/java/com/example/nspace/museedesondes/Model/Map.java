package com.example.nspace.museedesondes.Model;

import android.content.Context;

import com.example.nspace.museedesondes.Utility.JsonHelper;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
    private ArrayList<LabelledPoint> labelledPoints;
    private ArrayList<FloorPlan> floorPlans;
    private ArrayList<Point> point;


    private static Map instance = null;

    private Map(@JsonProperty("node") ArrayList<Point> point,
                @JsonProperty("edge") ArrayList<Edge> edges,
                @JsonProperty("storyLine") ArrayList<StoryLine> storyLines,
                @JsonProperty("floorPlan") ArrayList<FloorPlan> floorPlans) {

        this.point = point;
        this.edges = edges;
        this.storyLines = storyLines;
        this.floorPlans = floorPlans;
        this.pointOfInterests = new ArrayList<>();
        this.labelledPoints = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    public static Map getInstance(Context context) {
        if (instance == null) {
            String mapSource = JsonHelper.loadJSON("map.json", context);
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(DeserializationFeature. ACCEPT_SINGLE_VALUE_AS_ARRAY);
            try {
                instance = mapper.readValue((mapSource), Map.class);
                if (instance != null)
                    initializeNodes();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return instance;
    }

    private static void initializeNodes() {
        instance.pointOfInterests = instance.point.get(0).getPoi();
        instance.labelledPoints = instance.point.get(0).getPot();
        instance.nodes.addAll(instance.pointOfInterests);
        instance.nodes.addAll(instance.labelledPoints);
        setNodesReferenceForEdges();
        setNodesReferenceForStorylines();
    }


    private static void setNodesReferenceForStorylines() {
        for (StoryLine storyLine : instance.storyLines) {
            for (Integer id : storyLine.getPath()) {
                storyLine.addNodeReference(instance.searchNodeById(id));
            }
        }
    }

    private static void setNodesReferenceForEdges() {
        for (Edge edge : instance.edges) {
            edge.setStart(instance.searchNodeById(edge.getStartID()));
            edge.setEnd(instance.searchNodeById(edge.getEndID()));
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

    public Node searchNodeById(int id) {

        for (Node node : nodes) {
            if (node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    public PointOfInterest searchPoiByTitle(String title) {
        for (PointOfInterest poi : pointOfInterests) {
            for (PointOfInterestDescription description : poi.getDescriptions()) {
                if (description.getTitle().equalsIgnoreCase(title)) {
                    return poi;
                }
            }
        }
        return null;
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


    public ArrayList<LabelledPoint> getLabelledPoints() {
        return labelledPoints;
    }

    public ArrayList<FloorPlan> getFloorPlans() {
        return floorPlans;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<PointOfInterest> getPointOfInterestsCurrentFloor(int currentFloor) {
        ArrayList<PointOfInterest> currentFloorList = new ArrayList<>();
        for (PointOfInterest poi : pointOfInterests) {
            if (poi.getFloorID() == currentFloor) {
                currentFloorList.add(poi);
            }
        }
        return currentFloorList;
    }


    public ArrayList<LabelledPoint> getLabelledPointsCurrentFloor(int currentFloor) {
        ArrayList<LabelledPoint> currentFloorList = new ArrayList<>();
        for (LabelledPoint labelledPoint : labelledPoints) {
            if (labelledPoint.getFloorID() == currentFloor) {
                currentFloorList.add(labelledPoint);
            }
        }
        return currentFloorList;
    }

    private ArrayList<Point> getPoint() {
        return point;
    }
}
