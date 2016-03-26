package com.example.nspace.museedesondes.adapters;

import android.util.Log;

import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.Node;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by michal on 3/12/2016.
 *
 * adapt coordinate (x,y) from json to our google map latitude/longitude
 * TODO (x,y) value could be mapped to a smaller image, so we will need to compare x to the width of original plan and adapt it
 */
public class CoordinateAdapter {

    private double width;
    private double height;
    private FloorPlan floorPlan;

    public CoordinateAdapter(FloorPlan floorPlan, LatLngBounds bounds)
    {
        this.width = Math.abs(bounds.southwest.latitude - bounds.northeast.latitude);
        this.height = Math.abs(bounds.southwest.longitude - bounds.northeast.longitude);
        this.floorPlan = floorPlan;
        Log.e("CoordinateAdapter", "width: "+String.valueOf(width));
        Log.e("CoordinateAdapter", "height: "+String.valueOf(height));
    }

    public double convertX(Node node)
    {

        double ratio =   node.getX()/floorPlan.getImageWidth();
        double newX = ratio*width;


        //todo test this

        //longitude : west-est
         return node.getX();
    }
    public double convertY(Node node)
    {
        double ratio = node.getY()/ floorPlan.getImageHeight();
        double newY = ratio*height;

        //todo test this

        //latitude : north-south
        return node.getY();
    }

}
