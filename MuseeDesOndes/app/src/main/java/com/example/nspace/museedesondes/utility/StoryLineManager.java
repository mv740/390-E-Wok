package com.example.nspace.museedesondes.utility;

import android.graphics.Color;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.nspace.museedesondes.MapActivity;
import com.example.nspace.museedesondes.model.Node;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.example.nspace.museedesondes.model.StoryLine;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sebastian on 2016-02-24.
 */

//class used to handle storyline progression and interaction with the beacons

public class StoryLineManager {

    private static final String DEFAULT_MUSEUM_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";;

    private StoryLine storyLine;
    private List<PointOfInterest> pointOfInterestList;
    private ArrayList<ArrayList<Polyline>> segmentList;
    private Map<Integer, ArrayList<Polyline>> floorLineMap;
    private int pointOfInterestIndex;
    private PointOfInterest nextPOI;
    private MapActivity mapActivity;
    private GoogleMap googleMap;
    private BeaconManager beaconManager;
    private Region region;
    private PoiPanel panel;

    public StoryLineManager(StoryLine storyLine, MapActivity mapActivity, PoiPanel panel, GoogleMap googleMap) {
        this.storyLine = storyLine;
        initializePOIList();
        this.pointOfInterestIndex = 0;
        nextPOI = pointOfInterestList.get(pointOfInterestIndex);
        pointOfInterestIndex++;
        this.mapActivity = mapActivity;
        this.panel = panel;
        this.googleMap = googleMap;
        region = new Region("ranged region", UUID.fromString(DEFAULT_MUSEUM_UUID), null, null);
        beaconManager = new BeaconManager(mapActivity);
        setBeaconRangeListener();
    }

    private void setBeaconRangeListener() {
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    if ((nearestBeacon.getMajor() == nextPOI.getBeaconInformation().getMajor())
                            && (nearestBeacon.getMinor() == nextPOI.getBeaconInformation().getMinor())
                            && ((Utils.computeProximity(nearestBeacon)) == Utils.Proximity.NEAR)) {

                        panel.updateStoryPanel(storyLine, nextPOI);
                        // TODO: update UI with temp man marker
                        // TODO: update storyline polyline segments
                        updateNextPOI();
                    }
                }
            }
        });
    }

    private void initializePOIList() {
        pointOfInterestList = new ArrayList<PointOfInterest>();
        for(Node node : storyLine.getNodes()) {
            if(node instanceof PointOfInterest) {
                pointOfInterestList.add((PointOfInterest) node);
            }
        }
    }

    private void updateManMarker() {

    }

    //updates the next point of interest beacon to listen for, stops listening after the last beacon is discovered
    private void updateNextPOI() {
        if(pointOfInterestIndex < pointOfInterestList.size()){
            nextPOI = pointOfInterestList.get(pointOfInterestIndex);
            pointOfInterestIndex++;
        } else {
            beaconManager.stopRanging(region);
        }
    }

    public BeaconManager getBeaconManager() {
        return beaconManager;
    }

    public Region getRegion() {
        return region;
    }

    /*** STORYLINE POLYLINE METHODS ***/

    private Polyline getLineFromNodes(Node node1, Node node2) {
        List<LatLng> latLngList = new ArrayList<>();
        latLngList.add(new LatLng(node1.getX(),node1.getY()));
        latLngList.add(new LatLng(node2.getX(),node2.getY()));

        Polyline line = googleMap.addPolyline(new PolylineOptions().width(10));
        line.setVisible(false);
        return line;
    }

    private void initializeSegmentList() {
        ArrayList<Polyline> segment = new ArrayList<>();
        List<Node> nodeList = storyLine.getNodes();
        segmentList = new  ArrayList<>();
        Polyline line;

        for(int i = 0; i < nodeList.size() - 1; i++) {
            line = getLineFromNodes(nodeList.get(i),nodeList.get(i + 1));
            if(nodeList.get(i) instanceof PointOfInterest) {
                segmentList.add(segment);
                segment = new ArrayList<>();
            }
            segment.add(line);
        }
    }

    private void initializSegmentColors() {

    }

    private void initializeFloorLineMap(){
        floorLineMap = new HashMap<>();
    }

    private void updateSegmentList(){

    }

}
