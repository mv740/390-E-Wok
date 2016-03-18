package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.model.Node;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michal on 2/10/2016.
 */
public class MapManager {

    private static final double ZOOM_MAX = 15.0;
    private static final double ZOOM_MIN = 13.0;
    private final int DEFAULT_FLOOR_ID = 1;
    private GoogleMap mMap;
    private Context context;
    private LatLngBounds groundOverlayFloorMapBound;
    private GroundOverlay groundOverlayFloorMap;
    private int zoomLevel = 1;
    private Map<Integer, ArrayList<Polyline>> floorLineMap;
    private int currentFloorID;
    private boolean freeExploration;
    private boolean zoomToFitUsed = false;
    private boolean pinchZoomUsed = false;


    public MapManager(GoogleMap googleMap, Context context, Map<Integer, ArrayList<Polyline>> floorLineMap, boolean freeExploration) {
        this.mMap = googleMap;
        this.context = context;
        this.floorLineMap = floorLineMap;
        this.freeExploration = freeExploration;
    }


    /**
     * @param floorPlans
     * @param view
     */
    public void loadDefaultFloor(List<FloorPlan> floorPlans, View view) {
        initializeFloatingButtonSettings(view);

        BitmapDescriptor imageFloor = BitmapDescriptorFactory.fromResource(getFloorPlanResourceID(floorPlans, 0));
        LatLng position = new LatLng(0, 0);


        GroundOverlayOptions customMap = new GroundOverlayOptions()
                .image(imageFloor)
                .position(position, 5520f, 10704f).anchor(0, 1)
                .zIndex(0);

        groundOverlayFloorMap = mMap.addGroundOverlay(customMap);
        groundOverlayFloorMapBound = groundOverlayFloorMap.getBounds();

        //add white background under floor map
        GroundOverlayOptions mapBackground = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.white_background))
                .position(groundOverlayFloorMapBound.getCenter(), 20520f, 25704f)
                .zIndex(-1);
        mMap.addGroundOverlay(mapBackground);

        if (!freeExploration) {
            initFloorLines();
        }
    }

    private int getFloorPlanResourceID(List<FloorPlan> floorPlans, int index) {
        return context.getResources().getIdentifier(floorPlans.get(index).getImagePath(), "drawable", context.getPackageName());
    }

    /**
     * set button colors
     * disable button menu animation
     *
     * @param view
     */
    private void initializeFloatingButtonSettings(View view) {
        FloatingActionMenu floorButton = (FloatingActionMenu) view.findViewById(R.id.floor_button);
        //turn off button rotation on click

        if (floorButton != null) {
            floorButton.setIconAnimated(false);
            FloatingActionButton floorSelected = (FloatingActionButton) view.findViewById(R.id.fab1);
            floorSelected.setColorNormal(ContextCompat.getColor(context, R.color.rca_primary));
            FloatingActionButton floor5 = (FloatingActionButton) view.findViewById(R.id.fab5);
            FloatingActionButton floor3 = (FloatingActionButton) view.findViewById(R.id.fab3);
            FloatingActionButton floor4 = (FloatingActionButton) view.findViewById(R.id.fab4);
            FloatingActionButton floor2 = (FloatingActionButton) view.findViewById(R.id.fab2);
            floor5.setColorNormal(ContextCompat.getColor(context, R.color.rca_onclick));
            floor2.setColorNormal(ContextCompat.getColor(context, R.color.rca_onclick));
            floor3.setColorNormal(ContextCompat.getColor(context, R.color.rca_onclick));
            floor4.setColorNormal(ContextCompat.getColor(context, R.color.rca_onclick));
        }
    }

    /**
     * Change the image of the floor map
     *
     * @param floorID    floor number
     * @param floorPlans
     * @param markerList
     */
    public void switchFloor(int floorID, List<FloorPlan> floorPlans, List<Marker> markerList) {
        //http://stackoverflow.com/questions/16369814/how-to-access-the-drawable-resources-by-name-in-android
        int index = floorID - 1; //Todo if floor object aren't in order then we will need to loop to find the correct one by id

        Log.d("markerList", String.valueOf(markerList.size()));

        displayCurrentFloorPointOfInterest(floorID, markerList);

        if (!freeExploration) {
            updateFloorLines(floorID);
        }

        groundOverlayFloorMap.setImage(BitmapDescriptorFactory.fromResource(getFloorPlanResourceID(floorPlans, index)));
        groundOverlayFloorMapBound = groundOverlayFloorMap.getBounds();
    }

    public void initFloorLines() {
        List<Polyline> defaultFloorLines = floorLineMap.get(DEFAULT_FLOOR_ID);
        this.currentFloorID = DEFAULT_FLOOR_ID;

        for (Polyline line : defaultFloorLines) {
            line.setVisible(true);
        }
    }

    private void updateFloorLines(int newFloorID) {
        List<Polyline> currentFloorLines = floorLineMap.get(currentFloorID);
        List<Polyline> newFloorLines = floorLineMap.get(newFloorID);
        this.currentFloorID = newFloorID;

        for (Polyline line : currentFloorLines) {
            line.setVisible(false);
        }
        for (Polyline line : newFloorLines) {
            line.setVisible(true);
        }
    }

    /**
     * Show only marker that are meant for the current floor
     *
     * @param floorID
     * @param markerList
     */
    public void displayCurrentFloorPointOfInterest(int floorID, List<Marker> markerList) {
        for (Marker marker : markerList) {
            PointMarker.Information pMarkerInfo = new PointMarker.Information(marker.getSnippet());

            if ((pMarkerInfo.getFloorID()) == floorID) {
                marker.setVisible(true);
            } else {
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
            if (marker.isVisible()) {
                builder.include(marker.getPosition());
            }
        }
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        zoomToFitUsed = true;
    }

    /**
     * limit the max and min zoom
     *
     * @param position
     */
    public void zoomLimit(CameraPosition position) {
        if (position.zoom > ZOOM_MAX)
            mMap.animateCamera(CameraUpdateFactory.zoomTo((float) ZOOM_MAX));
        if (position.zoom < ZOOM_MIN) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo((float) ZOOM_MIN));
        }
    }


    /**
     * Every time a camera view change, it will verify its position to determine if it is out of bound.
     *
     * @param current
     */
    public void verifyCameraBounds(LatLngBounds current) {
        if (!current.contains(groundOverlayFloorMapBound.getCenter())) {
            //if your zoomed in, then it check if you are inside the overlay map
            if (!groundOverlayFloorMapBound.contains(current.getCenter())) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(groundOverlayFloorMapBound.getCenter()));
                Log.d("verifyCameraBounds", "YES, view out of bound");
            }
        } else {
            Log.d("verifyCameraBounds", "NO, view is correct");
        }
    }


    /**
     * zooms in on the map by moving the viewpoint's height closer
     */
    public void zoomIn() {
        if (zoomToFitUsed || pinchZoomUsed) {
            float zoomValue = mMap.getCameraPosition().zoom;
            zoomFitToZoomLevel(zoomValue);
        }

        if (zoomLevel < 5) {
            zoomLevel++;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(getDesiredZoomLevel(zoomLevel)));
        }
    }

    /**
     * zooms out on the map by moving the viewpoint's height farther away
     */
    public void zoomOut() {
        Log.v("pinch", "floor" + zoomLevel);
        if (zoomToFitUsed || pinchZoomUsed) {
            float zoomValue = mMap.getCameraPosition().zoom;
            zoomFitToZoomLevel(zoomValue);
        }


        if (zoomLevel > 1) {
            zoomLevel--;
            mMap.animateCamera(CameraUpdateFactory.zoomTo(getDesiredZoomLevel(zoomLevel)));
        }
    }

    public void initialCameraPosition() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(groundOverlayFloorMapBound.getCenter(), (float) ZOOM_MIN));
    }


    public LatLngBounds getGroundOverlayFloorMapBound() {
        return groundOverlayFloorMapBound;
    }

    /**
     * convert level to zoom value
     *
     * @param level
     * @return
     */
    private float getDesiredZoomLevel(int level) {
        float zoom = 13f;

        switch (level) {
            case 1:
                zoom = 13f;
                break;
            case 2:
                zoom = 13.5f;
                break;
            case 3:
                zoom = 14f;
                break;
            case 4:
                zoom = 14.5f;
                break;
            case 5:
                zoom = 15.0f;
                break;
        }

        return zoom;
    }

    /**
     * convert zoomFitAll new zoom value to the closest zoom level
     *
     * @param zoomValue
     */
    private void zoomFitToZoomLevel(float zoomValue) {


        if (zoomValue <= 13.25f) {
            zoomLevel = 1;
        } else if (zoomValue <= 13.75f && zoomValue > 13.25f) {
            zoomLevel = 2;
        } else if (zoomValue <= 14.25f && zoomValue > 13.75f) {
            zoomLevel = 3;
        } else if (zoomValue <= 14.75f && zoomValue > 14.25f) {
            zoomLevel = 4;
        } else if (zoomValue > 14.75f) {
            zoomLevel = 5;
        }
        zoomToFitUsed = false;
        pinchZoomUsed = false;


    }

    public GroundOverlay getGroundOverlayFloorMap() {
        return groundOverlayFloorMap;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    //TODO: use methods for shortest path user story

    /**
     * This function is meant to trace the path between nodes in the arraylist of coordinates
     * representing each node's latitudinal and longitudinal position respectively.
     *
     * @param nodes This is the list of nodes that are to be sorted through. The nodes could be
     *              either points of interest, points of traversal, or others.
     */
    public void tracePath(List<Node> nodes, int floorID, java.util.Map<String, Polyline> polylineList) {

        List<LatLng> nodePositions = listNodeCoordinates(nodes, floorID);
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .width(10)
                .color(Color.parseColor("#99E33C3C")));
        line.setPoints(nodePositions);
        polylineList.put("hello", line);
    }


    /**
     * This method is used to return a list of LatLng coordinates associated with the list of nodes passed as a parameter.
     *
     * @param nodes The list of nodes for which coordinates should be derived.
     * @return The list of LatLng coordinates.
     */
    public List<LatLng> listNodeCoordinates(List<Node> nodes, int floorID) {
        if (nodes == null) {
            return null;
        }

        List<LatLng> nodeLatLngs = new ArrayList<LatLng>();
        for (Node node : nodes) {
            if (node.getFloorID() == floorID) {
                nodeLatLngs.add(new LatLng(node.getY(), node.getX()));
            }
        }
        return nodeLatLngs;
    }

    public void detectingPinchZoom(CameraPosition cameraPosition) {

        Log.v("pinch", String.valueOf(cameraPosition.zoom));
        pinchZoomUsed = false;
        if (cameraPosition.zoom == 13.f) {
            zoomLevel = 1;
        } else if (cameraPosition.zoom == 13.5f) {
            zoomLevel = 2;
        } else if (cameraPosition.zoom == 14f) {
            zoomLevel = 3;
        } else if (cameraPosition.zoom == 14.5f) {
            zoomLevel = 4;
        } else if (cameraPosition.zoom == 15f) {
            zoomLevel = 5;
        } else {
            pinchZoomUsed = true;
            Log.v("pinch", "detected");
        }

    }
}
