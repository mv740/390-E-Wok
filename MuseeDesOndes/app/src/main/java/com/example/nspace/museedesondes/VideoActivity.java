package com.example.nspace.museedesondes;

import android.app.Activity;
import android.util.Log;
import android.widget.MediaController;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = (VideoView) findViewById(R.id.videoView);

        if(mediaController == null){//Controls the video
            mediaController = new MediaController(this);
        }

        //Sets the controller to be associated with the Video view
        videoView.setMediaController(mediaController);

        //Gets the path for the video
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample_video_1280x720_1mb));


        videoView.requestFocus();
        videoView.start();
    }
}
