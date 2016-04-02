package com.example.nspace.museedesondes.utility;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.example.nspace.museedesondes.MapActivity;
import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.adapters.HorizontalRecycleViewAdapter;
import com.example.nspace.museedesondes.model.Image;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.example.nspace.museedesondes.model.StoryLine;
import com.example.nspace.museedesondes.model.Video;
import com.github.clans.fab.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import static com.example.nspace.museedesondes.R.id.poi_title;

/**
 * Created by Lenovo on 2/18/2016.
 */
public class PoiPanelManager implements POIBeaconListener {

    private MapActivity activity;
    private SlidingUpPanelLayout panel;
    private PointOfInterest currentPointOfInterest;
    private int selectedImageId;
    private RelativeLayout poiPanelLayout;
    private FloatingActionButton navigationButton;

    public PoiPanelManager(MapActivity activity) {
        this.activity = activity;
        this.panel = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
        this.poiPanelLayout = (RelativeLayout) activity.findViewById(R.id.poiPanel);
        this.navigationButton = (FloatingActionButton) activity.findViewById(R.id.get_directions_button);

        onShadowClick();
        onStateChange();

    }



    private void onStateChange() {
        panel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                Log.e("Statechanged", "yes");
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    if (activity.getNavigationManager().isEndTour()) {
                        activity.getStoryLineManager().endOfTourDialog();
                    }
                    if (activity.getMediaService() != null) {
                        if(activity.getMediaService().isPlaying())
                        {
                            activity.getMediaService().releaseAudio();
                        }
                        Button playAudio = (Button) activity.findViewById(R.id.play_button);
                        playAudio.setBackgroundResource(R.drawable.ic_play_circle_filled_white_48dp);
                    }
                }
            }
        });


    }

    private void onShadowClick() {
        panel.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                if (activity.getNavigationManager().isEndTour()) {
                    activity.getStoryLineManager().endOfTourDialog();
                }
            }
        });
    }

    public void update(PointOfInterest pointOfInterest) {

        this.currentPointOfInterest = pointOfInterest;
        Log.v("test", activity.getResources().getConfiguration().locale.getLanguage());
        String description = pointOfInterest.getLocaleDescription(activity.getApplicationContext()).getDescription();
        String title = pointOfInterest.getLocaleDescription(activity.getApplicationContext()).getTitle();
        List<Image> images = pointOfInterest.getLocaleImages(activity.getApplicationContext());
        List<Video> videos = pointOfInterest.getLocaleVideos(activity.getApplicationContext());

        replaceTitle(title);
        replaceDescription(description);
        updateMedia(images, videos);


    }

    public void slideUp(){
        panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void onPOIBeaconDiscovered(PointOfInterest pointOfInterest, StoryLine storyLine) {

        this.currentPointOfInterest = pointOfInterest;
        String description = pointOfInterest.getStoryRelatedDescription(storyLine.getId(), activity.getApplicationContext()).getDescription();
        String title = pointOfInterest.getStoryRelatedDescription(storyLine.getId(), activity.getApplicationContext()).getTitle();
        List<Image> images = pointOfInterest.getStoryRelatedImages(storyLine.getId(), activity.getApplicationContext());
        List<Video> videos = pointOfInterest.getStoryRelatedVideos(storyLine.getId(), activity.getApplicationContext());

        replaceTitle(title);
        replaceDescription(description);
        updateMedia(images, videos);
        activity.startAudio(currentPointOfInterest);


        panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

        setVisibility(View.VISIBLE);
    }

    private void replaceDescription(String description) {
        DocumentView docView = (DocumentView) panel.findViewById(R.id.poi_text);
        docView.setText(Html.fromHtml(description));
    }

    public void replaceTitle(String title) {
        TextView docView = (TextView) panel.findViewById(poi_title);
        docView.setText(Html.fromHtml(title));
    }

    public String getTitle() {
        TextView docView = (TextView) panel.findViewById(poi_title);
        return docView.getText().toString();
    }

    private void updateMedia(List<Image> images, List<Video> videos) {
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        if (images.size() + videos.size() == 0){
            recyclerView.setVisibility(View.GONE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            HorizontalRecycleViewAdapter adapter = new HorizontalRecycleViewAdapter(activity, images, videos);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);
        }
    }

    public PointOfInterest getCurrentPointOfInterest() {
        return currentPointOfInterest;
    }

    public boolean isOpen() {
        return panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED;
    }

    public void close() {
        panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    /**
     * Set the selected image resource reference
     *
     * @param v
     */
    public void setSelectedImage(View v) {
        ImageView selectedImage = (ImageView) v.findViewById(R.id.poi_panel_pic_item_imageview);
        selectedImageId = Integer.parseInt(selectedImage.getTag().toString());
    }

    public int getSelectedImageId() {
        return selectedImageId;
    }

    public SlidingUpPanelLayout getPanel() {
        return panel;
    }

    public RelativeLayout getPoiPanelLayout() {
        return poiPanelLayout;
    }

    public void setVisibility(int visibility){
        poiPanelLayout.setVisibility(visibility);
    }


    public FloatingActionButton getNavigationButton() {
        return navigationButton;
    }

    public MapActivity getActivity() {
        return activity;
    }
}
