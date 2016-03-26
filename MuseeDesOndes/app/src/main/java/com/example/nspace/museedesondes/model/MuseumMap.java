package com.example.nspace.museedesondes.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.nspace.museedesondes.adapters.CoordinateAdapter;
import com.example.nspace.museedesondes.utility.JsonHelper;
import com.example.nspace.museedesondes.utility.Resource;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

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


    private static MuseumMap instance = null;

    private MuseumMap(@JsonProperty("node") ArrayList<Point> point,
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

    public static MuseumMap getInstance(Context context) {
        if (instance == null) {
            String mapSource = JsonHelper.loadJSON("map.json", context);
            ObjectMapper mapper = new ObjectMapper();

            try {
                instance = mapper.readValue(mapSource, MuseumMap.class);
                if (instance != null) {
                    initializeNodes();
                    convertCoordinate(context);
                }

            } catch (IOException e) {
                Log.e("JsonHelper", Log.getStackTraceString(e));
            }

        }
        return instance;
    }

    private static void convertCoordinate(Context context)
    {
        List<Node> nodeList = new ArrayList<>();
        nodeList.addAll(instance.pointOfInterests);
        nodeList.addAll(instance.labelledPoints);

        for(Node currentP : nodeList)
        {
            int floorId = currentP.getFloorID();
            BitmapFactory.Options options = Resource.getFloorImageDimensionOptions(floorId, instance.floorPlans, context);

            BitmapDescriptor imageFloor = BitmapDescriptorFactory.fromResource(Resource.getFloorPlanResourceID(floorId, instance.floorPlans, context));
            GroundOverlayOptions customMap = new GroundOverlayOptions()
                    .image(imageFloor)
                    .position(new LatLng(0,0), options.outWidth*3, options.outHeight*3).anchor(0, 1)
                    .zIndex(0);


            FloorPlan floorPlan = Resource.searchFloorPlanById(floorId,instance.floorPlans);
            CoordinateAdapter coordinateAdapter = new CoordinateAdapter(floorPlan, customMap.getBounds());
            currentP.setY(coordinateAdapter.convertY(currentP));
            currentP.setX(coordinateAdapter.convertX(currentP));
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

}
