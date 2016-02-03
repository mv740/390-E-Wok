package com.example.nspace.museedesondes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /**
     * Created by sebastian on 2/02/2016.
     */

    String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Locale currentLocale = getResources().getConfiguration().locale;
        currentLanguage = currentLocale.getLanguage();

        final Button settings_button = (Button) findViewById(R.id.settings_button);
        settings_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        Intent startSettings = new Intent(MainActivity.this, Settings.class);
                        startActivity(startSettings);
                    }
                }
        );
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Locale currentLocale = getResources().getConfiguration().locale;
        if(!currentLanguage.equals(currentLocale.getLanguage())) {
            recreate();
        };
    }
}
