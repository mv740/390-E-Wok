package com.example.nspace.museedesondes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.nspace.museedesondes.utility.DownloadResourcesManager;
import com.example.nspace.museedesondes.utility.Preferences;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.style.Wave;


public class MainActivity extends AppCompatActivity {

    /**
     * Created by sebastian on 2/02/2016.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Preferences.setAppContext(this.getApplicationContext());
        Preferences.loadLanguagePreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        if (sharedPrefs.getBoolean("firstrun", true)){
            loadingProgress();

            DownloadResourcesManager downloadResourcesManager = new DownloadResourcesManager(MainActivity.this);
            downloadResourcesManager.setResourceRootPath("http://michalwozniak.ca/map/demo");
            downloadResourcesManager.setDatabaseFilePath("Map.json");
            downloadResourcesManager.getMostRecentMapInformation();
        }else
        {
            loadReplaceMeWith(R.layout.welcome_begin_tour);
        }


    }
    //todo if team prefer this animation, need to remove     compile 'com.github.ndczz:infinity-loading:0.4' and commented code in ressource
    private void loadingProgress() {
        loadReplaceMeWith(R.layout.downloading_resources);
        final SpinKitView spinKitView = (SpinKitView) findViewById(R.id.loading);
        spinKitView.setIndeterminateDrawable(new Wave());
    }

    private void loadReplaceMeWith(int id) {
        FrameLayout replaceMe = (FrameLayout) findViewById(R.id.welcome_replace_me);
        replaceMe.removeAllViews();
        LayoutInflater li = LayoutInflater.from(this.getBaseContext());
        RelativeLayout replacer = (RelativeLayout) li.inflate(id, null);
        replaceMe.addView(replacer);
    }


    //HANDLERS ****************************************

    public void changeLanguageOnClickFr(View view) {
        changeLanguage("fr");
        loadReplaceMeWith(R.layout.welcome_begin_tour);
    }

    public void changeLanguageOnClickEn(View view) {
        changeLanguage("en_US");
        loadReplaceMeWith(R.layout.welcome_begin_tour);
    }

    private void changeLanguage(String lang) {
        Preferences.setLocale(lang);
        Preferences.saveLanguagePreference(lang);
    }

    public void beginTourOnClick(View view) {
        Intent startStorylines = new Intent(MainActivity.this, StoryLineActivity.class);
        startActivity(startStorylines);
    }

}
