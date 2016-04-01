package com.example.nspace.museedesondes.adapters;

import android.util.Log;

import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.Node;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by michal on 3/12/2016.
 *
 * adapt coordinate (x,y) from json to our google map latitude/longitude
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

    /**
     * longitude : west-est
     * @param node
     * @return
     */
    public double convertX(Node node)
    {

        double ratio =   node.getX()/floorPlan.getImageWidth();
        return ratio*width;

    }

    /**
     * latitude : north-south
     *
     * value is negative because top left of the screen is (0,0)
     * e
     * @param node
     * @return
     */
    public double convertY(Node node)
    {
        double ratio = node.getY()/ floorPlan.getImageHeight();
        return -ratio*height;

    }

}
