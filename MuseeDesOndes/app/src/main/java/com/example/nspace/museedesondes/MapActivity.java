package com.example.nspace.museedesondes;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.example.nspace.museedesondes.fragments.NavigationDrawerFragment;
import com.example.nspace.museedesondes.model.Edge;
import com.example.nspace.museedesondes.model.Map;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.example.nspace.museedesondes.model.StoryLine;
import com.example.nspace.museedesondes.services.AudioService;
import com.example.nspace.museedesondes.services.AudioService.AudioBinder;
import com.example.nspace.museedesondes.utility.MapManager;
import com.example.nspace.museedesondes.utility.Navigation;
import com.example.nspace.museedesondes.utility.PoiPanelManager;
import com.example.nspace.museedesondes.utility.PointMarkerFactory;
import com.example.nspace.museedesondes.utility.Preferences;
import com.example.nspace.museedesondes.utility.StoryLineManager;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.VisibleRegion;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationDrawerFragment.NavigationDrawerCallbacks, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Map information;
    AudioService audioService;
    private int[] floorButtonIdList = {R.id.fab1, R.id.fab2, R.id.fab3, R.id.fab4, R.id.fab5};
    private StoryLineManager storyLineManager;
    private StoryLine storyLine;
    private boolean freeExploration;
    private MapManager mapManager;
    private SeekBar seekBar;
    Handler audioHandler = new Handler();

    public PoiPanelManager getPanel() {
        return panel;
    }

    private PoiPanelManager panel;
    private Marker selectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.panel = new PoiPanelManager(this);

        //create storyline manager which handles storyline progression and interaction with the beacons
        information = Map.getInstance(getApplicationContext());
        getStoryLineSelected();

        if (!freeExploration) {
            storyLineManager = new StoryLineManager(storyLine, this);
            storyLineManager.registerObserver(panel);
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
    }


    //sets the storyline to the one selected in the StoryLineActivity
    private void getStoryLineSelected() {
        Intent mIntent = getIntent();
        int position = mIntent.getIntExtra("Story line list position", 0);
        List<StoryLine> storyLineList = information.getStoryLines();

        if (position == storyLineList.size()) {
            freeExploration = true;
        } else {
            storyLine = storyLineList.get(position);
            freeExploration = false;
        }
    }

    private void bringButtonsToFront() {
        FloatingActionButton ham = (FloatingActionButton) findViewById(R.id.hamburger);
        final FloatingActionMenu floorMenu = (FloatingActionMenu) findViewById(R.id.floor_button);

        FloatingActionButton zoomIn = (FloatingActionButton) findViewById(R.id.zoomInButton);
        FloatingActionButton zoomOut = (FloatingActionButton) findViewById(R.id.zoomOutButton);

        FloatingActionButton fitAllMarker = (FloatingActionButton) findViewById(R.id.zoomShowAllMarker);
        fitAllMarker.setVisibility(View.INVISIBLE);
        floorMenu.setClosedOnTouchOutside(true);

        FloatingActionButton getDirections = (FloatingActionButton) findViewById(R.id.get_directions_button);

        ham.bringToFront();
        floorMenu.bringToFront();
        zoomIn.bringToFront();
        zoomOut.bringToFront();
        getDirections.bringToFront();

        //show only fitAllMarker button in free exploration
        if (freeExploration) {
            fitAllMarker.bringToFront();
            fitAllMarker.setVisibility(View.VISIBLE);
        }

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

        java.util.Map<Integer, List<Polyline>> floorLineMap = new HashMap<>();

        mMap = googleMap;
        mMap.clear();
        mMap.setOnMarkerClickListener(this);
        initializeMapSetting();
        mapManager = new MapManager(mMap, this, floorLineMap, freeExploration, information.getFloorPlans());
        mapManager.createEmptyFloorLineMap();

        //initialize storyline manager
        if (!freeExploration) {
            storyLineManager.setGoogleMap(mMap);
            storyLineManager.setFloorLineMap(floorLineMap);
            storyLineManager.initSegmentListAndFloorLineMap();
        }

        //loading initial map
        mapManager.loadDefaultFloor(findViewById(android.R.id.content));
        mapManager.initialCameraPosition();

        //load map markers for storyline or all poi markers for free exploration
        if (freeExploration) {
            mapManager.setMarkerList(placeMarkersOnPointsOfInterest(information.getPointOfInterests()));
        } else {
            mapManager.setMarkerList(placeMarkersOnPointsOfInterest(storyLineManager.getPointOfInterestList()));
            storyLineManager.registerObserver(mapManager);
        }
        mapManager.displayCurrentFloorPointOfInterest(1);

        mMap.setOnCameraChangeListener(new OnCameraChangeListener());
    }

    /**
     * This method places the AZURE markers on the list of points of interest.
     *
     * @param pointsOfInterestList List of all points of interest.
     */
    private List<Marker> placeMarkersOnPointsOfInterest(List<PointOfInterest> pointsOfInterestList) {

        List<Marker> mMarkerArray = new ArrayList<>();
        for (PointOfInterest pointOfInterest : pointsOfInterestList) {
            Marker marker = PointMarkerFactory.singleInterestPointFactory(pointOfInterest, getApplicationContext(), mMap, mapManager.getGroundOverlayFloorMapBound());
            mMarkerArray.add(marker);
        }
        return mMarkerArray;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    public void poiPanelMediaOnClick(View v, ImageView MediaResource, int videoResourceID) {

        Intent intent;
        if (MediaResource.getTag() == "VIDEO") {
            intent = new Intent(this, VideoActivity.class);
            String fileName = String.valueOf(videoResourceID);
            Log.e("name",fileName);
            intent.putExtra("File_Name", fileName);
        } else {
            panel.setSelectedImage(v);
            intent = new Intent(this, FullscreenImgActivity.class);
            intent.putExtra("imageId", panel.getSelectedImageId());
        }
        startActivity(intent);

    }

    public void floorButtonOnClick(View v) {
        //maps floor button id to floor id
        View fitAllMarker = findViewById(R.id.zoomShowAllMarker);
        fitAllMarker.setVisibility(View.INVISIBLE);


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
            default:
                Log.e("MapActivity", "floor button view invalid " + v.getId());
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
        if (freeExploration) {
            fitAllMarker.setVisibility(View.VISIBLE);
        }
    }

    public void changeFloor(int floor) {
        mapManager.switchFloor(floor);
        FloatingActionMenu floorButton = (FloatingActionMenu) findViewById(R.id.floor_button);
        floorButton.toggle(true);
    }

    public void playAudioFile(View v) {
        startAudio(panel.getCurrentPointOfInterest());

    }

    public void playVideo(View v) {
        Intent intent = new Intent(this, VideoActivity.class);
        String fileName = Integer.toString(R.raw.sample_video_1280x720_1mb);
        intent.putExtra("File_Name", fileName);
        startActivity(intent);
    }

    public void getDirections(View v) {
        //fetch start and end nodes
        PointOfInterest startNode = panel.getCurrentPointOfInterest();

        //TODO get ending node (beacon or screen select) testing with static node 3
        PointOfInterest endNode = information.searchPoiById(3);

        //get edge sequence from start node to end node, exits function if no path found
        Navigation navigation = new Navigation(information);
        List<DefaultWeightedEdge> defaultWeightedEdgeList = navigation.findShortestPath(startNode.getId(), endNode.getId());
        if(!navigation.doesPathExist(defaultWeightedEdgeList)) {
            return;
        }
        List<Edge> edgeList = navigation.getCorrespondingEdgesFromPathSequence(defaultWeightedEdgeList);

        //clear existing lines and set new floor lines to display the shortest path
        mapManager.clearFloorLines();
        mapManager.initShortestPathFloorLineMap(edgeList);
        mapManager.displayFloorLines(mapManager.getCurrentFloorID(), true);
    }

    public void startAudio(PointOfInterest pointOfInterest) {
        audioService.setAudio();
        int audioDuration = audioService.getAudioDuration();
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(audioDuration / 1000);
        audioRunnable.run();

        View view = findViewById(R.id.play_button);
        audioService.toggleAudioOnOff(view);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        selectedMarkerDisplay(marker);

        //move camera to marker postion
        LatLng markerLocation = marker.getPosition();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLocation));
        panel.update(marker);
        return true;
    }

    private void selectedMarkerDisplay(Marker marker) {
        if (selectedMarker != null) {
            selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        selectedMarker = marker;
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
    protected void onDestroy() {
        super.onDestroy();
        if (audioService != null) {
            unbindService(audioConnection);
        }
        if (!freeExploration) {
            storyLineManager.getBeaconManager().disconnect();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        //changing orientation will modify the language locale to "en"
        Preferences.setAppContext(this.getApplicationContext());
        Preferences.loadLanguagePreference();

        if (!freeExploration) {
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
        if (!freeExploration) {
            storyLineManager.getBeaconManager().stopRanging(storyLineManager.getRegion());
        }
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        if (audioService != null) {
            audioService.releaseAudio();
        }
    }

    public void zoomInButtonClick(View view) {
        mapManager.zoomIn();
    }

    public void zoomOutButtonClick(View view) {
        mapManager.zoomOut();
    }

    public void zoomShowAllMarker(View view) {
        mapManager.zoomToFit();
    }

    public Map getInformation() {
        return information;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public StoryLineManager getStoryLineManager() {
        return storyLineManager;
    }

    @Override
    public void onBackPressed() {
        if (panel.isOpen()) {
            panel.close();
        } else {
            this.finish();
        }
    }

    /**
     * Custom camera listener, that will verify view boundary,zoom limit, certain specific gesture
     */
    private class OnCameraChangeListener implements GoogleMap.OnCameraChangeListener {

        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            VisibleRegion vr = mMap.getProjection().getVisibleRegion();
            double left = vr.latLngBounds.southwest.longitude;
            double top = vr.latLngBounds.northeast.latitude;
            double right = vr.latLngBounds.northeast.longitude;
            double bottom = vr.latLngBounds.southwest.latitude;

            Log.v("onCameraChange", "left :" + left);
            Log.v("onCameraChange", "top :" + top);
            Log.v("onCameraChange", "right :" + right);
            Log.v("onCameraChange", "bottom :" + bottom);

            mapManager.detectingPinchZoom(cameraPosition);
            mapManager.zoomLimit(cameraPosition);
            LatLngBounds current = new LatLngBounds(vr.latLngBounds.southwest, vr.latLngBounds.northeast);
            mapManager.verifyCameraBounds(current);
        }
    }

    private void initializeMapSetting() {
        mMap.setBuildingsEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
    }
}
