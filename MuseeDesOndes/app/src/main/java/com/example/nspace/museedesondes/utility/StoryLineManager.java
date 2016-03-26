package com.example.nspace.museedesondes.utility;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.nspace.museedesondes.MapActivity;
import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.adapters.CoordinateAdapter;
import com.example.nspace.museedesondes.model.Node;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.example.nspace.museedesondes.model.StoryLine;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by sebastian on 2016-02-24.
 */

//class used to handle storyline progression and interaction with the beacons

public class StoryLineManager {

    private static final String DEFAULT_MUSEUM_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";

    private StoryLine storyLine;
    private List<PointOfInterest> pointOfInterestList;
    private List<PointOfInterest> visitedPOIList;
    private List<List<Polyline>> segmentList;
    private Map<Integer, List<Polyline>> floorLineMap;
    private List<POIBeaconListener> poiBeaconListeners;
    private int pointOfInterestIndex;
    private PointOfInterest nextPOI;
    private MapActivity mapActivity;
    private GoogleMap googleMap;
    private BeaconManager beaconManager;
    private Region region;

    public StoryLineManager(StoryLine storyLine, MapActivity mapActivity) {
        this.storyLine = storyLine;
        this.visitedPOIList = new ArrayList<>();
        initPOIList();
        this.pointOfInterestIndex = 0;
        nextPOI = pointOfInterestList.get(pointOfInterestIndex);
        this.mapActivity = mapActivity;
        this.poiBeaconListeners= new ArrayList<>();
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

                        notifyObservers(nextPOI,storyLine);
                        visitedPOIList.add(nextPOI);
                        updateSegmentListColors();
                        updateNextPOI();
                    }
                }
            }
        });
    }

    public void registerObserver(POIBeaconListener observer) {
        poiBeaconListeners.add(observer);
    }

   public void notifyObservers(PointOfInterest node, StoryLine storyLine) {
       for(POIBeaconListener observer : poiBeaconListeners) {
           observer.onPOIBeaconDiscovered(node,storyLine);
       }
   }

    private void initPOIList() {
        pointOfInterestList = new ArrayList<PointOfInterest>();
        for(Node node : storyLine.getNodes()) {
            if(node instanceof PointOfInterest) {
                pointOfInterestList.add((PointOfInterest) node);
            }
        }
    }

    //updates the next point of interest beacon to listen for, stops listening after the last beacon is discovered
    private void updateNextPOI() {
        pointOfInterestIndex++;
        if(pointOfInterestIndex < pointOfInterestList.size()){
            nextPOI = pointOfInterestList.get(pointOfInterestIndex);
        } else {
            beaconManager.stopRanging(region);
            mapActivity.getNavigationManager().setEndTour();
        }
    }

    public void endOfTourDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mapActivity, R.style.AppCompatAlertDialogStyle)
                .setTitle(R.string.endOfTourTitle)
                .setMessage(R.string.endOfTourMsg)
                .setPositiveButton(R.string.endOfTourOption2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "new tour");
                    }
                })
                .setNegativeButton(R.string.endOfTourOption1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "exit");
                        mapActivity.setSearchingExit(true);
                        mapActivity.setNavigationMode(true);
                        mapActivity.getNavigationManager().startNavigationMode(mapActivity.getPanelManager());

                    }
                });
        builder.show();
    }

    public BeaconManager getBeaconManager() {
        return beaconManager;
    }

    public Region getRegion() {
        return region;
    }

    public List<PointOfInterest> getPointOfInterestList() {
        return pointOfInterestList;
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

        CoordinateAdapter coordinateAdapter = new CoordinateAdapter(mapActivity.getMapManager());

        Polyline line = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(coordinateAdapter.convertY(node1),coordinateAdapter.convertX(node1) ), new LatLng(coordinateAdapter.convertY(node2),coordinateAdapter.convertX(node2)))
                        .color(ContextCompat.getColor(mapActivity, R.color.rca_unexplored_segment))
                        .width(10));
        line.setVisible(false);
        return line;
    }

    public void initSegmentListAndFloorLineMap() {
        List<Polyline> segment = new ArrayList<>();
        List<Node> nodeList = storyLine.getNodes();
        segmentList = new ArrayList<>();
        Polyline line;

        for(int i = 0; i < nodeList.size() - 1; i++) {
            if(nodeList.get(i).getFloorID() == nodeList.get(i + 1).getFloorID()) {
                line = getLineFromNodes(nodeList.get(i),nodeList.get(i + 1));
                if((nodeList.get(i) instanceof PointOfInterest) && (i != 0)) {
                    segmentList.add(segment);
                    segment = new ArrayList<>();
                }
                segment.add(line);
                floorLineMap.get(nodeList.get(i).getFloorID()).add(line);
            }
        }
        segmentList.add(segment);
    }

    public void setFloorLineMap(java.util.Map<Integer, List<Polyline>> floorLineMap){
        this.floorLineMap = floorLineMap;
    }

    private void updateSegmentListColors(){

        //update color for new current segment
        if(pointOfInterestIndex < pointOfInterestList.size() - 1) {
            List<Polyline> currentSegment = segmentList.get(pointOfInterestIndex);
            for(Polyline line : currentSegment) {
                line.setColor(ContextCompat.getColor(mapActivity, R.color.rca_current_segment));
            }
        }

        //update previous current segment color to the explored color
        if(pointOfInterestIndex - 1 >= 0) {
            List<Polyline> exploredSegment = segmentList.get(pointOfInterestIndex - 1);
            for(Polyline line : exploredSegment) {
                line.setColor(ContextCompat.getColor(mapActivity, R.color.rca_explored_segment));
            }
        }
    }

    public boolean hasVisitedPOI(PointOfInterest pointOfInterest) {
        return visitedPOIList.contains(pointOfInterest);
    }
}
