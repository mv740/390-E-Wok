package com.example.nspace.museedesondes.adapters;

import android.util.Log;

import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.Node;
import com.example.nspace.museedesondes.utility.MapManager;

/**
 * Created by michal on 3/12/2016.
 *
 * adapt coordinate (x,y) from json to our google map latitude/longitude
 * TODO (x,y) value could be mapped to a smaller image, so we will need to compare x to the width of original plan and adapt it
 */
public class CoordinateAdapter {

    private double width;
    private double height;
    private MapManager mapManager;

    public CoordinateAdapter(MapManager mapManager)
    {
        this.mapManager = mapManager;
        width = this.mapManager.getGroundOverlayFloorMapBound().southwest.longitude - this.mapManager.getGroundOverlayFloorMapBound().northeast.latitude;
        height = this.mapManager.getGroundOverlayFloorMapBound().southwest.latitude - this.mapManager.getGroundOverlayFloorMapBound().northeast.longitude;
        Log.e("CoordinateAdapter", String.valueOf(width));
        Log.e("CoordinateAdapter", String.valueOf(height));
    }

    public double convertX(Node node)
    {

        FloorPlan current =  this.mapManager.getFloor(node.getFloorID());
        double ratio =   current.getImageWidth()/width;

        //todo test this

        //longitude : west-est
         return node.getX();
    }
    public double convertY(Node node)
    {
        FloorPlan current = this.mapManager.getFloor(node.getFloorID());
        double ratio = current.getImageHeight()/ height;

        //todo test this

        //latitude : north-south
        return node.getY();
    }

}
