package com.example.nspace.museedesondes;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import com.example.nspace.museedesondes.Model.Language;
import com.example.nspace.museedesondes.Model.Map;
import com.example.nspace.museedesondes.Model.PointOfInterest;
import com.example.nspace.museedesondes.Utility.ViewMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback, NavigationDrawerFragment.NavigationDrawerCallbacks{

    private GoogleMap mMap;
    private NavigationDrawerFragment mNavigationDrawerFragment;

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
        Button ham = (Button) findViewById(R.id.hamburger);
        Button search = (Button) findViewById(R.id.search_button);
        Button floor = (Button) findViewById(R.id.floor_button);
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
        GroundOverlay groundOverlay = ViewMap.loadDefaultFloor(mMap, custom);
        //need to implement a list view
        ViewMap.switchFloor(groundOverlay, 5);




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


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }


}
