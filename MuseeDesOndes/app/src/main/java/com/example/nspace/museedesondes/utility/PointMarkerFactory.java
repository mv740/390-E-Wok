package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.util.Log;

import com.example.nspace.museedesondes.adapters.CoordinateAdapter;
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
public class PointMarkerFactory {


    /**
     * Create a point of interest marker and drop it on the map on the specified coordinate
     * @param pointOfInterest
     * @param context
     * @param googleMap MapPlan
     * @param groundOverlayFloorMapBound
     */
    public static Marker singleInterestPointFactory(PointOfInterest pointOfInterest, Context context, GoogleMap googleMap, LatLngBounds groundOverlayFloorMapBound) {

        CoordinateAdapter coordinateAdapter = new CoordinateAdapter(groundOverlayFloorMapBound);

        MarkerOptions node = new MarkerOptions();
        node.position(new LatLng(coordinateAdapter.convertY(pointOfInterest.getX()),coordinateAdapter.convertX(pointOfInterest.getY()))); //TODO: convert check getX, getY and coordinate adapter are mismatched
        node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        Marker createdMarker = googleMap.addMarker(node);
        createdMarker.setVisible(false);

        return createdMarker;
    }

    public static Marker singleTransitionPointFactory(LabelledPoint labelledPoint, GoogleMap googleMap, LatLngBounds groundOverlayFloorMapBound) {
        CoordinateAdapter coordinateAdapter = new CoordinateAdapter(groundOverlayFloorMapBound);

        String title = labelledPoint.getLabel().name();
        MarkerOptions node = new MarkerOptions();
        node.position(new LatLng(coordinateAdapter.convertY(labelledPoint.getX()), coordinateAdapter.convertX(labelledPoint.getY())));  //TODO: convert check getX, getY and coordinate adapter are mismatched
        node.title(title);

        switch (labelledPoint.getLabel()) {
            case EMERGENCY_EXIT:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                break;
            case ENTRANCE:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                break;
            case EXIT:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                break;
            case ELEVATOR:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                break;
            case RAMP:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                break;
            case STAIRS:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                break;
            case WASHROOM:
                node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                break;
            default:
                Log.e("PointMarkerFactory", "label invalid " + labelledPoint.getLabel());
        }

        Marker createdMarker = googleMap.addMarker(node);
        createdMarker.setVisible(false);

        return createdMarker;
    }
}
