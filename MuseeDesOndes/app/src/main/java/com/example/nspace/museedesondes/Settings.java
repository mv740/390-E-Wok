package com.example.nspace.museedesondes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    /**
     * Created by sebastian on 2/02/2016.
     */

    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        //set english language
        final Button english_button = (Button) findViewById(R.id.english_button);
        english_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        setLocale("en_US");
                    }
                }
        );


        //set french language
        final Button french_button = (Button) findViewById(R.id.french_button);
        french_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        setLocale("fr");
                    }
                }
        );


    }
    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, Settings.class);
        startActivity(refresh);
        finish();
    }

}
