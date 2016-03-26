package com.example.nspace.museedesondes.adapters;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.Node;

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

    public CoordinateAdapter(BitmapFactory.Options options, FloorPlan floorPlan)
    {
        this.width = options.outWidth;
        this.height = options.outHeight;
        this.floorPlan = floorPlan;
        Log.e("CoordinateAdapter", "width: "+String.valueOf(width));
        Log.e("CoordinateAdapter", "height: "+String.valueOf(height));
    }

    public double convertX(Node node)
    {

        double ratio =   floorPlan.getImageWidth()/width;

        //todo test this

        //longitude : west-est
         return node.getX();
    }
    public double convertY(Node node)
    {
        double ratio = floorPlan.getImageHeight()/ height;

        //todo test this

        //latitude : north-south
        return node.getY();
    }

}
