package com.example.nspace.museedesondes;

import com.bluejamesbond.text.DocumentView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by Lenovo on 2/18/2016.
 */
public class PoiPanel {

    public static void replaceText(SlidingUpPanelLayout panel, String text){
        DocumentView docView = (DocumentView) panel.findViewById(R.id.poi_text);
        docView.setText(text);
    }

}
