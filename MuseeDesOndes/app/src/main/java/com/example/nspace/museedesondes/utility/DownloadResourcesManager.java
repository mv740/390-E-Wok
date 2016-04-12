package com.example.nspace.museedesondes.utility;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.model.Audio;
import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.Image;
import com.example.nspace.museedesondes.model.MuseumMap;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.example.nspace.museedesondes.model.StoryLine;
import com.example.nspace.museedesondes.model.Video;
import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by michal on 3/27/2016.
 */
public class DownloadResourcesManager {

    public static final String DOWNLOAD = "download";
    private static final String URL_DASH = "/";
    private ThinDownloadManager downloadManager;
    private String databaseFilePath;
    private String resourceRootPath;
    private List<Integer> downloadList;
    private Activity activity;


    public DownloadResourcesManager(Activity activity) {
        this.downloadManager = new ThinDownloadManager();
        this.activity = activity;
        downloadList = new ArrayList<>();
    }

    public void getMostRecentMapInformation() {
        if (!databaseFilePath.isEmpty() || !resourceRootPath.isEmpty()) {
            Uri downloadUri = Uri.parse(resourceRootPath +URL_DASH+ databaseFilePath);
            Uri destination = Uri.parse(activity.getFilesDir() + "/mapOnline.json");
            DownloadRequest downloadRequest = new DownloadRequest(downloadUri);
            downloadRequest.setDestinationURI(destination);
            downloadRequest.setRetryPolicy(new DefaultRetryPolicy());
            downloadRequest.setStatusListener(new DownloadStatusListenerV1() {
                @Override
                public void onDownloadComplete(DownloadRequest downloadRequest) {
                    Log.e("downloadC", "complete : " + downloadRequest.getUri());
                    //start resources download when json database was successfully downloaded
                    downloadList.remove(Integer.valueOf(downloadRequest.getDownloadId()));
                    downloadResources();
                }

                @Override
                public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                    Log.e(DOWNLOAD, "failed " + downloadRequest.getUri() + ": " + errorMessage);
                    loadReplaceMeWith(R.layout.download_error);
                }

                @Override
                public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                }
            });

            downloadList.add(downloadManager.add(downloadRequest));
        } else {
            Log.e("DownloadRManager", "Please set all source");
        }
    }

    private void downloadResources() {


        MuseumMap information = MuseumMap.getInstance(activity);
        downloadFloorPlans(information);

        Set<String> prepareQueryImageList = new HashSet<>();
        Set<String> prepareQueryVideoList = new HashSet<>();
        Set<String> prepareQueryAudioList = new HashSet<>();

        for (PointOfInterest pointOfInterest : information.getPointOfInterests()) {

            getImagesURI(pointOfInterest, prepareQueryImageList);
            getVideosURI(pointOfInterest, prepareQueryVideoList);
            getAudioURI(pointOfInterest, prepareQueryAudioList);
        }
        for (StoryLine storyLine : information.getStoryLines()) {
            if(!storyLine.getImagePath().isEmpty())
            {
                prepareQueryImageList.add(storyLine.getImagePath());
                storyLine.setImagePath(Resource.getSanitizedFileNameWithoutDirectories( storyLine.getImagePath()));
            }
        }

        if(prepareQueryImageList.isEmpty())
        {
            for (String filePath : prepareQueryImageList) {
                downloadImageOrAudio(filePath);
            }
        }
        if(!prepareQueryAudioList.isEmpty())
        {
            for (String filePath : prepareQueryAudioList) {
                downloadImageOrAudio(filePath);
            }
        }
        for (String filePath : prepareQueryVideoList) {
            downloadVideo(filePath);
        }
    }

    private void downloadVideo(String filePath) {
        Uri path = Uri.parse(resourceRootPath + filePath);
        Uri destination = Uri.parse(activity.getFilesDir() +URL_DASH+ Resource.getFilenameWithoutDirectories(filePath));
        DownloadRequest downloadRequest = new DownloadRequest(path);
        downloadRequest.setDestinationURI(destination);
        Log.e("destinationVideo", destination.toString());
        downloadRequest.setStatusListener(new DownloadStatusListener());
        downloadList.add(downloadManager.add(downloadRequest));
    }

    private void downloadImageOrAudio(String filePath) {
        Uri path = Uri.parse(resourceRootPath + filePath);
        Uri destination = Uri.parse(activity.getFilesDir() +URL_DASH+ Resource.getFilenameWithoutDirectories(filePath));
        Log.e("destinationImageAudio", destination.toString());
        DownloadRequest downloadRequest = new DownloadRequest(path);
        downloadRequest.setDestinationURI(destination);
        downloadRequest.setStatusListener(new DownloadStatusListener());
        downloadList.add(downloadManager.add(downloadRequest));
    }


    private void downloadFloorPlans(MuseumMap information) {
        for (FloorPlan floorPlan : information.getFloorPlans()) {
            Uri path = Uri.parse(resourceRootPath +URL_DASH+ floorPlan.getImagePath());
            String filenameStriped = Resource.getFilenameWithoutDirectories(floorPlan.getImagePath());
            floorPlan.setImagePath(filenameStriped);
            Uri destination = Uri.parse(activity.getFilesDir() +URL_DASH+ filenameStriped);
            Log.e("destinationFloor", destination.toString());
            DownloadRequest downloadRequest = new DownloadRequest(path);
            downloadRequest.setDestinationURI(destination);
            downloadRequest.setStatusListener(new DownloadStatusListener());
            downloadManager.add(downloadRequest);
        }
    }

    private void getImagesURI(PointOfInterest pointOfInterest, Set<String> stringHashSet) {

        List<Image> imageList = pointOfInterest.getAllImages(activity);
        if (!imageList.isEmpty()) {
            for (Image image : imageList) {
                stringHashSet.add(image.getPath());
                image.setPath(Resource.getSanitizedFileNameWithoutDirectories(image.getPath()));
            }
        }
    }

    private void getVideosURI(PointOfInterest pointOfInterest, Set<String> prepareQueryVideoList) {

        List<Video> videoList = pointOfInterest.getAllVideos(activity);
        if (!videoList.isEmpty()) {
            for (Video video : videoList) {
                prepareQueryVideoList.add(video.getPath());
                video.setPath(Resource.getSanitizedFileNameWithoutDirectories(video.getPath()));
            }
        }
    }

    private void getAudioURI(PointOfInterest pointOfInterest, Set<String> prepareQueryAudioList) {
        List<Audio> audioList = pointOfInterest.getAllAudios(activity);
        if (!audioList.isEmpty()) {
            for (Audio audio : audioList) {
                prepareQueryAudioList.add(audio.getPath());
                audio.setPath(Resource.getSanitizedFileNameWithoutDirectories(audio.getPath()));
            }
        }
    }

    public void setResourceRootPath(String resourceRootPath) {
        this.resourceRootPath = resourceRootPath;
    }

    public void setDatabaseFilePath(String databaseFilePath) {
        this.databaseFilePath = databaseFilePath;
    }

    private void isDone() {
        Log.e("list", String.valueOf(downloadList.size() == 0));
        if (downloadList.size() == 0) {

            loadReplaceMeWith(R.layout.welcome_language);

        }
    }

    private void loadReplaceMeWith(int id) {
        FrameLayout replaceMe = (FrameLayout) activity.findViewById(R.id.welcome_replace_me);
        replaceMe.removeAllViews();
        LayoutInflater li = LayoutInflater.from(activity.getBaseContext());
        RelativeLayout replacer = (RelativeLayout) li.inflate(id, null);
        replaceMe.addView(replacer);
    }

    private class DownloadStatusListener implements DownloadStatusListenerV1 {

        @Override
        public void onDownloadComplete(DownloadRequest downloadRequest) {
            Log.e("downloadC", "complete : " + downloadRequest.getUri());
            downloadList.remove(Integer.valueOf(downloadRequest.getDownloadId()));
            isDone();
        }

        @Override
        public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
            Log.e(DOWNLOAD, "failed " + downloadRequest.getUri() + ": " + errorMessage + "errorCode :" + errorCode);
        }

        @Override
        public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
            // Log.e("download", "in progress :" + downloadRequest.getUri());
        }
    }

}
