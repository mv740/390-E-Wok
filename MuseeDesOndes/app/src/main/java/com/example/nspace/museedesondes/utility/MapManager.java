package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michal on 2/10/2016.
 */
public class MapManager {

    private GoogleMap mMap;
    private Context context;
    private static final int ZOOM_MAX = 15;
    private static final int ZOOM_MIN = 13;

    private LatLngBounds groundOverlayBound;

    public MapManager(GoogleMap googleMap, Context context)
    {
        this.mMap = googleMap;
        this.context = context;
    }




    public GroundOverlay loadDefaultFloor(GoogleMap googleMap, LatLng position, List<FloorPlan> floorPlans, View view)
    {
        Resources resources = context.getResources();
        final int resourceID = resources.getIdentifier(floorPlans.get(0).getImagePath(), "drawable", context.getPackageName()); // 0 = floor 1
        FloatingActionMenu floorButton = (FloatingActionMenu) view.findViewById(R.id.floor_button);
        //turn off button rotation on click
        floorButton.setIconAnimated(false);
        FloatingActionButton floorSelected =  (FloatingActionButton)view.findViewById(R.id.fab1);
        floorSelected.setColorNormal(ContextCompat.getColor(context, R.color.rca_primary));
        FloatingActionButton floor5 =  (FloatingActionButton)view.findViewById(R.id.fab5);
        FloatingActionButton floor3 =  (FloatingActionButton)view.findViewById(R.id.fab3);
        FloatingActionButton floor4 =  (FloatingActionButton)view.findViewById(R.id.fab4);
        FloatingActionButton floor2 =  (FloatingActionButton)view.findViewById(R.id.fab2);
        floor5.setColorNormal(ContextCompat.getColor(context, R.color.rca_onclick));
        floor2.setColorNormal(ContextCompat.getColor(context, R.color.rca_onclick));
        floor3.setColorNormal(ContextCompat.getColor(context, R.color.rca_onclick));
        floor4.setColorNormal(ContextCompat.getColor(context, R.color.rca_onclick));

        BitmapDescriptor image = BitmapDescriptorFactory.fromResource(resourceID);


        GroundOverlayOptions customMap = new GroundOverlayOptions()
                .image(image)
                .position(position, 5520f, 10704f).anchor(0, 0);

        GroundOverlay groundOverlay = googleMap.addGroundOverlay(customMap);

        groundOverlayBound = groundOverlay.getBounds();

        return groundOverlay;

    }

    /**
     * @param groundOverlay pass the same groundOverlay from defaultFloorMethod; this keep all image customisation
     * @param floorID floor number
     * @param floorPlans
     * @param markerList
     * @param polylineList
     */
    public void switchFloor(GroundOverlay groundOverlay, int floorID, List<FloorPlan> floorPlans, List<Marker> markerList, Map<String, Polyline> polylineList)
    {
        //http://stackoverflow.com/questions/16369814/how-to-access-the-drawable-resources-by-name-in-android
        int index = floorID-1; //Todo if floor object aren't in order then we will need to loop to find the correct one by id

        Log.d("markerList", String.valueOf(markerList.size()));

        displayCurrentFloorPointOfInterest(floorID, markerList);

        //todo For testing only, hashmap will later be  {"storyline id-floor","polyline object"}
        // method .remove() delete the polyline
        if(floorID != 1)
        {
            polylineList.get("hello").setVisible(false);
        }else {
            polylineList.get("hello").setVisible(true);
        }

        Resources resources = context.getResources();
        final int resourceID = resources.getIdentifier(floorPlans.get(index).getImagePath(), "drawable", context.getPackageName());
        groundOverlay.setImage(BitmapDescriptorFactory.fromResource(resourceID));

        groundOverlayBound = groundOverlay.getBounds();

    }

    /**
     * Show only marker that are meant for the current floor
     *
     * @param floorID
     * @param markerList
     */
    public void displayCurrentFloorPointOfInterest(int floorID, List<Marker> markerList) {
        for(Marker marker : markerList)
        {
            PointMarker.Information pMarkerInfo = new PointMarker.Information(marker.getSnippet());

            if((pMarkerInfo.getFloorID()) == floorID){
                marker.setVisible(true);
            }else
            {
                marker.setVisible(false);
            }
        }
    }

    /**
     * android Zoom-to-Fit All Markers on Google Map
     */
    public void zoomToFit(List<Marker> markerList) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markerList) {
            if(marker.isVisible())
            {
                builder.include(marker.getPosition());
            }
        }
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    /**
     * limit the max and min zoom
     *
     * @param position
     */
    public void zoomLimit(CameraPosition position) {
        if (position.zoom > ZOOM_MAX)
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        if(position.zoom <12.8)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13.1f));
        }
    }


    /**
     * Every time a camera view change, it will verify its position to determine if it is out of bound.
     * @param current
     */
    public void verifyCameraBounds(LatLngBounds current)
    {
        if(!current.contains(groundOverlayBound.getCenter()))
        {
            //if your zoomed in, then it check if you are inside the overlay map
            if(!groundOverlayBound.contains(current.getCenter())){
                mMap.animateCamera(CameraUpdateFactory.newLatLng(groundOverlayBound.getCenter()));
                Log.d("verifyCameraBounds","YES, view out of bound");
            }
        }
        else {
            Log.d("verifyCameraBounds","NO, view is correct");
        }
    }


    /**
     * zooms in on the map by moving the viewpoint's height closer
     *
     * @param position
     */
    public void zoomIn(CameraPosition position)
    {
        if(position.zoom <ZOOM_MAX)
        {
            float newZoom = position.zoom + 0.5f;
            if(newZoom <= ZOOM_MAX){
                mMap.animateCamera(CameraUpdateFactory.zoomTo(newZoom));
            }
        }
    }

    /**
     * zooms out on the map by moving the viewpoint's height farther away
     *
     * @param position
     */
    public void zoomOut(CameraPosition position)
    {
       if(position.zoom >ZOOM_MIN)
       {
           float newZoom = position.zoom - 0.5f;
           if(newZoom >= ZOOM_MIN){
               mMap.animateCamera(CameraUpdateFactory.zoomTo(newZoom));
           }
       }
    }

}
