package com.example.nspace.museedesondes.Utility;

import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.nspace.museedesondes.MapActivity;
import com.example.nspace.museedesondes.Model.BeaconInformation;
import com.example.nspace.museedesondes.Model.Node;
import com.example.nspace.museedesondes.Model.PointOfInterest;
import com.example.nspace.museedesondes.Model.StoryLine;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sebastian on 2016-02-24.
 */

//class used to handle storyline progression and interaction with the beacons

public class StoryLineManager {

    private final String DEFAULT_MUSEUM_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";

    private StoryLine storyLine;
    private ArrayList<PointOfInterest> POIList;
    private int POIindex;
    private PointOfInterest nextPOI;
    private MapActivity mapActivity;
    private GoogleMap googleMap;
    private BeaconManager beaconManager;
    private Region region;

    public StoryLineManager(StoryLine storyLine, MapActivity mapActivity, GoogleMap googleMap) {
        this.storyLine = storyLine;
        initializePOIList();
        this.POIindex = 0;
        nextPOI = POIList.get(POIindex);
        POIindex++;
        this.mapActivity = mapActivity;
        this.googleMap = googleMap;
        region = new Region("ranged region", UUID.fromString(DEFAULT_MUSEUM_UUID), null, null);
        beaconManager = new BeaconManager(mapActivity);
        setBeaconRangeListener();
    }

    public void setBeaconRangeListener() {
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    if((nearestBeacon.getMajor() == nextPOI.getBeaconInformation().getMajor())
                            &&(nearestBeacon.getMinor() == nextPOI.getBeaconInformation().getMinor())
                            &&((Utils.computeProximity(nearestBeacon)) == Utils.Proximity.NEAR)) {
                        // TODO: update/popup the POI panel
                        // TODO: update UI with temp man marker
                        // TODO: update storyline polyline segments

                        //updates the next point of interest beacon to listen for and stops listening after the last nodes beacon is discovered
                        if(POIindex < POIList.size()){
                            nextPOI = POIList.get(POIindex);
                            POIindex++;
                        } else {
                            beaconManager.stopRanging(region);
                        }
                    }
                }
            }
        });
    }

    public void initializePOIList() {
        POIList = new ArrayList<PointOfInterest>();
        for(Node node : storyLine.getNodes()) {
            if(node instanceof PointOfInterest) {
                POIList.add((PointOfInterest) node);
            }
        }
    }

    //TODO:
    public void updatePOIPanel(){

    }

    public void updateManMarker() {

    }

    public void updateStorylinePolyline(){

    }


    public BeaconManager getBeaconManager() {
        return beaconManager;
    }

    public Region getRegion() {
        return region;
    }

}
