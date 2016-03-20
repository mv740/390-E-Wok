package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.example.nspace.museedesondes.model.StoryLine;
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

import java.util.List;
import java.util.Map;

/**
 * Created by michal on 2/10/2016.
 */
public class MapManager implements POIBeaconListener {

    private static final double ZOOM_MAX = 15.0;
    private static final double ZOOM_MIN = 13.0;
    private static final int DEFAULT_FLOOR_ID = 1;
    private static final float WIDTH = 5520f;
    private static final float HEIGHT = 10704f;
    private static final float WIDTH_WHITE_BACKGROUND = 20520f;
    private static final float HEIGHT_WHITE_BACKGROUND = 25704f;
    private GoogleMap mMap;
    private Context context;
    private LatLngBounds groundOverlayFloorMapBound;
    private GroundOverlay groundOverlayFloorMap;
    private int zoomLevel = 1;
    private Map<Integer, List<Polyline>> floorLineMap;
    private int currentFloorID;
    private boolean freeExploration;
    private boolean zoomToFitUsed = false;
    private boolean pinchZoomUsed = false;
    private List<Marker> markerList;
    private List<FloorPlan> floorPlans;


    public MapManager(GoogleMap googleMap, Context context, Map<Integer, List<Polyline>> floorLineMap, boolean freeExploration, List<FloorPlan> floorPlans) {
        this.mMap = googleMap;
        this.context = context;
        this.floorLineMap = floorLineMap;
        this.freeExploration = freeExploration;
        this.floorPlans = floorPlans;
    }

    /**
     * Load default floor with white background behind it
     *
     * @param view
     */
    public void loadDefaultFloor(View view) {
        initializeFloatingButtonSettings(view);


        BitmapDescriptor imageFloor = BitmapDescriptorFactory.fromResource(getFloorPlanResourceID(DEFAULT_FLOOR_ID));
        LatLng position = new LatLng(0, 0);


        GroundOverlayOptions customMap = new GroundOverlayOptions()
                .image(imageFloor)
                .position(position, WIDTH, HEIGHT).anchor(0, 1)
                .zIndex(0);

        groundOverlayFloorMap = mMap.addGroundOverlay(customMap);
        groundOverlayFloorMapBound = groundOverlayFloorMap.getBounds();

        //add white background under floor map
        GroundOverlayOptions mapBackground = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.white_background))
                .position(groundOverlayFloorMapBound.getCenter(), WIDTH_WHITE_BACKGROUND, HEIGHT_WHITE_BACKGROUND)
                .zIndex(-1);
        mMap.addGroundOverlay(mapBackground);

        if (!freeExploration) {
            initFloorLines();
        }
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
     * @param floorID floor number
     */
    public void switchFloor(int floorID) {

        displayCurrentFloorPointOfInterest(floorID);

        if (!freeExploration) {
            updateFloorLines(floorID);
        }
        //http://stackoverflow.com/questions/16369814/how-to-access-the-drawable-resources-by-name-in-android
        groundOverlayFloorMap.setImage(BitmapDescriptorFactory.fromResource(getFloorPlanResourceID(floorID)));
        groundOverlayFloorMapBound = groundOverlayFloorMap.getBounds();
    }

    /**
     * Search correct floorPlan
     * floorPlan index is not the floorPlan id,  The Json could have floorPlan 3,5,8,1 in random order
     *
     * @param id
     * @return
     */
    private FloorPlan searchFloorPlanById(int id) {
        for (FloorPlan currentFloor : floorPlans) {
            if (currentFloor.getId() == id) {
                return currentFloor;
            }
        }
        return null;
    }

    private int getFloorPlanResourceID(int id) {

        FloorPlan floorPlan = searchFloorPlanById(id);
        return Resource.getResourceIDFromPath(floorPlan.getImagePath(),context);
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
     */
    public void displayCurrentFloorPointOfInterest(int floorID) {
        for (Marker marker : markerList) {
            PointMarkerFactory.Information pMarkerInfo = new PointMarkerFactory.Information(marker.getSnippet());

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
    public void zoomToFit() {

        int counter = 0;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markerList) {
            if (marker.isVisible()) {
                builder.include(marker.getPosition());
                counter++;
            }
        }
        if (counter > 0) {
            LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            zoomToFitUsed = true;
        }
    }

    /**
     * limit the max and min zoom
     *
     * @param position
     */
    public void zoomLimit(CameraPosition position) {
        if (position.zoom > ZOOM_MAX) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo((float) ZOOM_MAX));
        }
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
            default:
                Log.e("MapManager", "level invalid " + level);
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

    /**
     * Verifying if the zoom value correspond to one of the existing level value.
     * if it doesn't then pinch gesture were used to zoom in/out.
     *
     * @param cameraPosition
     */
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

    public void setMarkerList(List<Marker> markerList) {
        this.markerList = markerList;
    }

    /**
     * updates poi marker color to red after reaching beacon
     *
     * @param node
     * @param storyLine
     */
    public void onPOIBeaconDiscovered(PointOfInterest node, StoryLine storyLine) {
        for(Marker marker : markerList) {
            PointMarkerFactory.Information pMarkerInfo = new PointMarkerFactory.Information(marker.getSnippet());
            if(pMarkerInfo.getNodeID() == node.getId()) {
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                return;
            }
        }
    }
}
