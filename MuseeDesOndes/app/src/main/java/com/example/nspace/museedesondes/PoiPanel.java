package com.example.nspace.museedesondes;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.example.nspace.museedesondes.Model.Image;
import com.example.nspace.museedesondes.Model.Language;
import com.example.nspace.museedesondes.Model.PointOfInterest;
import com.example.nspace.museedesondes.Model.StoryLine;
import com.example.nspace.museedesondes.Utility.HorizontalRecycleViewAdapter;
import com.example.nspace.museedesondes.Utility.PointMarker;
import com.google.android.gms.maps.model.Marker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import static com.example.nspace.museedesondes.R.id.poi_title;

/**
 * Created by Lenovo on 2/18/2016.
 */
public class PoiPanel {

    MapActivity activity;
    SlidingUpPanelLayout panel;

    public PoiPanel(MapActivity activity) {
        this.activity = activity;
        this.panel = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
    }

    public void update(Marker marker){

        PointMarker.Information pMarkerInfo = new PointMarker.Information(marker.getSnippet());

        PointOfInterest pointOfInterest = activity.information.searchPoiById(pMarkerInfo.getNodeID());
        String description = pointOfInterest.getLocaleDescription(activity.getApplicationContext()).getDescription();
        String title = pointOfInterest.getLocaleDescription(activity.getApplicationContext()).getTitle();
        ArrayList<Image> images = pointOfInterest.getLocaleImages(activity.getApplicationContext());

        //get test images:
        images = new ArrayList<>();
        images.add(new Image("museum_ex_1", Language.en_US, "museum_ex_1"));
        images.add(new Image("museum_ex_2", Language.en_US, "museum_ex_2"));

        replaceTitle(title);
        replaceDescription(description);
        replacePics(images);

        panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void updateStoryPanel(StoryLine storyLine, PointOfInterest pointOfInterest){


        String description = pointOfInterest.getStoryRelatedDescription(storyLine.getId(), activity.getApplicationContext()).getDescription();
        String title = pointOfInterest.getStoryRelatedDescription(storyLine.getId(), activity.getApplicationContext()).getTitle();
        ArrayList<Image> images = pointOfInterest.getLocaleImages(activity.getApplicationContext());

        //get test images:
        images = new ArrayList<>();
        images.add(new Image("museum_ex_1", Language.en_US, "museum_ex_1"));
        images.add(new Image("museum_ex_2", Language.en_US, "museum_ex_2"));

        replaceTitle(title);
        replaceDescription(description);
        replacePics(images);

        panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    private void replaceDescription(String description){
        DocumentView docView = (DocumentView) panel.findViewById(R.id.poi_text);
        docView.setText(description);
    }

    private void replaceTitle(String title){
        TextView docView = (TextView) panel.findViewById(poi_title);
        docView.setText(title);
    }

    private void replacePics(ArrayList<Image> images){
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        HorizontalRecycleViewAdapter adapter = new HorizontalRecycleViewAdapter(activity, images);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

}
