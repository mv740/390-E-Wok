package com.example.nspace.museedesondes;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class FullscreenImgActivity extends AppCompatActivity {

    /**
     * Created by sebastian on 2/02/2016.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_imgview);
        ImageView imgView = (ImageView) findViewById(R.id.fullscreen_imgview);
        imgView.setImageDrawable(MapActivity.imgToSendToFullscreenImgActivity);

        setProperOrientation(imgView);

        bringButtonsToFront();
    }

    private void setProperOrientation(ImageView imgView){
        if (isPortrait(imgView)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    private boolean isPortrait(ImageView imageView) {
        float width = imageView.getDrawable().getBounds().width();
        float height = imageView.getDrawable().getBounds().height();

        return (width < height);
    }

    private void bringButtonsToFront() {
        Button exit = (Button) findViewById(R.id.exitFullscreenImg);
        exit.bringToFront();
    }


    public void exitFullscreenImgOnClick(View v) {
        finish();
    }
}
