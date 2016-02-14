package com.example.nspace.museedesondes;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.nspace.museedesondes.Model.Language;
import com.example.nspace.museedesondes.Model.Map;
import com.example.nspace.museedesondes.Model.PointOfInterest;
import com.example.nspace.museedesondes.Model.Text;
import com.example.nspace.museedesondes.Utility.ViewMap;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback, NavigationDrawerFragment.NavigationDrawerCallbacks{

    private GoogleMap mMap;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GroundOverlay groundOverlay;
    private Map information;
    public static Drawable imgToSendToFullscreenImgActivity;

    //todo will need to mediaPlayer.release();  when menu is closed to release ram 
    private MediaPlayer mediaPlayer;

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

        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sampleaudio);
    }

    private void bringButtonsToFront(){
        FloatingActionButton ham = (FloatingActionButton) findViewById(R.id.hamburger);
        FloatingActionButton search = (FloatingActionButton) findViewById(R.id.search_button);
        FloatingActionMenu floor = (FloatingActionMenu) findViewById(R.id.floor_button);

        ham.bringToFront();
        search.bringToFront();
        floor.bringToFront();

    }

    public void onHamClick(View v){
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
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        //// TODO: 2/7/2016  need to get the lat/lng of each map et bound the available view screen
//        final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(0.027,-0.02), new LatLng(41.9667, 12.5938));
//        final int MAX_ZOOM = 16;
//        final int MIN_ZOOM = 13;


        LatLng custom = new LatLng(0.027, -0.02);
        LatLng center = new LatLng(0, 0);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 14));
        mMap.clear();



        //load map and then switch floor to 5
       // GroundOverlay groundOverlay = ViewMap.loadDefaultFloor(mMap, custom);
        groundOverlay = ViewMap.loadDefaultFloor(mMap, custom, information.getFloorPlans(), getApplicationContext());
        //need to implement a list view
        //ViewMap.switchFloor(groundOverlay, 5);




        //// TODO: 2/7/2016 refactor this in a proper fuction
        PointOfInterest pointOfInterest = information.getPointOfInterests().get(0);

        String title = "error";
        String snippet = "error";
        for(Text text : pointOfInterest.getText())
        {
            if(getApplicationContext().getResources().getConfiguration().locale.getLanguage().equals(text.getLanguage().name().toLowerCase()))
            {
                title = text.getLanguage().name();
            }
        }
        for(Text text : pointOfInterest.getText())
        {
            if(getApplicationContext().getResources().getConfiguration().locale.getLanguage().equals(text.getLanguage().name().toLowerCase()))
            {
                snippet = text.getContent();
            }
        }


        //single marker with value from json
        MarkerOptions node = new MarkerOptions();
        node.position(new LatLng(pointOfInterest.getX(), pointOfInterest.getY()));
        node.title(title);
        node.snippet(snippet);
        node.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.addMarker(node);


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    //HANDLERS ************

    public void poiImgOnClick(View v){
        imgToSendToFullscreenImgActivity = ((ImageView)v).getDrawable();
        Intent fullscreenImgActivity = new Intent(MapActivity.this, FullscreenImgActivity.class);
        startActivity(fullscreenImgActivity);
    }

    public void floorButton1OnClick(View v){
        changeFloor(1);
    }
    public void floorButton2OnClick(View v){
        changeFloor(2);
    }
    public void floorButton3OnClick(View v){
        changeFloor(3);
    }
    public void floorButton4OnClick(View v){
        changeFloor(4);
    }

    public void changeFloor(int floor){
        ViewMap.switchFloor(groundOverlay, floor, information.getFloorPlans(), getApplicationContext() );
        FloatingActionMenu floorButton = (FloatingActionMenu) findViewById(R.id.floor_button);
        floorButton.toggle(true);
    }

    public void playAudioFile(View v){
        Button play = (Button) findViewById(R.id.play_button);
        ViewGroup layout = (ViewGroup)play.getParent();


        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            v.setBackgroundResource(R.drawable.ic_play_circle_filled_white_48dp);
        } else {
            mediaPlayer.start();
            v.setBackgroundResource(R.drawable.ic_pause_circle_filled_white_48dp);
        }

    }


}
