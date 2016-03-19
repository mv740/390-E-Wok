package com.example.nspace.museedesondes.utility;

import com.example.nspace.museedesondes.model.PointOfInterest;
import com.example.nspace.museedesondes.model.StoryLine;

/**
 * Created by sebastian on 2016-03-18.
 */
public interface POIBeaconListener {
    void onPOIBeaconDiscovered(PointOfInterest node, StoryLine storyLine);
}
