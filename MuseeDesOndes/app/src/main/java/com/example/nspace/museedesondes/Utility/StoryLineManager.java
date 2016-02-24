package com.example.nspace.museedesondes.Utility;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.nspace.museedesondes.MapActivity;
import com.example.nspace.museedesondes.Model.PointOfInterest;
import com.example.nspace.museedesondes.Model.StoryLine;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;
import java.util.UUID;

/**
 * Created by sebastian on 2016-02-24.
 */

//class used to handle storyline progression and interaction with the beacons
//TODO: Map Activity onResume/onPause start ranging, tie into map activity

public class StoryLineManager {

    private final String DEFAULT_MUSEUM_UUID = "b9407f30-f5f8-466e-aff9-25556b57fe6d";

    private StoryLine storyline;
    private PointOfInterest nextPOI;
    private MapActivity mapActivity;
    private GoogleMap googleMap;
    private BeaconManager beaconManager;
    private Region region;

    public StoryLineManager(StoryLine storyLine, MapActivity mapActivity, GoogleMap googleMap) {
        this.storyline = storyLine;
        //TODO: how to initialize first node?
        //this.nextPOI = (PointOfInterest) storyline.getNodes().get(0);
        this.mapActivity = mapActivity;
        this.googleMap = googleMap;
        region = new Region("ranged region", UUID.fromString(DEFAULT_MUSEUM_UUID), null, null);
        beaconManager = new BeaconManager(mapActivity);
        setBeaconListener();
    }

    public void setBeaconListener() {
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
                        // TODO: update nextPOI

                    }
                    //Log.d("Test", "Nearest beacon " + testValue);
                }
            }
        });
    }

    public BeaconManager getBeaconManager() {
        return beaconManager;
    }
}
