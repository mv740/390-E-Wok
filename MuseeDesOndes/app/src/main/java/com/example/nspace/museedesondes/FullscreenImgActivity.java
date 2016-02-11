package com.example.nspace.museedesondes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.nspace.museedesondes.Model.Preferences;


public class FullscreenImgActivity extends AppCompatActivity {

    /**
     * Created by sebastian on 2/02/2016.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_imgview);
        ImageView img = (ImageView) findViewById(R.id.fullscreen_imgview);
        img.setImageDrawable(MapActivity.imgToSendToFullscreenImgActivity);

        bringButtonsToFront();
    }

    private void bringButtonsToFront(){
        Button exit = (Button) findViewById(R.id.exitFullscreenImg);
        exit.bringToFront();
    }


    public void exitFullscreenImgOnClick(View v){
        finish();
    }
}
