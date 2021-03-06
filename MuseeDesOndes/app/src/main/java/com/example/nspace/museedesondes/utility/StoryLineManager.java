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

/**
 * Created by sebastian on 2016-02-24.
 */

//class used to handle storyline progression and interaction with the beacons

public class StoryLineManager {

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
    private boolean newTourSelected;
    private boolean endTour;
    private static final int DEFAULT_ZINDEX = 0;
    private static final int PRIORITY_ZINDEX = 1;

    public StoryLineManager(StoryLine storyLine, MapActivity mapActivity) {
        this.storyLine = storyLine;
        this.visitedPOIList = new ArrayList<>();
        initPOIList();
        this.pointOfInterestIndex = 0;
        nextPOI = pointOfInterestList.get(pointOfInterestIndex);
        this.mapActivity = mapActivity;
        this.poiBeaconListeners= new ArrayList<>();
        region = new Region("ranged region", null, null, null);
        beaconManager = new BeaconManager(mapActivity);
        setBeaconRangeListener();
    }

    /**
     * scans for beacon info of the next point of interest in chosen storyline
     * and updates line and observers after discovery. also when user chooses to start
     * a new tour the method scans for the initial node and when discovered returns
     * to the storyline activity
     */
    private void setBeaconRangeListener() {
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    String beaconUUID = nearestBeacon.getProximityUUID().toString();
                    if (beaconUUID.equalsIgnoreCase(nextPOI.getBeaconInformation().getUUID())
                            && (nearestBeacon.getMajor() == nextPOI.getBeaconInformation().getMajor())
                            && (nearestBeacon.getMinor() == nextPOI.getBeaconInformation().getMinor())
                            && ((Utils.computeProximity(nearestBeacon)) == Utils.Proximity.NEAR)) {
                        if (newTourSelected) {
                            mapActivity.finish();
                        } else if (!endTour){
                            notifyObservers(nextPOI, storyLine);
                            visitedPOIList.add(nextPOI);
                            updateSegmentListColors();
                            updateNextPOI();
                        }
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
            mapActivity.getNavigationManager().setEndTour();
            endTour = true;
        }
    }

    public void endOfTourDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mapActivity, R.style.AppCompatAlertDialogStyle)
                .setTitle(R.string.endOfTourTitle)
                .setMessage(R.string.endOfTourMsg)
                .setPositiveButton(R.string.endOfTourOption2, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "new tour");
                        newTourSelected = true;
                        mapActivity.getMapManager().clearFloorLines();
                        if (!pointOfInterestList.isEmpty()) {
                            PointOfInterest startNode = pointOfInterestList.get(0);
                            PointOfInterest finalNode = pointOfInterestList.get(pointOfInterestList.size() - 1);
                            mapActivity.getMapManager().displayShortestPath(finalNode.getId(), startNode.getId(), false);
                            nextPOI = pointOfInterestList.get(0);
                        }
                    }
                })
                .setNegativeButton(R.string.endOfTourOption1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "exit");
                        beaconManager.disconnect();
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


        Polyline line = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(node1.getY(),node1.getX() ), new LatLng(node2.getY(),node2.getX()))
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
        /**
         * update previous current segment color to the explored color of transparent red and
         * resets the zIndex to the default height to remove any priority over the others
         */
        if(pointOfInterestIndex - 1 >= 0) {
            List<Polyline> exploredSegment = segmentList.get(pointOfInterestIndex - 1);
            for(Polyline line : exploredSegment) {
                line.setColor(ContextCompat.getColor(mapActivity, R.color.rca_explored_segment));
                line.setZIndex(DEFAULT_ZINDEX);
            }
        }

        /**
         * update color for new current segment to dark red, also sets it to be displayed
         * above any unexplored or explored segments in cases where the same path is reused.
         * this is done using a greater polyline zIndex to give priority over the others
         */
        if(pointOfInterestIndex < pointOfInterestList.size() - 1) {
            List<Polyline> currentSegment = segmentList.get(pointOfInterestIndex);
            for(Polyline line : currentSegment) {
                line.setColor(ContextCompat.getColor(mapActivity, R.color.rca_current_segment));
                line.setZIndex(PRIORITY_ZINDEX);
            }
        }
    }

    public boolean hasVisitedPOI(PointOfInterest pointOfInterest) {
        return visitedPOIList.contains(pointOfInterest);
    }
}
