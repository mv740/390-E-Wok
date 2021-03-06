package com.example.nspace.museedesondes.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.utility.Resource;


public class MediaService extends Service {

    private final IBinder audioBinder = new AudioBinder();
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler audioHandler = new Handler();
    LayoutInflater inflater;
    private boolean isPlaying = false;


    public MediaService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return audioBinder;
    }

    /**
     * This method sets the audio  according to the path provided.
     * @param filePath
     */
    public void setAudio(String filePath) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(Resource.getAbsoluteFilePath(getApplicationContext(), filePath)));
        }
    }

    public void toggleAudioOnOff(View v){
        /*if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sampleaudio);
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.poi_panel, null);
            seekBar = (SeekBar) layout.findViewById(R.id.seekBar);
            seekBar.setMax(mediaPlayer.getDuration());
        }*/

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            v.setBackgroundResource(R.drawable.ic_play_circle_filled_white_48dp);
            isPlaying = false;
        } else {
            mediaPlayer.start();
            isPlaying = true;
            v.setBackgroundResource(R.drawable.ic_pause_circle_filled_white_48dp);
            //updateProgress.run();

        }

    }

    Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            while(mediaPlayer.isPlaying()) {
                int currentTime = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                int percentageProgress = (int) ((currentTime / duration) * 100);
                int currentPosition = currentTime / 1000;
                seekBar.setProgress(currentPosition);

                audioHandler.postDelayed(this, 1000);
            }
        }
    };

    public void updateProgressBar(){
        audioHandler.postDelayed(updateProgress, 100);
    }

    public class AudioBinder extends Binder{

        public MediaService getAudioService(){
            return MediaService.this;
        }
    }


    public int getCurrentPosition(){
        if(mediaPlayer != null) {
            int currentTime = mediaPlayer.getCurrentPosition();
            return currentTime / 1000;
        }
        return 0;
    }

    public void releaseAudio(){
        if (mediaPlayer != null) {
            if(mediaPlayer.isPlaying())
            {
                mediaPlayer.pause();
            }
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public int getAudioDuration(){
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public boolean isMediaSet(){
        return mediaPlayer !=null;
    }

    public void setAudioPosition(int updatedPosition){
        mediaPlayer.seekTo(updatedPosition);
    }

    /*
     //On long click reset audio
        Button buttonAudio = (Button) findViewById(R.id.play_button);
        buttonAudio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        //http://stackoverflow.com/questions/2969242/problems-with-mediaplayer-raw-resources-stop-and-start
                        //how to set data source again after reset
                        v.setBackgroundResource(R.drawable.ic_play_circle_filled_white_48dp);
                        mediaPlayer.reset();//It requires again setDataSource for player object.
                        AssetFileDescriptor afd = getApplicationContext().getResources().openRawResourceFd(R.raw.sampleaudio);
                        try {
                            mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            mediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            }
        });
     */
}
