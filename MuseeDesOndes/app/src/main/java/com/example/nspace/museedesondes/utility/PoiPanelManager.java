package com.example.nspace.museedesondes.utility;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private String selectedImageFilePath;
    private RelativeLayout poiPanelLayout;
    private FloatingActionButton navigationButton;
    private boolean endTourDialogueShown;

    public PoiPanelManager(MapActivity activity) {
        this.activity = activity;
        this.panel = (SlidingUpPanelLayout) activity.findViewById(R.id.sliding_layout);
        this.poiPanelLayout = (RelativeLayout) activity.findViewById(R.id.poiPanel);
        this.navigationButton = (FloatingActionButton) activity.findViewById(R.id.get_directions_button);

        onShadowClick();
        onStateChange();

        //hide panel
        panel.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        activity.findViewById(R.id.get_directions_button).setVisibility(View.INVISIBLE);
        activity.findViewById(R.id.audioPlayer).setVisibility(View.GONE);
        activity.findViewById(R.id.my_recycler_view).setVisibility(View.GONE);
    }


    private void onStateChange() {
        panel.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                //after first point of interest, always display
                if (previousState == SlidingUpPanelLayout.PanelState.HIDDEN)
                    if (activity.isFreeExploration()) {
                        activity.findViewById(R.id.get_directions_button).setVisibility(View.VISIBLE);
                    }

                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED) {

                    if (activity.getNavigationManager().isEndTour() && !endTourDialogueShown) {
                        activity.getStoryLineManager().endOfTourDialog();
                        endTourDialogueShown = true;
                    }
                    if (activity.getMediaService() != null) {

                        activity.getMediaService().releaseAudio();

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
                if (activity.getNavigationManager().isEndTour() && !endTourDialogueShown) {
                    activity.getStoryLineManager().endOfTourDialog();
                    endTourDialogueShown = true;
                }
            }
        });
    }

    public void update(PointOfInterest pointOfInterest) {

        this.currentPointOfInterest = pointOfInterest;
        Log.v("test", activity.getResources().getConfiguration().locale.getLanguage());

        String description = "";
        String title = "";
        List<Image> images = null;
        List<Video> videos = null;
        if(activity.isFreeExploration())
        {

            description = pointOfInterest.getLocaleDescription(activity.getApplicationContext()).getDescription();
            title = pointOfInterest.getLocaleDescription(activity.getApplicationContext()).getTitle();
            images = pointOfInterest.getLocaleImages(activity.getApplicationContext());
            videos = pointOfInterest.getLocaleVideos(activity.getApplicationContext());

        }else
        {
            StoryLine storyLine = activity.getStoryLine();
            description = pointOfInterest.getStoryRelatedDescription(storyLine.getId(), activity.getApplicationContext()).getDescription();
            title = pointOfInterest.getStoryRelatedDescription(storyLine.getId(), activity.getApplicationContext()).getTitle();
            images = pointOfInterest.getStoryRelatedImages(storyLine.getId(), activity.getApplicationContext());
            videos = pointOfInterest.getStoryRelatedVideos(storyLine.getId(), activity.getApplicationContext());
        }


        if (doesMediaExist(videos, images)) {
            Log.e("exist", "yes");
            activity.findViewById(R.id.my_recycler_view).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.my_recycler_view).setVisibility(View.INVISIBLE);
        }
        replaceTitle(title);
        replaceDescription(description);
        updateMedia(images, videos);
        if(activity.isFreeExploration())
        {
            doesAudioExist(currentPointOfInterest);
        }else
        {
            doesAudioStoryExist(currentPointOfInterest,activity.getStoryLine());
        }

        delaySlideUp();
    }

    private void startVideo(final RecyclerView view) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                view.getChildAt(0).callOnClick();
            }
        }, 500);
    }

    private void delaySlideUp() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                slideUp();
            }
        }, 250);
    }

    private void delayedAStartAudio(final PointOfInterest currentPointOfInterest) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.startAudio(currentPointOfInterest);
            }
        }, 500);
    }

    private void delayedAStartVideo(final RecyclerView view) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startVideo(view);
            }
        }, 500);
    }

    private boolean doesAudioExist(PointOfInterest pointOfInterest) {
        RelativeLayout audioLayout = (RelativeLayout) activity.findViewById(R.id.audioPlayer);
        if (pointOfInterest.getLocaleAudios(activity.getApplication()).size() == 0) {
            audioLayout.setVisibility(View.GONE);
            return false;
        } else {
            EditText audioName = (EditText) activity.findViewById(R.id.audioPlayerName);
            audioName.setText(Resource.getFileNameWithoutExtension(pointOfInterest.getLocaleAudios(activity.getApplicationContext()).get(0).getPath()));
            audioLayout.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private boolean doesAudioStoryExist(PointOfInterest pointOfInterest, StoryLine storyLine) {
        RelativeLayout audioLayout = (RelativeLayout) activity.findViewById(R.id.audioPlayer);
        if (pointOfInterest.getStoryRelatedAudios(storyLine.getId(),activity.getApplication()).size() == 0) {
            audioLayout.setVisibility(View.GONE);
            return false;
        } else {
            EditText audioName = (EditText) activity.findViewById(R.id.audioPlayerName);
            audioName.setText(Resource.getFileNameWithoutExtension(pointOfInterest.getStoryRelatedAudios(activity.getStoryLine().getId(),activity.getApplicationContext()).get(0).getPath()));
            audioLayout.setVisibility(View.VISIBLE);
            return true;
        }
    }

    public void slideUp() {
        panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void onPOIBeaconDiscovered(PointOfInterest pointOfInterest, StoryLine storyLine) {

        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(activity.getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.currentPointOfInterest = pointOfInterest;
        String description = pointOfInterest.getStoryRelatedDescription(storyLine.getId(), activity.getApplicationContext()).getDescription();
        String title = pointOfInterest.getStoryRelatedDescription(storyLine.getId(), activity.getApplicationContext()).getTitle();
        List<Image> images = pointOfInterest.getStoryRelatedImages(storyLine.getId(), activity.getApplicationContext());
        List<Video> videos = pointOfInterest.getStoryRelatedVideos(storyLine.getId(), activity.getApplicationContext());

        if (doesMediaExist(videos, images)) {
            activity.findViewById(R.id.my_recycler_view).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.my_recycler_view).setVisibility(View.INVISIBLE);
        }
        boolean audioExist = doesAudioStoryExist(currentPointOfInterest,storyLine);
        replaceTitle(title);
        replaceDescription(description);
        final RecyclerView view = updateMedia(images, videos);
        delaySlideUp();

        if (videos.size() > 0) {
            delayedAStartVideo(view);
        } else if (audioExist) {
            delayedAStartAudio(currentPointOfInterest);
        }

    }

    private boolean doesMediaExist(List<Video> videoList, List<Image> imageList) {
        boolean exist = false;
        if(videoList != null)
        {
           exist =  videoList.size() > 0;
        }
        if(imageList !=null && exist == false)
        {
            exist = imageList.size()>0;
        }
        Log.e("exist", String.valueOf(exist));
        return exist;
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

    private RecyclerView updateMedia(List<Image> images, List<Video> videos) {
        RecyclerView recyclerView = (RecyclerView) activity.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        if (images.size() + videos.size() == 0) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            HorizontalRecycleViewAdapter adapter = new HorizontalRecycleViewAdapter(activity, images, videos);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);

            return recyclerView;
        }
        return null;
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
        //selectedImageId = Integer.parseInt(selectedImage.getTag().toString());
        selectedImageFilePath = String.valueOf(selectedImage.getTag());
    }


    public SlidingUpPanelLayout getPanel() {
        return panel;
    }

    public RelativeLayout getPoiPanelLayout() {
        return poiPanelLayout;
    }

    public void setVisibility(int visibility) {
        poiPanelLayout.setVisibility(visibility);
    }


    public FloatingActionButton getNavigationButton() {
        return navigationButton;
    }

    public MapActivity getActivity() {
        return activity;
    }

    public String getSelectedImageFilePath() {
        return selectedImageFilePath;
    }

}
