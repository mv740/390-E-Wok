package com.example.nspace.museedesondes.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;

import com.example.nspace.museedesondes.Model.FloorPlan;
import com.example.nspace.museedesondes.R;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by michal on 2/10/2016.
 */
public class ViewMap {


    public static GroundOverlay loadDefaultFloor(GoogleMap googleMap, LatLng position,ArrayList<FloorPlan> floorPlans, Context context, View view)
    {
        Resources resources = context.getResources();
        final int resourceID = resources.getIdentifier(floorPlans.get(0).getImagePath(), "drawable", context.getPackageName()); // 0 = floor 1
        FloatingActionButton floorSelected =  (FloatingActionButton)view.findViewById(R.id.fab1);
        floorSelected.setColorNormal(Color.parseColor("#FFFFA8A8"));

        BitmapDescriptor image = BitmapDescriptorFactory.fromResource(resourceID);


        GroundOverlayOptions customMap = new GroundOverlayOptions()
                .image(image)
                .position(position, 5520f, 10704f).anchor(0, 0);

        return googleMap.addGroundOverlay(customMap);

    }

    /**
     * @param groundOverlay pass the same groundOverlay from defaultFloorMethod; this keep all image customisation
     * @param floorID floor number
     * @param floorPlans
     */
    public static void switchFloor(GroundOverlay groundOverlay, int floorID, ArrayList<FloorPlan> floorPlans, Context context)
    {
        //http://stackoverflow.com/questions/16369814/how-to-access-the-drawable-resources-by-name-in-android
        int index = floorID-1; //Todo if floor object aren't in order then we will need to loop to find the correct one by id

        Resources resources = context.getResources();
        final int resourceID = resources.getIdentifier(floorPlans.get(index).getImagePath(),"drawable",context.getPackageName());

        groundOverlay.setImage(BitmapDescriptorFactory.fromResource(resourceID));
    }




}
