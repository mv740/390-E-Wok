package com.example.nspace.museedesondes.utility;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.nspace.museedesondes.MapActivity;
import com.example.nspace.museedesondes.R;
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

    public StoryLineManager(StoryLine storyLine, MapActivity mapActivity, PoiPanel panel) {
        this.storyLine = storyLine;
        initializePOIList();
        this.pointOfInterestIndex = 0;
        nextPOI = pointOfInterestList.get(pointOfInterestIndex);
        pointOfInterestIndex++;
        this.mapActivity = mapActivity;
        this.panel = panel;
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

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }


    /***
     * STORYLINE POLYLINE METHODS
     * for these methods I have broken the storyline path through the museum into separate 'segments'
     * each segment is a section of the path between two POIs and can be colored independently
     ***/

    private Polyline getLineFromNodes(Node node1, Node node2) {
        List<LatLng> latLngList = new ArrayList<>();
        latLngList.add(new LatLng(node1.getX(),node1.getY()));
        latLngList.add(new LatLng(node2.getX(),node2.getY()));

        Polyline line = googleMap.addPolyline(new PolylineOptions().width(10));
        line.setVisible(false);
        return line;
    }

    public void initializeSegmentList() {
        ArrayList<Polyline> segment = new ArrayList<>();
        List<Node> nodeList = storyLine.getNodes();
        segmentList = new  ArrayList<>();
        Polyline line;

        for(int i = 0; i < nodeList.size() - 1; i++) {
            if(nodeList.get(i).getFloorID() == nodeList.get(i + 1).getFloorID()) {
                line = getLineFromNodes(nodeList.get(i),nodeList.get(i + 1));
                if(nodeList.get(i) instanceof PointOfInterest) {
                    segmentList.add(segment);
                    segment = new ArrayList<>();
                }
                segment.add(line);
            }
        }
    }

    public void initializSegmentColors() {
//        int segmentColor = ContextCompat.getColor(mapActivity, R.color.rca_current_segment);
//        int segmentColor = ContextCompat.getColor(mapActivity, R.color.rca_unexplored_segment);

    }

    public void initializeFloorLineMap(HashMap<Integer,ArrayList<Polyline>> floorLineMap){
        this.floorLineMap = floorLineMap;
        //TODO iterate through segment list to init
    }

    private void updateSegmentList(){
        //TODO:
        //change colors
        //redraw current floor?
    }

}
