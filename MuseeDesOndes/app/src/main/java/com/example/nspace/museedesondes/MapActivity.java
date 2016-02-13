package com.example.nspace.museedesondes;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.google.android.gms.maps.model.Polyline;

public class MapActivity extends ActionBarActivity implements OnMapReadyCallback, NavigationDrawerFragment.NavigationDrawerCallbacks{

    private GoogleMap mMap;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GroundOverlay groundOverlay;
    public static Drawable imgToSendToFullscreenImgActivity;

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
        Map information = Map.getInstance(getApplicationContext());

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
        groundOverlay = ViewMap.loadDefaultFloor(mMap, custom);
        //need to implement a list view
        //ViewMap.switchFloor(groundOverlay, 5);




        //// TODO: 2/7/2016 refactor this in a proper fuction
        PointOfInterest pointOfInterest = information.getPointOfInterests().get(0);

        String title = "error";
        String snippet = "error";
        for(Language language : pointOfInterest.getName())
        {
            if(getApplicationContext().getResources().getConfiguration().locale.getLanguage().equals(language.getLanguage()))
            {
                title = language.getData();
            }
        }
        for(Language language : pointOfInterest.getDescription())
        {
            if(getApplicationContext().getResources().getConfiguration().locale.getLanguage().equals(language.getLanguage()))
            {
                snippet = language.getData();
            }
        }


        //single marker with value from json
        MarkerOptions node = new MarkerOptions();
        node.position(new LatLng(pointOfInterest.getCoordinate().getX(), pointOfInterest.getCoordinate().getY()));
        node.title(title);
        node.snippet(snippet);
        node.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.addMarker(node);

        // This is a testing point. TO BE REMOVED Harrison Ianatchkov Feb 12, 2016
        MarkerOptions anotherOne = new MarkerOptions();
        anotherOne.position(new LatLng(0.001, 0.01));
        anotherOne.title("Waguan");
        anotherOne.snippet(snippet);
        anotherOne.icon((BitmapDescriptorFactory.defaultMarker((BitmapDescriptorFactory.HUE_AZURE))));
        mMap.addMarker(anotherOne);

        // This is a testing point. TO BE REMOVED Harrison Ianatchkov Feb 12, 2016
        MarkerOptions andAnotherOne1 = new MarkerOptions();
        andAnotherOne1.position(new LatLng(0.015, 0.01));
        andAnotherOne1.title("Waguan");
        andAnotherOne1.snippet(snippet);

        // This is a testing point. TO BE REMOVED Harrison Ianatchkov Feb 12, 2016
        MarkerOptions andAnotherOne2 = new MarkerOptions();
        andAnotherOne2.position(new LatLng(0.015, -0.004));
        andAnotherOne2.title("Waguan");
        andAnotherOne2.snippet(snippet);

        
        // This is a testing the creation of a polyline between two points. Harrison Ianatchkov Feb 12, 2016
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(node.getPosition(), andAnotherOne2.getPosition())
                .add(andAnotherOne2.getPosition(), andAnotherOne1.getPosition())
                .add(andAnotherOne1.getPosition(), anotherOne.getPosition())
                .width(15)
                .color(Color.parseColor("#FFFE7070")));




        
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
        ViewMap.switchFloor(groundOverlay, floor);
        FloatingActionMenu floorButton = (FloatingActionMenu) findViewById(R.id.floor_button);
        floorButton.toggle(true);
    }


}
