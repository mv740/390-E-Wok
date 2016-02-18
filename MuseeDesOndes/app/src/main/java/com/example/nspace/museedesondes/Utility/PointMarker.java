package com.example.nspace.museedesondes.Utility;

import android.content.Context;
import com.example.nspace.museedesondes.Model.PointOfInterest;
import com.example.nspace.museedesondes.Model.Text;
import com.example.nspace.museedesondes.Model.TransitionPoint;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by michal on 2/15/2016.
 */
public class PointMarker {


    /**
     * Create a point of interest marker and drop it on the map on the specified coordinate
     *
     * @param pointOfInterest
     * @param context
     * @param googleMap MapPlan
     */
    public static void singleInterestPointFactory(PointOfInterest pointOfInterest, Context context, GoogleMap googleMap) {

        String title = "error";
        for (Text text : pointOfInterest.getText()) {
            if (context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(text.getLanguage().name())) {
                title = text.getLanguage().name();
            }
        }

        MarkerOptions node = new MarkerOptions();
        node.position(new LatLng(pointOfInterest.getX(), pointOfInterest.getY()));
        node.title(title);
        node.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        googleMap.addMarker(node);
    }

    public static void singleTransitionPointFactory(TransitionPoint transitionPoint, GoogleMap googleMap) {
        String title = transitionPoint.getLabel().name();
        MarkerOptions node = new MarkerOptions();
        node.position(new LatLng(transitionPoint.getX(), transitionPoint.getY()));
        node.title(title);

        switch (transitionPoint.getLabel()) {
            case NONE:
                node.visible(false); // hide point of transition that are between node of interest
                break;
            case EMERGENCY_EXIT:
                node.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                break;
            case ENTRANCE:
                node.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                break;
            case EXIT:
                node.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                break;
            case ELEVATOR:
                node.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                break;
            case RAMP:
                node.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                break;
            case STAIRS:
                node.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                break;
            case WASHROOM:
                node.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                break;
        }
        googleMap.addMarker(node);
    }
}
