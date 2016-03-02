package com.example.nspace.museedesondes;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;


import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.example.nspace.museedesondes.AudioService.AudioBinder;


import com.example.nspace.museedesondes.Model.Map;
import com.example.nspace.museedesondes.Model.PointOfInterest;
import com.example.nspace.museedesondes.Model.StoryLine;
import com.example.nspace.museedesondes.Utility.MapManager;
import com.example.nspace.museedesondes.Utility.PointMarker;
import com.example.nspace.museedesondes.Utility.StoryLineManager;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.maps.model.Polyline;
import com.example.nspace.museedesondes.Model.Node;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.model.VisibleRegion;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback, NavigationDrawerFragment.NavigationDrawerCallbacks, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GroundOverlay groundOverlay;
    public Map information;
    public static Bitmap imgToSendToFullscreenImgActivity;
    AudioService audioService;
    private int[] floorButtonIdList = {R.id.fab1, R.id.fab2, R.id.fab3, R.id.fab4, R.id.fab5};
    private StoryLineManager storyLineManager;
    private StoryLine storyLine;
    private boolean freeExploration;
    private ArrayList<Marker> markerList;
    private HashMap<String, Polyline> polylineList;
    private MapManager mapManager;
    private SeekBar seekBar;
    Handler audioHandler = new Handler();
    PoiPanel panel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.panel = new PoiPanel(this);

        //create storyline manager which handles storyline progression and interaction with the beacons
        information = Map.getInstance(getApplicationContext());
        getStoryLineSelected();
        if(!freeExploration){
            storyLineManager = new StoryLineManager(storyLine, this, panel, mMap);
        }

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
        seekBar = (SeekBar) findViewById(R.id.seekBar);



        this.polylineList = new HashMap<>();
    }

    //sets the storyline to the one selected in the StoryLineActivity
    private void getStoryLineSelected() {
        Intent mIntent = getIntent();
        int position = mIntent.getIntExtra("Story line list position", 0);
        ArrayList<StoryLine> storyLineList = information.getStoryLines();

        if(position == 0) {
            freeExploration = true;
        } else {
            storyLine = storyLineList.get(position - 1);
            freeExploration = false;
        }
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
        mapManager = new MapManager(mMap,this);
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
        groundOverlay = mapManager.loadDefaultFloor(mMap, custom, information.getFloorPlans(), findViewById(android.R.id.content));

        //need to implement a list view
        //MapManager.switchFloor(groundOverlay, 5);

        this.markerList = placeMarkersOnPointsOfInterest(information.getPointOfInterests());
        mapManager.displayCurrentFloorPointOfInterest(1, this.markerList);


        //todo testing currently 2/19/15
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mapManager.zoomToFit(markerList);
            }
        });


        // Obtains ALL nodes.
        ArrayList<Node> nodes = information.getNodes();

        // This statement places all the nodes on the map and traces the path between them.
        tracePath(nodes, 1, this.polylineList);

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            public void onCameraChange(CameraPosition position) {
                VisibleRegion vr = mMap.getProjection().getVisibleRegion();
                double left = vr.latLngBounds.southwest.longitude;
                double top = vr.latLngBounds.northeast.latitude;
                double right = vr.latLngBounds.northeast.longitude;
                double bottom = vr.latLngBounds.southwest.latitude;


                Log.v("onCameraChange", "left :"+left);
                Log.v("onCameraChange", "top :"+top);
                Log.v("onCameraChange", "right :"+right);
                Log.v("onCameraChange", "bottom :"+bottom);

                mapManager.zoomLimit(position);
                mapManager.verifyCameraPosition(left, top, right, bottom);
            }
        });
    }


    /**
     * This method places the AZURE markers on the list of points of interest.
     *
     * @param pointsOfInterest List of all points of interest.
     */
    private ArrayList<Marker> placeMarkersOnPointsOfInterest(ArrayList<PointOfInterest> pointsOfInterest) {
        ArrayList<Marker> mMarkerArray = new ArrayList<>();
        for (PointOfInterest pointOfInterest : pointsOfInterest) {
            PointMarker.singleInterestPointFactory(pointOfInterest, getApplicationContext(), mMap, mMarkerArray);
        }
        return mMarkerArray;
    }

    /**
     * This function is meant to trace the path between nodes in the arraylist of coordinates
     * representing each node's latitudinal and longitudinal position respectively.
     *
     * @param nodes This is the list of nodes that are to be sorted through. The nodes could be
     *              either points of interest, points of traversal, or others.
     */
    public void tracePath(ArrayList<Node> nodes, int floorID, HashMap<String, Polyline> polylineList) {

        ArrayList<LatLng> nodePositions = listNodeCoordinates(nodes, floorID);
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .width(15)
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
    public ArrayList<LatLng> listNodeCoordinates(ArrayList<Node> nodes, int floorID) {
        if (nodes == null) {
            return null;
        }

        ArrayList<LatLng> nodeLatLngs = new ArrayList<LatLng>();
        for (Node node : nodes) {
            if (node.getFloorID() == floorID) {
                nodeLatLngs.add(new LatLng(node.getX(), node.getY()));
            }
        }
        return nodeLatLngs;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }


    public void poiImgOnClick(View v) {
        imgToSendToFullscreenImgActivity = ((BitmapDrawable)((ImageView) v.findViewById(R.id.poi_panel_pic_item_imageview)).getDrawable()).getBitmap();
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
        mapManager.switchFloor(groundOverlay, floor, information.getFloorPlans(), this.markerList, this.polylineList);
        FloatingActionMenu floorButton = (FloatingActionMenu) findViewById(R.id.floor_button);
        floorButton.toggle(true);
    }

    public void playAudioFile(View v) {
        audioService.setAudio();
        int audioDuration = audioService.getAudioDuration();
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(audioDuration / 1000);
        audioRunnable.run();
        audioService.toggleAudioOnOff(v);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //move camera to marker postion
        LatLng markerLocation = marker.getPosition();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLocation));

        //update SlidingPanel to selected point of interest


        panel.update(marker);
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

    private Runnable audioRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = audioService.getCurrentPosition();
            seekBar.setProgress(currentPosition);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (audioService.isMediaSet() && fromUser) {
                        audioService.setAudioPosition(progress * 1000);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            audioHandler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if(!freeExploration) {
            SystemRequirementsChecker.checkWithDefaultDialogs(this);
            storyLineManager.getBeaconManager().connect(new BeaconManager.ServiceReadyCallback() {
                @Override
                public void onServiceReady() {
                    storyLineManager.getBeaconManager().startRanging(storyLineManager.getRegion());
                }
            });
        }
    }

    @Override
    protected void onPause() {
        if(!freeExploration) {
            storyLineManager.getBeaconManager().stopRanging(storyLineManager.getRegion());
        }
        super.onPause();
    }

}
