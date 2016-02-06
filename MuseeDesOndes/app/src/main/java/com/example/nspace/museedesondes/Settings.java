package com.example.nspace.museedesondes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.nspace.museedesondes.Model.Preferences;

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

        final RadioGroup language_radios = (RadioGroup) findViewById(R.id.language_radios);
        final Button ok_button = (Button) findViewById(R.id.ok_button);

        //set english or french language
        ok_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        int languageID = language_radios.getCheckedRadioButtonId();
                        String langSelected = null;

                        switch(languageID) {
                            case R.id.english_button :
                                langSelected = "en_US";
                                break;
                            case R.id.french_button:
                                langSelected = "fr";
                                break;
                        }

                        Preferences.setLocale(langSelected);
                        //Preferences.savePreferences(langSelected);
                        Intent startLauncher = new Intent(Settings.this, MainActivity.class);
                        startActivity(startLauncher);
                    }
                }
        );
    }
}
