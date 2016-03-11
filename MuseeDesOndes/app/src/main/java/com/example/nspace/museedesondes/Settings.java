package com.example.nspace.museedesondes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.nspace.museedesondes.utility.Preferences;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;


public class Settings extends AppCompatActivity {

    /**
     * Created by sebastian on 2/02/2016.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final RadioGroup language_radios = (RadioGroup) findViewById(R.id.language_radios);
        final FloatingActionButton ok_button = (FloatingActionButton) findViewById(R.id.ok_button);

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

                        //skip if no radio buttons were selected
                        if(languageID != -1) {
                            Preferences.setLocale(langSelected);
                            Preferences.savePreferences(langSelected);
                        }

                        finish();
                    }
                }
        );
    }
}
