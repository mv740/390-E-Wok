package com.example.nspace.museedesondes;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.example.nspace.museedesondes.adapters.CoordinateAdapter;
import com.example.nspace.museedesondes.fragments.NavigationDrawerFragment;
import com.example.nspace.museedesondes.model.Audio;
import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.MuseumMap;
import com.example.nspace.museedesondes.model.Node;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.example.nspace.museedesondes.model.StoryLine;
import com.example.nspace.museedesondes.services.MediaService;
import com.example.nspace.museedesondes.services.MediaService.AudioBinder;
import com.example.nspace.museedesondes.utility.MapManager;
import com.example.nspace.museedesondes.utility.NavigationManager;
import com.example.nspace.museedesondes.utility.PoiPanelManager;
import com.example.nspace.museedesondes.utility.Preferences;
import com.example.nspace.museedesondes.utility.Resource;
import com.example.nspace.museedesondes.utility.StoryLineManager;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationDrawerFragment.NavigationDrawerCallbacks, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private MuseumMap information;
    MediaService mediaService;
    private StoryLineManager storyLineManager;
    private StoryLine storyLine;
    private boolean freeExploration;
    private boolean navigationMode;
    private boolean searchingExit;
    private NavigationManager navigationManager;
    private MapManager mapManager;
    private SeekBar seekBar;
    Handler audioHandler = new Handler();
    private Map<Marker, PointOfInterest> markerPointOfInterestMap;
    private PoiPanelManager panelManager;
    private Marker selectedMarker;
    private List<FloatingActionButton> floatingActionButtonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.panelManager = new PoiPanelManager(this);
        this.markerPointOfInterestMap = new HashMap<>();

        //create storyline manager which handles storyline progression and interaction with the beacons
        information = MuseumMap.getInstance(getApplicationContext());
        getStoryLineSelected();


        if (!freeExploration) {
            storyLineManager = new StoryLineManager(storyLine, this);
            storyLineManager.registerObserver(panelManager);
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
        Intent intent = new Intent(this, MediaService.class);
        bindService(intent, audioConnection, Context.BIND_AUTO_CREATE);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        this.navigationManager = new NavigationManager(information);
        this.navigationMode = false;
    }


    private void convertCoordinate(MuseumMap information, GoogleMap googleMap) {
        List<Node> nodeList = new ArrayList<>();
        nodeList.addAll(information.getLabelledPoints());
        nodeList.addAll(information.getPointOfInterests());

        for (Node currentP : nodeList) {
            int floorId = currentP.getFloorID();
            BitmapFactory.Options options = Resource.getFloorImageDimensionOptions(floorId, information.getFloorPlans(), getApplicationContext());

            Log.e("Option", String.valueOf(options.outHeight));
            FloorPlan floorPlan = Resource.searchFloorPlanById(floorId, information.getFloorPlans());
            String fileLocation = Resource.getAbsoluteFilePath(getApplicationContext(), floorPlan.getImagePath());
            Log.e("fileLocation", fileLocation);
            BitmapDescriptor imageFloor = BitmapDescriptorFactory.fromPath(fileLocation);


            GroundOverlayOptions customMap = new GroundOverlayOptions()
                    .image(imageFloor)
                    .position(new LatLng(0, 0), options.outWidth * 3, options.outHeight * 3);

            GroundOverlay groundOverlayFloorMap = googleMap.addGroundOverlay(customMap);

            CoordinateAdapter coordinateAdapter = new CoordinateAdapter(floorPlan, groundOverlayFloorMap.getBounds());
            currentP.setY(coordinateAdapter.convertY(currentP));
            currentP.setX(coordinateAdapter.convertX(currentP));
            information.setCoordinateAlreadyConverted(true);
        }
    }


    //sets the storyline to the one selected in the StoryLineActivity
    private void getStoryLineSelected() {
        Intent mIntent = getIntent();
        int id = mIntent.getIntExtra("Story line id", 0);

        if (id == -1) {
            freeExploration = true;
        } else {
            storyLine = information.searchStorylineById(id);
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

        generateFloorButtons(floorMenu);

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

    /**
     * create the require floor buttons
     *
     * @param floorMenu
     */
    private void generateFloorButtons(FloatingActionMenu floorMenu) {
        boolean firstButtonDone = false;
        this.floatingActionButtonList = new ArrayList<>();
        for (FloorPlan currentFloor : MuseumMap.getInstance(MapActivity.this).getFloorPlans()) {
            final FloatingActionButton floorButton = new FloatingActionButton(MapActivity.this);
            switch (currentFloor.getId()) {
                case 1:
                    floorButton.setImageResource(R.drawable.floor1_icon);
                    break;
                case 2:
                    floorButton.setImageResource(R.drawable.floor2_icon);
                    break;
                case 3:
                    floorButton.setImageResource(R.drawable.floor3_icon);
                    break;
                case 4:
                    floorButton.setImageResource(R.drawable.floor4_icon);
                    break;
                case 5:
                    floorButton.setImageResource(R.drawable.floor5_icon);
                    break;
                default:
                    Log.e("FloorIcon", "not found");
            }
            if (!firstButtonDone) {
                floorButton.setColorNormal(ContextCompat.getColor(MapActivity.this, R.color.rca_primary));
                firstButtonDone = true;
            } else {
                floorButton.setColorNormal(ContextCompat.getColor(MapActivity.this, R.color.rca_onclick));
            }
            floorButton.setColorPressed(ContextCompat.getColor(MapActivity.this, R.color.rca_primary));
            floorButton.setColorRipple(ContextCompat.getColor(MapActivity.this, R.color.rca_onclick));
            floorButton.setId(currentFloor.getId());
            floorButton.setOnClickListener(new FloorButtonOnClickListener());
            floorButton.setTag(currentFloor.getId());
            floatingActionButtonList.add(floorButton);
            floorMenu.addMenuButton(floorButton);
        }
    }


    private class FloorButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Log.e("floor", String.valueOf(v.getId()));
            changeFloor(v.getId());

            for (FloatingActionButton current : floatingActionButtonList) {
                if (current.getId() == v.getId()) {
                    current.setColorNormal(ContextCompat.getColor(MapActivity.this, R.color.rca_primary));
                } else {
                    current.setColorNormal(ContextCompat.getColor(MapActivity.this, R.color.rca_onclick));
                    current.setColorPressed(ContextCompat.getColor(MapActivity.this, R.color.rca_primary));
                }
            }


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
        information = MuseumMap.getInstance(getApplicationContext());
        if (!information.isCoordinateAlreadyConverted()) {
            convertCoordinate(information, googleMap);
        }

        Map<Integer, List<Polyline>> floorLineMap = new HashMap<>();

        mMap = googleMap;
        mMap.clear();
        mMap.setOnMarkerClickListener(this);
        initializeMapSetting();
        mapManager = new MapManager(mMap, this, floorLineMap, freeExploration, information.getFloorPlans());

        //loading initial map
        mapManager.loadDefaultFloor(findViewById(android.R.id.content));

        //initialize storyline manager
        if (!freeExploration) {
            storyLineManager.setGoogleMap(mMap);
            storyLineManager.setFloorLineMap(floorLineMap);
            storyLineManager.initSegmentListAndFloorLineMap();
        }

        //initialize markers for labelled points on floor
        mapManager.initFloorPOTMarkerMap(information.getLabelledPoints());

        //load map markers for storyline or all poi markers for free exploration
        if (freeExploration) {
            mapManager.initFloorPOIMarkerMap(information.getPointOfInterests(), markerPointOfInterestMap);
        } else {
            mapManager.initFloorPOIMarkerMap(storyLineManager.getPointOfInterestList(), markerPointOfInterestMap);
            storyLineManager.registerObserver(mapManager);
        }

        mapManager.defaultFloorLinesAndMarker();
        mapManager.initialCameraPosition();


        mMap.setOnCameraChangeListener(new OnCameraChangeListener());
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    public void poiPanelMediaOnClick(View v, ImageView MediaResource, int videoResourceID, String videoResourceFilePath) {

        Intent intent;
        if (MediaResource.getTag() == "VIDEO") {
            intent = new Intent(this, VideoActivity.class);
            //String fileName = String.valueOf(videoResourceID);
            String fileName = videoResourceFilePath;
            Log.e("name", fileName);
            intent.putExtra("File_Name", fileName);
        } else {
            panelManager.setSelectedImage(v);
            intent = new Intent(this, FullscreenImgActivity.class);
            intent.putExtra("imageId", panelManager.getSelectedImageFilePath());
        }
        startActivity(intent);

    }

    public void changeFloor(int floor) {
        mapManager.switchFloor(floor);
        FloatingActionMenu floorButton = (FloatingActionMenu) findViewById(R.id.floor_button);
        floorButton.toggle(true);
    }

    public void playAudioFile(View v) {
        startAudio(panelManager.getCurrentPointOfInterest());

    }

    public void getDirections(View v) {

        if (navigationMode) {
            mapManager.clearFloorLines();
            navigationManager.stopNavigationMode();
            navigationManager.clear();
            navigationMode = false;
            searchingExit = false;
        } else {
            navigationManager.startNavigationMode(panelManager);
            navigationMode = true;
        }
    }

    /**
     * This method sets audio files associated with the current storyline (if applicable, default
     * otherwise), and plays them.
     *
     * @param pointOfInterest
     */
    public void startAudio(PointOfInterest pointOfInterest) {
        // get the audio files associated with a point of interest.
        String fileName = "";
        if(!freeExploration)
        {
            fileName = pointOfInterest.getStoryRelatedAudios(storyLine.getId(),getApplicationContext()).get(0).getPath();
        }else
        {
           fileName= pointOfInterest.getLocaleAudios(getApplicationContext()).get(0).getPath();
        }


        // associates the ID to the media player of the media service
        mediaService.setAudio(fileName);

        int audioDuration = mediaService.getAudioDuration();
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(audioDuration / 1000);
        audioRunnable.run();

        View view = findViewById(R.id.play_button);
        mediaService.toggleAudioOnOff(view);
    }

    /**
     * On select click, set audio service to new audio
     *
     * @param button
     */
    public void selectAudioFile(View button) {
        Context wrapper = new ContextThemeWrapper(getApplicationContext(), R.style.AppCompatAlertDialogStyle);
        final PopupMenu popup = new PopupMenu(wrapper, button);

        List<Audio> audioList = panelManager.getCurrentPointOfInterest().getLocaleAudios(getApplicationContext());
        TextView textFileName = (TextView) findViewById(R.id.audioPlayerName);
        String currentAudioName = textFileName.getText().toString();
        for (int i = 0; i < audioList.size(); i++) {
            String current = Resource.getFileNameWithoutExtension(audioList.get(i).getPath());
            MenuItem item = popup.getMenu().add(Menu.NONE, i, Menu.NONE, current);
            if (current.equalsIgnoreCase(currentAudioName)) {
                item.setCheckable(true);
                item.setChecked(true);
            }
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                setupSelectedAudio(item.getItemId(), popup);
                return true;
            }
        });

        popup.show();
    }

    /**
     * Reset audio service and set it with new selected one
     *
     * @param audioID
     * @param popup
     */
    public void setupSelectedAudio(int audioID, PopupMenu popup) {
        if (!popup.getMenu().getItem(audioID).isChecked()) {
            getMediaService().releaseAudio();
            Button playAudio = (Button) findViewById(R.id.play_button);
            playAudio.setBackgroundResource(R.drawable.ic_play_circle_filled_white_48dp);

            mediaService.setAudio(panelManager.getCurrentPointOfInterest().getLocaleAudios(getApplicationContext()).get(audioID).getPath());

            int audioDuration = mediaService.getAudioDuration();
            SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
            seekBar.setMax(audioDuration / 1000);
        }
    }

    /**
     * This method is called when a marker is selected.
     * <p/>
     * If the marker selected is not a point of interest it is instead a labelled point (ex stairs,
     * exit, washroom) and no interaction with poi panel or navigation occurs. This also occurs in
     * storyline mode when a visitor has not yet discovered a poi beacon.
     * <p/>
     * If the user is in navigation mode, the
     * user will have already indicated the destination point. The method will proceed to generate
     * the shortest path between the already selected marker and the marker selected in navigation
     * mode, .
     * <p/>
     * If the user is not in navigation mode, the method registers the marker as the
     * "currentPointOfInterest" and changes the color of the marker to red.
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        if (markerPointOfInterestMap.containsKey(marker)) {
            PointOfInterest pointOfInterest = markerPointOfInterestMap.get(marker);

            if (!freeExploration && !storyLineManager.hasVisitedPOI(pointOfInterest)) {
                return true;
            }

            if (navigationMode) {

                if (marker.equals(selectedMarker)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "This is your destination", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 250);
                    toast.show();
                } else {
                    navigationManager.selectedStart(marker);

                    PointOfInterest destinationNode = panelManager.getCurrentPointOfInterest();
                    mapManager.displayShortestPath(pointOfInterest.getId(), destinationNode.getId(), searchingExit);
                }

            } else {

                selectedMarkerDisplay(marker);
                //move camera to marker position
                LatLng markerLocation = marker.getPosition();

                mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLocation));
                panelManager.update(pointOfInterest);
            }
        }
        return true;
    }


    /**
     * Change point of interest marker color to red when selected.
     *
     * @param marker The selected marker.
     */
    private void selectedMarkerDisplay(Marker marker) {
        if (freeExploration) {
            if (selectedMarker != null) {
                selectedMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            }
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            selectedMarker = marker;
        }
    }

    private ServiceConnection audioConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioBinder binder = (AudioBinder) service;
            mediaService = binder.getAudioService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private Runnable audioRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = mediaService.getCurrentPosition();
            seekBar.setProgress(currentPosition);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (mediaService.isMediaSet() && fromUser) {
                        mediaService.setAudioPosition(progress * 1000);
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
        if (mediaService != null) {
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
        if (mediaService != null) {
            mediaService.releaseAudio();
        }
    }

    public MediaService getMediaService() {
        return mediaService;
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

    public MuseumMap getInformation() {
        return information;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public StoryLineManager getStoryLineManager() {
        return storyLineManager;
    }

    @Override
    public void onBackPressed() {
        if (panelManager.isOpen()) {
            panelManager.close();
        } else {
            this.finish();
        }
    }

    /**
     * Custom camera listener, that will verify view boundary,zoom limit, certain specific gesture
     */
    private class OnCameraChangeListener implements GoogleMap.OnCameraChangeListener {

        public static final String ON_CAMERA_CHANGE = "onCameraChange";

        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            VisibleRegion vr = mMap.getProjection().getVisibleRegion();
            double left = vr.latLngBounds.southwest.longitude;
            double top = vr.latLngBounds.northeast.latitude;
            double right = vr.latLngBounds.northeast.longitude;
            double bottom = vr.latLngBounds.southwest.latitude;

            Log.d(ON_CAMERA_CHANGE, "left :" + left);
            Log.d(ON_CAMERA_CHANGE, "top :" + top);
            Log.d(ON_CAMERA_CHANGE, "right :" + right);
            Log.d(ON_CAMERA_CHANGE, "bottom :" + bottom);

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

    public boolean isFreeExploration() {
        return freeExploration;
    }

    public void setSearchingExit(boolean searchingExit) {
        this.searchingExit = searchingExit;
    }

    public boolean isSearchingExit() {
        return searchingExit;
    }

    public void setNavigationMode(boolean navigationMode) {
        this.navigationMode = navigationMode;
    }

    public NavigationManager getNavigationManager() {
        return navigationManager;
    }

    public PoiPanelManager getPanelManager() {
        return panelManager;
    }

    public Map<Marker, PointOfInterest> getMarkerPointOfInterestMap() {
        return markerPointOfInterestMap;
    }

    public PoiPanelManager getPanel() {
        return panelManager;
    }

    public StoryLine getStoryLine() {
        return storyLine;
    }
}
