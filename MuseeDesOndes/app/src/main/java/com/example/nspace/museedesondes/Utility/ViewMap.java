package com.example.nspace.museedesondes.Utility;

import com.example.nspace.museedesondes.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by michal on 2/10/2016.
 */
public class ViewMap {

    public static GroundOverlay loadDefaultFloor(GoogleMap googleMap, LatLng position)
    {
        int imageRessource;


        BitmapDescriptor image = BitmapDescriptorFactory.fromResource(R.drawable.floor_1_rca_march2011_1);


        GroundOverlayOptions customMap = new GroundOverlayOptions()
                .image(image)
                .position(position, 5520f, 10704f).anchor(0, 0);
        GroundOverlay groundOverlay = googleMap.addGroundOverlay(customMap);

        return groundOverlay;

    }

    /**
     * @param groundOverlay pass the same groundOverlay from defaultFloorMethod; this keep all image customisation
     * @param floorID floor number
     */
    public static void switchFloor(GroundOverlay groundOverlay, int floorID)
    {
        int ressource = 1;

        switch (floorID)
        {
            case 1: ressource = R.drawable.floor_1_rca_march2011_1;
                break;
            case 2: ressource = R.drawable.floor_2_rca_march2011_1;
                break;
            case 3: ressource = R.drawable.floor_3_rca_march2011_1;
                break;
            case 4: ressource = R.drawable.floor_4_rca_march2011_1;
                break;
            case 5: ressource = R.drawable.floor_5_rca_march2011_1;
                break;
        }

        groundOverlay.setImage(BitmapDescriptorFactory.fromResource(ressource));
    }




}
