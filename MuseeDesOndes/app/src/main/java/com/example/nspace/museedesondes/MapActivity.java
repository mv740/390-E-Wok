package com.example.nspace.museedesondes;


import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.nspace.museedesondes.Model.Language;
import com.example.nspace.museedesondes.Model.Map;
import com.example.nspace.museedesondes.Model.PointOfInterest;
import com.example.nspace.museedesondes.Utility.ViewMap;
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
        final Button floor = (Button) findViewById(R.id.floor_button);

        //floor menu ... not sure if it is the best way,  listview of button or menu with a icon that will similar to  our floor button
        floor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MapActivity.this, floor);

                //PopupMenu with icons  http://stackoverflow.com/questions/15454995/popupmenu-with-icons
//                try {
//                    Field field = popup.getClass().getDeclaredField("mPopup");
//                    field.setAccessible(true);
//                    Object menuPopupHelper = field.get(popup);
//                    Class<?> cls = Class.forName("com.android.internal.view.menu.MenuPopupHelper");
//                    Method method = cls.getDeclaredMethod("setForceShowIcon", new Class[]{boolean.class});
//                    method.setAccessible(true);
//                    method.invoke(menuPopupHelper, new Object[]{true});
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.floor, popup.getMenu());


                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        CharSequence id = item.getTitle();

                        ViewMap.switchFloor(groundOverlay, Integer.parseInt(id.toString()));
                        Toast.makeText(
                                MapActivity.this,
                                "You Clicked : " + item.getTitle() + " id: " + item.getItemId(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });


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


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }


}
