package com.example.nspace.museedesondes.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.nspace.museedesondes.utility.JsonHelper;
import com.example.nspace.museedesondes.utility.Resource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 1/28/2016.
 */
public class MuseumMap {
    private List<Node> nodes;
    private List<Edge> edges;
    private List<StoryLine> storyLines;
    private List<PointOfInterest> pointOfInterests;
    private List<LabelledPoint> labelledPoints;
    private List<FloorPlan> floorPlans;
    private List<Point> point;
    private boolean coordinateAlreadyConverted;


    private static MuseumMap instance = null;

    private MuseumMap(@JsonProperty("node") ArrayList<Point> point,
                      @JsonProperty("edge") ArrayList<Edge> edges,
                      @JsonProperty("storyline") ArrayList<StoryLine> storyLines,
                      @JsonProperty("floorPlan") ArrayList<FloorPlan> floorPlans) {

        this.point = point;
        this.edges = edges;
        this.storyLines = storyLines;
        this.floorPlans = floorPlans;
        this.pointOfInterests = new ArrayList<>();
        this.labelledPoints = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.coordinateAlreadyConverted = false;
    }

    public static MuseumMap getInstance(Context context) {
        if (instance == null) {
            //String mapSource = JsonHelper.loadJSON("map.json", context);
            FileInputStream test = JsonHelper.loadJsonFile("mapOnline.json", context);
            ObjectMapper mapper = new ObjectMapper();

            try {
                instance = mapper.readValue(test, MuseumMap.class);
                if (instance != null) {
                    initializeNodes();

                    SharedPreferences sharedPrefs = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                    if (!sharedPrefs.getBoolean("firstrun", true)){
                        Log.e("Map", "sanitizePath");
                        sanitizePath(context);
                    }
                }

            } catch (IOException e) {
                Log.e("JsonHelper", Log.getStackTraceString(e));
            }

        }
        return instance;
    }

    private static void sanitizePath(Context context) {
        final String URL_DASH = "/";

        for (StoryLine storyLine : instance.getStoryLines()) {
            storyLine.setImagePath(Resource.getSanitizedFileNameWithoutDirectories(URL_DASH + storyLine.getImagePath()));
        }
        for (PointOfInterest pot : instance.getPointOfInterests()) {

            for (Image image : pot.getAllImages(context)) {
                image.setPath(Resource.getSanitizedFileNameWithoutDirectories(URL_DASH + image.getPath()));
            }
            for (Video video : pot.getAllVideos(context)) {
                video.setPath(Resource.getSanitizedFileNameWithoutDirectories(URL_DASH + video.getPath()));
            }
            for (Audio audio : pot.getAllAudios(context)) {
                audio.setPath(Resource.getSanitizedFileNameWithoutDirectories(URL_DASH + audio.getPath()));
            }
        }
        for(FloorPlan floorPlan: instance.getFloorPlans())
        {
            floorPlan.setImagePath(Resource.getSanitizedFileNameWithoutDirectories(URL_DASH + floorPlan.getImagePath()));
        }
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

    public StoryLine searchStorylineById(int id) {
        for (StoryLine currentStoryline : storyLines) {
            if (currentStoryline.getId() == id) {
                return currentStoryline;
            }
        }
        return null;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<StoryLine> getStoryLines() {
        return storyLines;
    }

    public List<PointOfInterest> getPointOfInterests() {
        return pointOfInterests;
    }


    public List<LabelledPoint> getLabelledPoints() {
        return labelledPoints;
    }

    public List<FloorPlan> getFloorPlans() {
        return floorPlans;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<PointOfInterest> getPointOfInterestsCurrentFloor(int currentFloor) {
        List<PointOfInterest> currentFloorList = new ArrayList<>();
        for (PointOfInterest poi : pointOfInterests) {
            if (poi.getFloorID() == currentFloor) {
                currentFloorList.add(poi);
            }
        }
        return currentFloorList;
    }


    public List<LabelledPoint> getLabelledPointsCurrentFloor(int currentFloor) {
        List<LabelledPoint> currentFloorList = new ArrayList<>();
        for (LabelledPoint labelledPoint : labelledPoints) {
            if (labelledPoint.getFloorID() == currentFloor) {
                currentFloorList.add(labelledPoint);
            }
        }
        return currentFloorList;
    }

    public boolean isCoordinateAlreadyConverted() {
        return coordinateAlreadyConverted;
    }

    public void setCoordinateAlreadyConverted(boolean coordinateAlreadyConverted) {
        this.coordinateAlreadyConverted = coordinateAlreadyConverted;
    }
}
