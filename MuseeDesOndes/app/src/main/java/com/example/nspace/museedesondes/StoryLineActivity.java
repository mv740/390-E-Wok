package com.example.nspace.museedesondes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nspace.museedesondes.Utility.CustomStoryList;

import java.util.Locale;

public class StoryLineActivity extends AppCompatActivity {

    ListView list;
    String[] titles;
    String[] description;
    Integer[] imageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_line);

        Locale currentLocale = getResources().getConfiguration().locale;
        String currentLanguage = currentLocale.getLanguage();

        //todo: fetch storyline title/description/image array from Map and put into arrays based on currentLanguage
        titles = new String[]{
                "Free Exploration",
                "RCA Throughout history",
                "Nippers the Dog",
                "Gramaphone Stuff",
        } ;

        description = new String[]{
                "Explore all of the exhbits in the museum in any order.",
                "Before the publication of the First Folio in 1623, nineteen of the thirty-seven plays in Shakespeare's canon had appeared in quarto format. ",
                "With the exception of Othello (1622), all of the quartos were published prior to the date of Shakespeare's retirement from the theatre in about 1611",
                "Here you will find the complete text of Shakespeare's plays, based primarily on the First Folio, and a variety of helpful resources, including extensive explanatory notes, character analysis."
        } ;

        imageId = new Integer[]{
                R.drawable.free_exploration,
                R.drawable.placeholder_home_icon,
                R.drawable.placeholder_panda_icon,
                R.drawable.placeholder_tree_icon,
        };

        CustomStoryList adapter = new CustomStoryList(StoryLineActivity.this, titles, description, imageId);
        list = (ListView)findViewById(R.id.storylineList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent startMap = new Intent(StoryLineActivity.this, MapActivity.class);
                startMap.putExtra("title",titles[position]);
                startActivity(startMap);
            }
        });
    }
}
