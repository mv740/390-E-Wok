package com.example.nspace.museedesondes.utility;

import android.content.Context;

import com.example.nspace.museedesondes.model.LabelledPoint;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by michal on 2/15/2016.
 */
public class PointMarker {


    /**
     * Create a point of interest marker and drop it on the map on the specified coordinate
     * @param pointOfInterest
     * @param context
     * @param googleMap MapPlan
     * @param groundOverlayFloorMapBound
     */
    public static Marker singleInterestPointFactory(PointOfInterest pointOfInterest, Context context, GoogleMap googleMap, LatLngBounds groundOverlayFloorMapBound) {

        String title = "error";
        title = pointOfInterest.getLocaleDescription(context).getTitle();
        CoordinateAdapter coordinateAdapter = new CoordinateAdapter(groundOverlayFloorMapBound);

        MarkerOptions node = new MarkerOptions();
        node.position(new LatLng(coordinateAdapter.convertY(pointOfInterest.getX()),coordinateAdapter.convertX(pointOfInterest.getY())));
        node.title(title);
        node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        //storing information in marker snippet
        String floorID = String.valueOf(pointOfInterest.getFloorID());
        String nodeID = String.valueOf(pointOfInterest.getId());
        String data = floorID+"/"+nodeID;
        node.snippet(data);

        Marker createdMarker = googleMap.addMarker(node);

        return createdMarker;
    }

    // used to easily extract information from a string
    public static class Information{
        private int floorID;
        private int nodeID;

        public Information(String data)
        {
            String[] tokens = data.split("/");
            floorID = Integer.parseInt(tokens[0]);
            nodeID = Integer.parseInt(tokens[1]);
        }

        public int getFloorID() {
            return floorID;
        }

        public int getNodeID() {
            return nodeID;
        }
    }

    public static void singleTransitionPointFactory(LabelledPoint labelledPoint, GoogleMap googleMap) {
        String title = labelledPoint.getLabel().name();
        MarkerOptions node = new MarkerOptions();
        node.position(new LatLng(labelledPoint.getX(), labelledPoint.getY()));
        node.title(title);

        switch (labelledPoint.getLabel()) {
            case NONE:
                node.visible(false); // hide point of transition that are between node of interest
                break;
            case EMERGENCY_EXIT:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                break;
            case ENTRANCE:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                break;
            case EXIT:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                break;
            case ELEVATOR:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                break;
            case RAMP:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                break;
            case STAIRS:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                break;
            case WASHROOM:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                break;
        }
        googleMap.addMarker(node);
    }
}
