package com.example.nspace.museedesondes;

import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import static com.example.nspace.museedesondes.R.id.poi_title;

/**
 * Created by Lenovo on 2/18/2016.
 */
public class PoiPanel {

    public static void replaceDescription(SlidingUpPanelLayout panel, String description){
        DocumentView docView = (DocumentView) panel.findViewById(R.id.poi_text);
        docView.setText(description);
    }

    public static void replaceTitle(SlidingUpPanelLayout panel, String title){
        TextView docView = (TextView) panel.findViewById(poi_title);
        docView.setText(title);
    }
}
