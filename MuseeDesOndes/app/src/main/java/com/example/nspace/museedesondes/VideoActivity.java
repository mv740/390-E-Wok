package com.example.nspace.museedesondes;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    static final String VIDEO_KEY = "position";
    int videoPosition = 0;
    String fileName;

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

        fileName = getIntent().getExtras().getString("File_Name");
        Log.v("videoPos",fileName);

        //Gets the path for the video
        videoView.setVideoURI(Uri.parse(fileName));

        if(savedInstanceState != null){
            videoPosition = savedInstanceState.getInt(VIDEO_KEY);
            Log.v("videoPos",Integer.toString(videoPosition));
        }

        videoView.requestFocus();

        //VideoView does not go immediately to the seekTo position
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.seekTo(videoPosition);
                videoView.start();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(VIDEO_KEY, videoView.getCurrentPosition());
        videoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        videoPosition = savedInstanceState.getInt(VIDEO_KEY);
        videoView.seekTo(videoPosition);
    }



}