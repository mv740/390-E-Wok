package com.example.nspace.museedesondes.adapters;

import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by michal on 3/12/2016.
 *
 * adapt coordinate (x,y) from json to our google map latitude/longitude
 * TODO (x,y) value could be mapped to a smaller image, so we will need to compare x to the width of original plan and adapt it
 */
public class CoordinateAdapter {

    private LatLngBounds floorPlanBounds;

    public CoordinateAdapter(LatLngBounds floorPlanBounds)
    {
        this.floorPlanBounds = floorPlanBounds;
    }

    public double convertX(double x)
    {

        //longitude : west-est
         return x;
    }
    public double convertY(double y)
    {
        //latitude : north-south
        return y;
    }

}
