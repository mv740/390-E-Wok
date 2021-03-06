package com.example.nspace.museedesondes.adapters;

import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.Node;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by michal on 3/12/2016.
 * <p/>
 * adapt coordinate (x,y) from json to our google map latitude/longitude
 */
public class CoordinateAdapter {

    private double width;
    private double height;
    private FloorPlan floorPlan;

    public CoordinateAdapter(FloorPlan floorPlan, LatLngBounds bounds) {
        this.width = Math.abs(bounds.northeast.longitude - bounds.southwest.longitude);
        this.height = Math.abs(bounds.southwest.latitude - bounds.northeast.latitude);
        this.floorPlan = floorPlan;
    }

    /**
     * longitude : west-est
     *
     * @param node
     * @return
     */
    public double convertX(Node node) {
        double ratio = node.getX() / floorPlan.getImageWidth();
        return ratio * width;
    }

    /**
     * latitude : north-south
     * <p/>
     * value is negative because top left of the screen is (0,0)
     * e
     *
     * @param node
     * @return
     */
    public double convertY(Node node) {
        double ratio = node.getY() / floorPlan.getImageHeight();
        return -ratio * height;
    }
}
