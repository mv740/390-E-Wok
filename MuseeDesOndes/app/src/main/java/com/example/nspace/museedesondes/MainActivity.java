package com.example.nspace.museedesondes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;

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

        final Button settings_button = (Button) findViewById(R.id.settings_button);
        settings_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        Intent startSettings = new Intent(MainActivity.this, Settings.class);
                        startActivity(startSettings);
                    }
                }
        );


        final Button exploration_button = (Button) findViewById(R.id.exploration_button);
        exploration_button.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent  startMap = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(startMap);
                    }
                }
        );
    }



}
