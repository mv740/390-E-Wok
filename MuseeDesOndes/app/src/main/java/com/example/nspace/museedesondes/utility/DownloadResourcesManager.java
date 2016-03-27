package com.example.nspace.museedesondes.utility;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.Image;
import com.example.nspace.museedesondes.model.MuseumMap;
import com.example.nspace.museedesondes.model.PointOfInterest;
import com.example.nspace.museedesondes.model.StoryLine;
import com.example.nspace.museedesondes.model.Video;
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
            Uri downloadUri = Uri.parse(resourceRootPath + "/" + databaseFilePath);
            Uri destination = Uri.parse(activity.getCacheDir().toString() + "/mapOnline.json");
            DownloadRequest downloadRequest = new DownloadRequest(downloadUri);
            downloadRequest.setDestinationURI(destination);
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
                    Log.e("download", "failed " + downloadRequest.getUri() + ": " + errorMessage);
                }

                @Override
                public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                    // Log.e("download", "in progress :" + downloadRequest.getUri());
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

        for (PointOfInterest pointOfInterest : information.getPointOfInterests()) {

            getImagesURI(pointOfInterest, prepareQueryImageList);
            getVideosURI(pointOfInterest, prepareQueryVideoList);
        }
        for (StoryLine storyLine : information.getStoryLines()) {
            prepareQueryImageList.add(storyLine.getImagePath());
        }

        for (String filePath : prepareQueryImageList) {
            downloadImage(filePath);
        }
        for (String filePath : prepareQueryVideoList) {
            downloadVideo(filePath);
        }

    }

    private void downloadVideo(String filePath) {
        Uri path = Uri.parse(resourceRootPath + "/video/" + filePath + ".mp4");
        Uri destination = Uri.parse(activity.getCacheDir().toString() + "/" + filePath + ".mp4");
        DownloadRequest downloadRequest = new DownloadRequest(path);
        downloadRequest.setDestinationURI(destination);
        downloadRequest.setStatusListener(new DownloadStatusListenerV1() {
            @Override
            public void onDownloadComplete(DownloadRequest downloadRequest) {
                Log.e("downloadC", "complete : " + downloadRequest.getUri());
                downloadList.remove(Integer.valueOf(downloadRequest.getDownloadId()));
                isDone();
            }

            @Override
            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                Log.e("download", "failed " + downloadRequest.getUri() + ": " + errorMessage);
            }

            @Override
            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                // Log.e("download", "in progress :" + downloadRequest.getUri());
            }
        });
        downloadList.add(downloadManager.add(downloadRequest));
    }

    private void downloadImage(String filePath) {
        Uri path = Uri.parse(resourceRootPath + "/image/" + filePath + ".jpg");
        Uri destination = Uri.parse(activity.getCacheDir().toString() + "/" + filePath + ".jpg");
        DownloadRequest downloadRequest = new DownloadRequest(path);
        downloadRequest.setDestinationURI(destination);
        downloadRequest.setStatusListener(new DownloadStatusListenerV1() {
            @Override
            public void onDownloadComplete(DownloadRequest downloadRequest) {
                Log.e("downloadC", "complete : " + downloadRequest.getUri());
                downloadList.remove(Integer.valueOf(downloadRequest.getDownloadId()));
            }

            @Override
            public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                Log.e("download", "failed " + downloadRequest.getUri() + ": " + errorMessage);
            }

            @Override
            public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                // Log.e("download", "in progress :" + downloadRequest.getUri());
            }
        });
        downloadList.add(downloadManager.add(downloadRequest));
    }

    private void downloadFloorPlans(MuseumMap information) {
        for (FloorPlan floorPlan : information.getFloorPlans()) {
            Log.e("download", floorPlan.getImagePath());
            Uri path = Uri.parse(resourceRootPath + "/floors/" + floorPlan.getImagePath() + ".png");
            Uri destination = Uri.parse(activity.getCacheDir().toString() + "/" + floorPlan.getImagePath() + ".png");
            DownloadRequest downloadRequest = new DownloadRequest(path);
            downloadRequest.setDestinationURI(destination);
            downloadRequest.setStatusListener(new DownloadStatusListenerV1() {
                @Override
                public void onDownloadComplete(DownloadRequest downloadRequest) {
                    Log.e("downloadC", "complete : " + downloadRequest.getUri());
                    downloadList.remove(Integer.valueOf(downloadRequest.getDownloadId()));
                }

                @Override
                public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                    Log.e("download", "failed " + downloadRequest.getUri() + ": " + errorMessage);
                }

                @Override
                public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                    // Log.e("download", "in progress :" + downloadRequest.getUri());
                }
            });
            downloadManager.add(downloadRequest);
        }
    }

    private void getImagesURI(PointOfInterest pointOfInterest, Set<String> stringHashSet) {

        for (Image image : pointOfInterest.getAllImages(activity)) {
            stringHashSet.add(image.getPath());
        }
    }

    private void getVideosURI(PointOfInterest pointOfInterest, Set<String> prepareQueryVideoList) {

        for (Video video : pointOfInterest.getAllVideos(activity)) {
            prepareQueryVideoList.add(video.getPath());
        }
    }

    public void setResourceRootPath(String resourceRootPath) {
        this.resourceRootPath = resourceRootPath;
    }

    public void setDatabaseFilePath(String databaseFilePath) {
        this.databaseFilePath = databaseFilePath;
    }

    public void isDone() {
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

}
