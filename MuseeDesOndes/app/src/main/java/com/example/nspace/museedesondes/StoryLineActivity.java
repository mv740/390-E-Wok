package com.example.nspace.museedesondes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nspace.museedesondes.Utility.CustomStoryList;

import java.util.Locale;

public class StoryLineActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_line);


        Locale currentLocale = getResources().getConfiguration().locale;
        String currentLanguage = currentLocale.getLanguage();

        //todo: fetch storyline title/description/image array from Map and put into arrays based on currentLanguage
        ListView list;
        String[] titles = {
                "RCA Throughout history",
                "Nippers the Dog",
                "Gramaphone Stuff",
        } ;
        Integer[] imageId = {
                R.drawable.placeholder_home_icon,
                R.drawable.placeholder_panda_icon,
                R.drawable.placeholder_tree_icon,
        };

        CustomStoryList adapter = new CustomStoryList(StoryLineActivity.this, titles, imageId);
        list = (ListView)findViewById(R.id.storylineList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //todo: replace with start map activity addExtra() passing storyline ID
                //Toast.makeText(StorylineActivity.this, "You Clicked at " + titles[+position], Toast.LENGTH_SHORT).show();

            }
        });

        final Button exploration_button = (Button) findViewById(R.id.exploration_button);
        exploration_button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        Intent startMap = new Intent(StoryLineActivity.this, MapActivity.class);
                        startActivity(startMap);
                    }
                }
        );
    }
}
