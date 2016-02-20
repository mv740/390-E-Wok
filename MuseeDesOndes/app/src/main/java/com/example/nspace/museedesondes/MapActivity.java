package com.example.nspace.museedesondes;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;


import com.example.nspace.museedesondes.AudioService.AudioBinder;


import com.example.nspace.museedesondes.Model.Map;
import com.example.nspace.museedesondes.Model.PointOfInterest;
import com.example.nspace.museedesondes.Utility.MapManager;
import com.example.nspace.museedesondes.Utility.PointMarker;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.maps.model.Polyline;
import com.example.nspace.museedesondes.Model.Node;

import java.util.ArrayList;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback, NavigationDrawerFragment.NavigationDrawerCallbacks, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GroundOverlay groundOverlay;
    private Map information;
    public static Drawable imgToSendToFullscreenImgActivity;
    AudioService audioService;
    private int[] floorButtonIdList = {R.id.fab1, R.id.fab2, R.id.fab3, R.id.fab4, R.id.fab5};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        bringButtonsToFront();
        Intent intent = new Intent(this, AudioService.class);
        bindService(intent, audioConnection, Context.BIND_AUTO_CREATE);

    }

    private void bringButtonsToFront() {
        FloatingActionButton ham = (FloatingActionButton) findViewById(R.id.hamburger);
        FloatingActionButton search = (FloatingActionButton) findViewById(R.id.search_button);
        FloatingActionMenu floor = (FloatingActionMenu) findViewById(R.id.floor_button);

        ham.bringToFront();
        search.bringToFront();
        floor.bringToFront();

    }

    public void onHamClick(View v) {
        mNavigationDrawerFragment.toggleDrawer(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        information = Map.getInstance(getApplicationContext());

        mMap = googleMap;
        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);


        //// TODO: 2/7/2016  need to get the lat/lng of each map et bound the available view screen
//        final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(0.027,-0.02), new LatLng(41.9667, 12.5938));
//        final int MAX_ZOOM = 16;
//        final int MIN_ZOOM = 13;


        LatLng custom = new LatLng(0.027, -0.02);
        LatLng center = new LatLng(0, 0);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 14));
        mMap.clear();
        mMap.setOnMarkerClickListener(this);


        //load map and then switch floor to 5
        // GroundOverlay groundOverlay = MapManager.loadDefaultFloor(mMap, custom);
        groundOverlay = MapManager.loadDefaultFloor(mMap, custom, information.getFloorPlans(), getApplicationContext(), findViewById(android.R.id.content));
        //need to implement a list view
        //MapManager.switchFloor(groundOverlay, 5);


        //// TODO: 2/7/2016 refactor this in a proper fuction
        //// 2/18/2016 Completed by Harrison.
        ArrayList<PointOfInterest> pointsOfInterest = information.getPointOfInterests();
        final ArrayList<MarkerOptions> markers = placeMarkersOnPointsOfInterest(pointsOfInterest);


        //todo testing currently 2/19/15
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                //android Zoom-to-Fit All Markers on Google Map
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (MarkerOptions marker : markers)
                {
                    builder.include(marker.getPosition());
                }
                LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            }
        });


        // Obtains ALL nodes.
        ArrayList<Node> nodes = information.getNodes();

        // This statement places all the nodes on the map and traces the path between them.
        tracePath(nodes);


    }

    /**
     * This method places the AZURE markers on the list of points of interest.
     *
     * @param pointsOfInterest List of all points of interest.
     */
    private ArrayList<MarkerOptions> placeMarkersOnPointsOfInterest(ArrayList<PointOfInterest> pointsOfInterest) {
        ArrayList<MarkerOptions> mMarkerArray = new ArrayList<>();
        for (PointOfInterest pointOfInterest : pointsOfInterest) {
            PointMarker.singleInterestPointFactory(pointOfInterest, getApplicationContext(), mMap, mMarkerArray);
        }
        return  mMarkerArray;
    }

    /**
     * This function is meant to trace the path between nodes in the arraylist of coordinates
     * representing each node's latitudinal and longitudinal position respectively.
     *
     * @param nodes This is the list of nodes that are to be sorted through. The nodes could be
     *              either points of interest, points of traversal, or others.
     */
    public void tracePath(ArrayList<Node> nodes) {

        ArrayList<LatLng> nodePositions = listNodeCoordinates(nodes);
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .width(15)
                .color(Color.parseColor("#99E33C3C")));
        line.setPoints(nodePositions);
    }


    /**
     * This method is used to return a list of LatLng coordinates associated with the list of nodes passed as a parameter.
     *
     * @param nodes The list of nodes for which coordinates should be derived.
     * @return The list of LatLng coordinates.
     */
    public ArrayList<LatLng> listNodeCoordinates(ArrayList<Node> nodes) {
        if (nodes == null) {
            return null;
        }

        ArrayList<LatLng> nodeLatLngs = new ArrayList<LatLng>();
        for (Node node : nodes) {
            nodeLatLngs.add(new LatLng(node.getX(), node.getY()));
        }
        return nodeLatLngs;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }


    public void poiImgOnClick(View v) {
        imgToSendToFullscreenImgActivity = ((ImageView) v).getDrawable();
        Intent fullscreenImgActivity = new Intent(MapActivity.this, FullscreenImgActivity.class);
        startActivity(fullscreenImgActivity);
    }

    public void floorButtonOnClick(View v) {
        //maps floor button id to floor id
        switch (v.getId()) {
            case R.id.fab1:
                changeFloor(1);
                break;
            case R.id.fab2:
                changeFloor(2);
                break;
            case R.id.fab3:
                changeFloor(3);
                break;
            case R.id.fab4:
                changeFloor(4);
                break;
            case R.id.fab5:
                changeFloor(5);
                break;
        }

        FloatingActionButton floor;
        for (int buttonId : floorButtonIdList) {
            floor = (FloatingActionButton) this.findViewById(buttonId);
            if (buttonId == v.getId()) {
                floor.setColorNormal(ContextCompat.getColor(this, R.color.rca_primary));
            } else {
                floor.setColorNormal(ContextCompat.getColor(this, R.color.rca_onclick));
            }
        }
    }

    public void changeFloor(int floor) {
        MapManager.switchFloor(groundOverlay, floor, information.getFloorPlans(), getApplicationContext());
        FloatingActionMenu floorButton = (FloatingActionMenu) findViewById(R.id.floor_button);
        floorButton.toggle(true);
    }

    public void playAudioFile(View v) {
        audioService.toggleAudioOnOff(v);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //move camera to marker postion
        LatLng markerLocation = marker.getPosition();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLocation));

        //update SlidingPanel to selected point of interest
        SlidingUpPanelLayout layout = (SlidingUpPanelLayout) this.findViewById(R.id.sliding_layout);
        PointOfInterest pointOfInterest = information.searchPoiByTitle(marker.getTitle());
        String description = pointOfInterest.getLocaleDescription(getApplicationContext()).getDescription();
        String title = pointOfInterest.getLocaleDescription(getApplicationContext()).getTitle();

        PoiPanel.replaceTitle((SlidingUpPanelLayout) findViewById(R.id.sliding_layout), title);
        PoiPanel.replaceDescription((SlidingUpPanelLayout) findViewById(R.id.sliding_layout), description);

        layout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        return true;
    }

    private ServiceConnection audioConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioBinder binder = (AudioBinder) service;
            audioService = binder.getAudioService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
