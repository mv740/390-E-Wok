package com.example.nspace.museedesondes.utility;

import android.util.Log;

import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.adapters.CoordinateAdapter;
import com.example.nspace.museedesondes.model.LabelledPoint;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by michal on 2/15/2016.
 */
public class PointMarkerFactory {


    /**
     * Create a point of interest marker and drop it on the map on the specified coordinate
     * @param pointOfInterest
     * @param googleMap MapPlan
     */
    public static Marker singleInterestPointFactory(PointOfInterest pointOfInterest, GoogleMap googleMap, MapManager mapManager) {

        CoordinateAdapter coordinateAdapter = new CoordinateAdapter(mapManager);

        MarkerOptions node = new MarkerOptions();
        node.position(new LatLng(coordinateAdapter.convertY(pointOfInterest),coordinateAdapter.convertX(pointOfInterest))); //TODO: convert check getX, getY and coordinate adapter are mismatched
        node.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        Marker createdMarker = googleMap.addMarker(node);
        createdMarker.setVisible(false);

        return createdMarker;
    }

    public static Marker singleTransitionPointFactory(LabelledPoint labelledPoint, GoogleMap googleMap, MapManager mapManager) {
        CoordinateAdapter coordinateAdapter = new CoordinateAdapter(mapManager);

        String title = labelledPoint.getLabel().name();
        MarkerOptions node = new MarkerOptions();
        node.position(new LatLng(coordinateAdapter.convertY(labelledPoint), coordinateAdapter.convertX(labelledPoint)));  //TODO: convert check getX, getY and coordinate adapter are mismatched
        node.title(title);

        switch (labelledPoint.getLabel()) {
            case EMERGENCY_EXIT:
                node.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_emergency_exit));
                break;
            case ENTRANCE:
                node.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_entrance));
                break;
            case EXIT:
                node.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_exit));
                break;
            case ELEVATOR:
                node.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_elevator));
                break;
            case RAMP:
                node.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_disability));
                break;
            case STAIRS:
                node.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_stairs));
                break;
            case WASHROOM:
                node.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_toilets));
                break;
            default:
                Log.e("PointMarkerFactory", "label invalid " + labelledPoint.getLabel());
        }

        Marker createdMarker = googleMap.addMarker(node);
        createdMarker.setVisible(false);

        return createdMarker;
    }
}
