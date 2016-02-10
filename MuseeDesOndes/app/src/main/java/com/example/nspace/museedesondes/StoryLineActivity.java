package com.example.nspace.museedesondes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Locale;

public class StorylineActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_line);


        Locale currentLocale = getResources().getConfiguration().locale;
        String currentLanguage = currentLocale.getLanguage();

        //todo: fetch storyline array from Map

        //array of options --> array adapter --> listView
        //list view : {views: storyline_items.xml}

        //todo display "special buttons" with image, title, decription? for each storyline

//        for(Storyline storyline: storylineList) {
//            Button button = new Button(this);
//
//            if(currentLanguage.equals("en_US")) {
//                button.setText("DYNAMIC: " + storyline.getName().get(0).getData());
//            } else if(currentLanguage.equals("fr")) {
//                button.setText("DYNAMIC: " + storyline.getName().get(1).getData());
//            }
//
//        }




        final Button exploration_button = (Button) findViewById(R.id.exploration_button);
        exploration_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent startMap = new Intent(StorylineActivity.this, MapActivity.class);
                        startActivity(startMap);
                    }
                }
        );
    }
}
