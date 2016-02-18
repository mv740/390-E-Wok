package com.example.nspace.museedesondes;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

public class AudioService extends Service {

    private final IBinder audioBinder = new AudioBinder();
    private MediaPlayer mediaPlayer; //todo will need to mediaPlayer.release();  when menu is closed to release ram
    private SeekBar seekBar;
    LayoutInflater inflater;

    public AudioService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return audioBinder;
    }

    public void toggleAudioOnOff(View v){
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sampleaudio);
            inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.poi_more_info, null);
            seekBar = (SeekBar) layout.findViewById(R.id.seekBar);
        }

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            v.setBackgroundResource(R.drawable.ic_play_circle_filled_white_48dp);
        } else {
            mediaPlayer.start();
            v.setBackgroundResource(R.drawable.ic_pause_circle_filled_white_48dp);

        }

    }

    public class AudioBinder extends Binder{

        AudioService getAudioService(){
            return AudioService.this;
        }
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
