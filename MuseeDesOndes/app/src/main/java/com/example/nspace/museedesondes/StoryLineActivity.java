package com.example.nspace.museedesondes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.nspace.museedesondes.Model.Map;
import com.example.nspace.museedesondes.Model.StoryLine;
import com.example.nspace.museedesondes.Model.StoryLineDescription;
import com.example.nspace.museedesondes.Model.Text;
import com.example.nspace.museedesondes.Utility.CustomStoryList;

import java.util.ArrayList;
import java.util.Locale;

public class StoryLineActivity extends AppCompatActivity {

    ListView list;
    String[] titleArray;
    String[] descriptionArray;
    Integer[] imageIdArray;
    Map information;
    String storyLineActivityLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_line);

        information = Map.getInstance(getApplicationContext());

        Locale currentLocale = getResources().getConfiguration().locale;
        storyLineActivityLang = currentLocale.getLanguage();

        ArrayList<StoryLine> storyLineList = information.getStoryLines();
        populateAdapterArrays(storyLineList, storyLineActivityLang);

        CustomStoryList adapter = new CustomStoryList(StoryLineActivity.this, titleArray, descriptionArray, imageIdArray);
        list = (ListView)findViewById(R.id.storylineList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent startMap = new Intent(StoryLineActivity.this, MapActivity.class);
                startMap.putExtra("title",titleArray[position]);
                startActivity(startMap);
            }
        });
    }

    public void populateAdapterArrays(ArrayList<StoryLine> storyLineList, String currentLanguage) {
        titleArray = new String[storyLineList.size() + 1];
        descriptionArray = new String[storyLineList.size() + 1];
        imageIdArray = new Integer[storyLineList.size() + 1];

        //default values used for free exploration in the first listview position
        titleArray[0] = getResources().getString(R.string.free_exploration);
        descriptionArray[0] = getResources().getString(R.string.free_exploration_description);
        imageIdArray[0] = R.drawable.free_exploration;

        String imageName;
        int index = 1;
        
        for(StoryLine storyline : storyLineList){
            ArrayList<StoryLineDescription> textList = storyline.getDescriptions();
            for(StoryLineDescription description : textList){
                if(description.getLanguage().toString().equalsIgnoreCase(currentLanguage)) {
                    titleArray[index] = description.getTitle();
                    descriptionArray[index] = description.getDescription();
                    break;
                }
            }
            imageName = storyline.getImagePath();
            imageIdArray[index] = getResources().getIdentifier(imageName , "drawable", getPackageName());
            index++;
        }
    }

    @Override
    protected void onResume() {
        Locale currentLocale = getResources().getConfiguration().locale;
        String currentAppLanguage = currentLocale.getLanguage();

        if(!currentAppLanguage.equalsIgnoreCase(storyLineActivityLang)) {
            storyLineActivityLang = currentAppLanguage;
            recreate();
        }
        super.onResume();
    }
}
