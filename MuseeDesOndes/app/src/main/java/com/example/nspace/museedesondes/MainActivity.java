package com.example.nspace.museedesondes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.nspace.museedesondes.Model.Preferences;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    /**
     * Created by sebastian on 2/02/2016.
     */

    String appLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Preferences.setAppContext(this.getApplicationContext());
        Preferences.loadLanguagePreference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadReplaceMeWith(R.layout.welcome_language);
    }

    private void loadReplaceMeWith(int id){
        FrameLayout replaceMe = (FrameLayout) findViewById(R.id.welcome_replace_me);
        replaceMe.removeAllViews();
        LayoutInflater li = LayoutInflater.from(this.getBaseContext());
        RelativeLayout replacer = (RelativeLayout) li.inflate(id, null);
        replaceMe.addView(replacer);
    }


    //HANDLERS ****************************************

    public void changeLanguageOnClickFr(View view){
        changeLanguage("fr");
        loadReplaceMeWith(R.layout.welcome_begin_tour);
    }
    public void changeLanguageOnClickEn(View view){
        changeLanguage("en_US");
        loadReplaceMeWith(R.layout.welcome_begin_tour);
    }

    private void changeLanguage(String lang){
        Preferences.setLocale(lang);
        Preferences.savePreferences(lang);
    }

    public void beginTourOnClick(View view) {
        Intent  startStorylines = new Intent(MainActivity.this, StoryLineActivity.class);
        startActivity(startStorylines);
    }

    public void settingsOnClick(View view){
        Intent startSettings = new Intent(MainActivity.this, Settings.class);
        startActivity(startSettings);
    }




}
