package com.example.nspace.museedesondes.utility;

import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.nspace.museedesondes.MapActivity;
import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.model.Edge;
import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.Label;
import com.example.nspace.museedesondes.model.LabelledPoint;
import com.example.nspace.museedesondes.model.MuseumMap;
import com.example.nspace.museedesondes.model.Node;
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
import com.google.android.gms.maps.model.PolylineOptions;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by michal on 2/10/2016.
 */
public class MapManager implements POIBeaconListener {

    private static final double ZOOM_MAX = 15.0;
    private static final double ZOOM_MIN = 13.0;
    private static final int DEFAULT_FLOOR_ID = 1;
    private static final float WIDTH_WHITE_BACKGROUND = 20520f;
    private static final float HEIGHT_WHITE_BACKGROUND = 25704f;
    private GoogleMap mMap;
    private MapActivity context;
    private LatLngBounds groundOverlayFloorMapBound;
    private GroundOverlay groundOverlayFloorMap;
    private int zoomLevel = 1;
    private Map<Integer, List<Polyline>> floorLineMap;
    private Map<Integer, List<Marker>> floorMarkerMap;
    private int currentFloorID;
    private boolean freeExploration;
    private boolean zoomToFitUsed = false;
    private boolean pinchZoomUsed = false;
    private List<Marker> markerList;
    private List<FloorPlan> floorPlans;


    public MapManager(GoogleMap googleMap, MapActivity context, Map<Integer, List<Polyline>> floorLineMap, boolean freeExploration, List<FloorPlan> floorPlans) {
        this.mMap = googleMap;
        this.context = context;
        this.floorLineMap = floorLineMap;
        this.floorMarkerMap = new HashMap<>();
        this.freeExploration = freeExploration;
        this.floorPlans = floorPlans;
        this.currentFloorID = DEFAULT_FLOOR_ID;
        createEmptyFloorLineAndMarkerMaps();
    }

    /**
     * Load default floor with white background behind it
     *
     * @param view
     */
    public void loadDefaultFloor(View view) {
        initializeFloatingButtonSettings(view);


        BitmapDescriptor imageFloor;
        BitmapFactory.Options options;
        if(defaultFloorExist())
        {
            imageFloor = BitmapDescriptorFactory.fromResource(Resource.getFloorPlanResourceID(DEFAULT_FLOOR_ID, floorPlans, context));
            options = Resource.getFloorImageDimensionOptions(DEFAULT_FLOOR_ID, floorPlans, context);
        }
        else
        {
            imageFloor = BitmapDescriptorFactory.fromResource(Resource.getFloorPlanResourceID(floorPlans.get(0).getId(), floorPlans, context));
            options = Resource.getFloorImageDimensionOptions(floorPlans.get(0).getId(), floorPlans, context);
        }

        LatLng position = new LatLng(0, 0);


        float width = scaleDimension(options.outWidth);
        float height = scaleDimension(options.outHeight);

        Log.e("width", String.valueOf(width));
        Log.e("height", String.valueOf(height));


        GroundOverlayOptions customMap = new GroundOverlayOptions()
                .image(imageFloor)
                .position(position, width, height).anchor(0, 1)
                .zIndex(0);

        groundOverlayFloorMap = mMap.addGroundOverlay(customMap);
        groundOverlayFloorMapBound = groundOverlayFloorMap.getBounds();

        //add white background under floor map
        GroundOverlayOptions mapBackground = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.white_background))
                .position(groundOverlayFloorMapBound.getCenter(), WIDTH_WHITE_BACKGROUND, HEIGHT_WHITE_BACKGROUND)
                .zIndex(-1);
        mMap.addGroundOverlay(mapBackground);

        displayFloorLinesAndMarkers(DEFAULT_FLOOR_ID, true);
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

        updateFloorLinesAndMarkers(floorID);

        //http://stackoverflow.com/questions/16369814/how-to-access-the-drawable-resources-by-name-in-android
        groundOverlayFloorMap.setImage(BitmapDescriptorFactory.fromResource(Resource.getFloorPlanResourceID(floorID, floorPlans, context)));
        BitmapFactory.Options  options = Resource.getFloorImageDimensionOptions(floorID, floorPlans, context);
        float width = scaleDimension(options.outWidth);
        float height = scaleDimension(options.outHeight);
        groundOverlayFloorMap.setDimensions(width,height);
        groundOverlayFloorMapBound = groundOverlayFloorMap.getBounds();
    }


    private boolean defaultFloorExist()
    {
        for(FloorPlan floorPlan : floorPlans)
        {
            if(floorPlan.getId() == DEFAULT_FLOOR_ID)
            {
                return  true;
            }
        }
        return false;
    }

    private float scaleDimension(float value)
    {
        return value*3;
    }

    /*** FLOOR MARKER METHODS ***/

    //maps floor id to markers for displaying, also initializes map from marker -> node for on marker click events
    public void initFloorPOIMarkerMap(List<PointOfInterest> pointsOfInterestList, Map<Marker, PointOfInterest> markerPOIMap) {
        Marker marker;
        for(PointOfInterest pointOfInterest : pointsOfInterestList) {
            marker = PointMarkerFactory.singleInterestPointFactory(pointOfInterest, mMap);
            floorMarkerMap.get(pointOfInterest.getFloorID()).add(marker);
            markerPOIMap.put(marker, pointOfInterest);

        }
    }

    public void initFloorPOTMarkerMap(List<LabelledPoint> labelledPointList) {
        Marker marker;
        for (LabelledPoint labelledPoint : labelledPointList) {
            if(labelledPoint.getLabel() != Label.NONE) {
                marker = PointMarkerFactory.singleTransitionPointFactory(labelledPoint, mMap);
                floorMarkerMap.get(labelledPoint.getFloorID()).add(marker);
            }
        }
    }

    /*** FLOOR POLYLINE + MARKER METHODS ***/

    public void displayFloorLinesAndMarkers(Integer floorID, boolean visibility) {
        List<Polyline> floorLines = floorLineMap.get(floorID);
        markerList = floorMarkerMap.get(floorID);

        for (Polyline line : floorLines) {
            line.setVisible(visibility);
        }
        for (Marker marker : markerList) {
            marker.setVisible(visibility);
        }
    }

    public void defaultFloorLinesAndMarker()
    {
        updateFloorLinesAndMarkers(DEFAULT_FLOOR_ID);
    }

    private void updateFloorLinesAndMarkers(int newFloorID) {
        displayFloorLinesAndMarkers(currentFloorID, false);
        displayFloorLinesAndMarkers(newFloorID, true);
        this.currentFloorID = newFloorID;
    }

    public void createEmptyFloorLineAndMarkerMaps() {
        List<FloorPlan> floorPlans = MuseumMap.getInstance(context).getFloorPlans();
        for(FloorPlan floorPlan : floorPlans) {
            List<Polyline> lineList = new ArrayList<>();
            List<Marker> markerList = new ArrayList<>();
            floorLineMap.put(floorPlan.getId(), lineList);
            floorMarkerMap.put(floorPlan.getId(), markerList);
        }
    }

    /*** FLOOR MARKER METHODS ***/

    public void clearFloorLines(){
        for(List<Polyline> floorLines : floorLineMap.values()){
            for(Polyline line : floorLines) {
                line.remove();
            }
        }
    }

    public void initShortestPathFloorLineMap(List<Edge> edgeList) {
        Node node1, node2;
        Polyline line;

        for(Edge edge : edgeList) {
            node1 = edge.getStart();
            node2 = edge.getEnd();
            if(node1.getFloorID() == node2.getFloorID()) {
                line = getLineFromNodes(node1, node2);
                floorLineMap.get(node1.getFloorID()).add(line);
            }

        }
    }

    public void displayShortestPath(int startNodeID, Node destinationNode, boolean searchingExit) {
        MuseumMap information = MuseumMap.getInstance(context);
        NavigationManager navigation = new NavigationManager(information);
        List<DefaultWeightedEdge> weightedEdgeList;

        if(searchingExit) {
            weightedEdgeList = navigation.getShortestExitPath(startNodeID);
        } else {
            int destinationNodeID = destinationNode.getId();
            weightedEdgeList = navigation.findShortestPath(startNodeID, destinationNodeID);
        }

        if(!navigation.doesPathExist(weightedEdgeList )) {
            clearFloorLines();
            return;
        }
        List<Edge> shortestPath = navigation.getCorrespondingEdgesFromPathSequence(weightedEdgeList );

        //clear existing lines and set new floor lines to display the shortest path
        clearFloorLines();
        initShortestPathFloorLineMap(shortestPath);
        displayFloorLinesAndMarkers(currentFloorID, true);
    }

    private Polyline getLineFromNodes(Node node1, Node node2) {
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(node1.getY(), node1.getX()), new LatLng(node2.getY(), node2.getX()))
                .color(ContextCompat.getColor(context, R.color.rca_unexplored_segment))
                .width(10));
        line.setVisible(false);
        return line;
    }

    /*** CAMERA METHODS ***/

    /**
     * android Zoom-to-Fit All Markers on Google MuseumMap
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
        Map<Marker, PointOfInterest> markerPOIMap = context.getMarkerPointOfInterestMap();
        Marker marker;

        //fetching marker from node in the marker->node map in map activity
        for (Map.Entry<Marker, PointOfInterest> entry : markerPOIMap.entrySet()) {
            if (entry.getValue().equals(node)) {
                marker = entry.getKey();
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                return;
            }
        }
    }

    public int getCurrentFloorID() {
        return currentFloorID;
    }

    public List<FloorPlan> getFloorPlans() {
        return floorPlans;
    }
}
