package com.example.nspace.museedesondes;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.example.nspace.museedesondes.AudioService.AudioBinder;


import com.example.nspace.museedesondes.Model.Map;
import com.example.nspace.museedesondes.Model.PointOfInterest;
import com.example.nspace.museedesondes.Utility.PointMarker;
import com.example.nspace.museedesondes.Utility.ViewMap;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.gms.maps.model.Polyline;
import com.example.nspace.museedesondes.Model.Node;
import java.util.ArrayList;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;


public class MapActivity extends ActionBarActivity implements OnMapReadyCallback, NavigationDrawerFragment.NavigationDrawerCallbacks, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private GroundOverlay groundOverlay;
    private Map information;
    public static Drawable imgToSendToFullscreenImgActivity;
    AudioService audioService;



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
        Intent intent = new Intent(this,AudioService.class);
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
        // GroundOverlay groundOverlay = ViewMap.loadDefaultFloor(mMap, custom);
        groundOverlay = ViewMap.loadDefaultFloor(mMap, custom, information.getFloorPlans(), getApplicationContext(), findViewById(android.R.id.content));
        //need to implement a list view
        //ViewMap.switchFloor(groundOverlay, 5);


        //// TODO: 2/7/2016 refactor this in a proper fuction
        PointOfInterest pointOfInterest = information.getPointOfInterests().get(0);
        PointMarker.singleInterestPointFactory(pointOfInterest, getApplicationContext(), mMap);
        PoiPanel.replaceText((SlidingUpPanelLayout) findViewById(R.id.sliding_layout), "SALUT");


        // Obtains ALL nodes.
        ArrayList<Node> nodes = information.getNodes();

        // This statement places all the nodes on the map and traces the path between them.
        tracePath(nodes);


    }

    /**
     * This function is meant to trace the path between nodes in the arraylist of coordinates
     * representing each node's latitudinal and longitudinal position respectively.
     * @param nodes This is the list of nodes that are to be sorted through. The nodes could be
     *              either points of interest, points of traversal, or others.
     */
    public void tracePath(ArrayList<Node> nodes) {

        ArrayList<LatLng> nodePositions = listNodeCoordinates(nodes);
        Polyline line = null;
        for(int i = 0; i < nodePositions.size() - 1; i++) {
            line = mMap.addPolyline(new PolylineOptions()
                    .add(nodePositions.get(i), nodePositions.get(i + 1))
                    .width(15)
                    .color(Color.parseColor("#99E33C3C")));
        }
        line.setPoints(nodePositions);
    }


    /**
     * This method is used to return a list of LatLng coordinates associated with the list of nodes passed as a parameter.
     * @param nodes   The list of nodes for which coordinates should be derived.
     * @return        The list of LatLng coordinates.
     */
    public ArrayList<LatLng> listNodeCoordinates(ArrayList<Node> nodes) {
        if (nodes == null) {
            return null;
        }

        ArrayList<LatLng> nodeLatLngs = new ArrayList<LatLng>();
        for (Node node: nodes) {
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

    public void floorButton1OnClick(View v) {
        changeFloor(1);
        FloatingActionButton floorSelected =  (FloatingActionButton)this.findViewById(R.id.fab1);
        floorSelected.setColorNormal(this.getResources().getColor(R.color.rca_primary));
        FloatingActionButton floor5 =  (FloatingActionButton)this.findViewById(R.id.fab5);
        FloatingActionButton floor3 =  (FloatingActionButton)this.findViewById(R.id.fab3);
        FloatingActionButton floor4 =  (FloatingActionButton)this.findViewById(R.id.fab4);
        FloatingActionButton floor2 =  (FloatingActionButton)this.findViewById(R.id.fab2);
        
        floor5.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor2.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor3.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor4.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
    }

    public void floorButton2OnClick(View v) {
        changeFloor(2);
        FloatingActionButton floorSelected =  (FloatingActionButton)this.findViewById(R.id.fab2);
        floorSelected.setColorNormal(Color.parseColor("#FFE33C3C"));
        FloatingActionButton floor1 =  (FloatingActionButton)this.findViewById(R.id.fab1);
        FloatingActionButton floor3 =  (FloatingActionButton)this.findViewById(R.id.fab3);
        FloatingActionButton floor4 =  (FloatingActionButton)this.findViewById(R.id.fab4);
        FloatingActionButton floor5 =  (FloatingActionButton)this.findViewById(R.id.fab5);
        floor1.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor5.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor3.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor4.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
    }


    public void floorButton3OnClick(View v) {
        changeFloor(3);
        FloatingActionButton floorSelected =  (FloatingActionButton)this.findViewById(R.id.fab3);
        floorSelected.setColorNormal(Color.parseColor("#FFE33C3C"));
        FloatingActionButton floor1 =  (FloatingActionButton)this.findViewById(R.id.fab1);
        FloatingActionButton floor5 =  (FloatingActionButton)this.findViewById(R.id.fab5);
        FloatingActionButton floor4 =  (FloatingActionButton)this.findViewById(R.id.fab4);
        FloatingActionButton floor2 =  (FloatingActionButton)this.findViewById(R.id.fab2);
        floor1.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor2.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor5.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor4.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
    }


    public void floorButton4OnClick(View v) {
        changeFloor(4);
        FloatingActionButton floorSelected =  (FloatingActionButton)this.findViewById(R.id.fab4);
        floorSelected.setColorNormal(Color.parseColor("#FFE33C3C"));
        FloatingActionButton floor1 =  (FloatingActionButton)this.findViewById(R.id.fab1);
        FloatingActionButton floor3 =  (FloatingActionButton)this.findViewById(R.id.fab3);
        FloatingActionButton floor5 =  (FloatingActionButton)this.findViewById(R.id.fab5);
        FloatingActionButton floor2 =  (FloatingActionButton)this.findViewById(R.id.fab2);
        floor1.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor2.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor3.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor5.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
    }
    public void floorButton5OnClick(View v) {
        changeFloor(5);
        FloatingActionButton floorSelected =  (FloatingActionButton)this.findViewById(R.id.fab5);
        floorSelected.setColorNormal(Color.parseColor("#FFE33C3C"));
        FloatingActionButton floor1 =  (FloatingActionButton)this.findViewById(R.id.fab1);
        FloatingActionButton floor3 =  (FloatingActionButton)this.findViewById(R.id.fab3);
        FloatingActionButton floor4 =  (FloatingActionButton)this.findViewById(R.id.fab4);
        FloatingActionButton floor2 =  (FloatingActionButton)this.findViewById(R.id.fab2);
        floor1.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor2.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor3.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
        floor4.setColorNormal(this.getResources().getColor(R.color.rca_onclick));
    }

    public void changeFloor(int floor) {
        ViewMap.switchFloor(groundOverlay, floor, information.getFloorPlans(), getApplicationContext());
        FloatingActionMenu floorButton = (FloatingActionMenu) findViewById(R.id.floor_button);
        floorButton.toggle(true);
    }

    public void playAudioFile(View v) {
        audioService.toggleAudioOnOff(v);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getTitle().equals("fr") || marker.getTitle().equalsIgnoreCase("en_us"))
        {
            SlidingUpPanelLayout  layout = (SlidingUpPanelLayout) this.findViewById(R.id.sliding_layout);
            layout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }
        return false;
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
