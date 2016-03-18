package com.example.nspace.museedesondes.utility;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.example.nspace.museedesondes.AudioService;
import com.example.nspace.museedesondes.MapActivity;
import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.model.Image;
import com.example.nspace.museedesondes.model.Language;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.example.nspace.museedesondes.model.StoryLine;
import com.example.nspace.museedesondes.utility.HorizontalRecycleViewAdapter;
import com.example.nspace.museedesondes.utility.PointMarker;
import com.google.android.gms.maps.model.Marker;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.nspace.museedesondes.R.id.poi_title;

/**
 * Created by Lenovo on 2/18/2016.
 */
public class PoiPanel {

    private MapActivity activity;
    private SlidingUpPanelLayout panel;
    private PointOfInterest currentPointofInterest;
    private int selectedImageId;

    public PoiPanel(MapActivity activity) {
        this.activity = activity;
        this.panel = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);

        onShadowClick();

    }

    private void onShadowClick() {
        panel.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    public void update(Marker marker){



        PointMarker.Information pMarkerInfo = new PointMarker.Information(marker.getSnippet());

        PointOfInterest pointOfInterest = activity.getInformation().searchPoiById(pMarkerInfo.getNodeID());
        this.currentPointofInterest = pointOfInterest;
        Log.v("test",activity.getResources().getConfiguration().locale.getLanguage());
        String description = pointOfInterest.getLocaleDescription(activity.getApplicationContext()).getDescription();
        String title = pointOfInterest.getLocaleDescription(activity.getApplicationContext()).getTitle();
        List<Image> images = pointOfInterest.getLocaleImages(activity.getApplicationContext());


        //todo if no image, remove layout
//       if (currentPointofInterest.getLocaleImages(activity.getApplicationContext()).isEmpty())
//        {
//            RelativeLayout v = (RelativeLayout) activity.findViewById(R.id.poiPanel);
//            v.removeView(activity.findViewById(R.id.my_recycler_view));
//            Log.v("test");
//        }

        replaceTitle(title);
        replaceDescription(description);
        replacePics(images);

        panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void updateStoryPanel(StoryLine storyLine, PointOfInterest pointOfInterest){

        this.currentPointofInterest = pointOfInterest;
        String description = pointOfInterest.getStoryRelatedDescription(storyLine.getId(), activity.getApplicationContext()).getDescription();
        String title = pointOfInterest.getStoryRelatedDescription(storyLine.getId(), activity.getApplicationContext()).getTitle();
        List<Image> images = pointOfInterest.getLocaleImages(activity.getApplicationContext());

        replaceTitle(title);
        replaceDescription(description);
        replacePics(images);
        activity.startAudio(currentPointofInterest);


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

    private void replacePics(List<Image> images){
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        HorizontalRecycleViewAdapter adapter = new HorizontalRecycleViewAdapter(activity, images);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }

    public PointOfInterest getCurrentPointofInterest() {
        return currentPointofInterest;
    }

    public boolean isOpen()
    {
        return panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED;
    }
    public  void close()
    {
        panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    /**
     * Set the selected image resource reference
     *
     * @param v
     */
    public void setSelectedImage(View v)
    {
        ImageView selectedImage = ((ImageView) v.findViewById(R.id.poi_panel_pic_item_imageview));
        selectedImageId = Integer.parseInt(selectedImage.getTag().toString());
    }

    public int getSelectedImageId() {
        return selectedImageId;
    }
}
