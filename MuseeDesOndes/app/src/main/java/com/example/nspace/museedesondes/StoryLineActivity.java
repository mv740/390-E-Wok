package com.example.nspace.museedesondes;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.view.MaterialListView;
import com.example.nspace.museedesondes.Model.Map;
import com.example.nspace.museedesondes.Model.StoryLine;
import com.example.nspace.museedesondes.Model.StoryLineDescription;
import com.example.nspace.museedesondes.Utility.StoryListAdapter;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by sebastian on 2016-02-08.
 */

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
        setContentView(R.layout.activity_storyline);

        information = Map.getInstance(getApplicationContext());

        Locale currentLocale = getResources().getConfiguration().locale;
        storyLineActivityLang = currentLocale.getLanguage();

        ArrayList<StoryLine> storyLineList = information.getStoryLines();
        populateAdapterArrays(storyLineList, storyLineActivityLang);

        buildStorylineList(storyLineList, storyLineActivityLang);
    }

    private void buildStorylineList(ArrayList<StoryLine> storyLineList, String currentLanguage) {

        MaterialListView mListView = (MaterialListView) findViewById(R.id.material_listview);

        for(StoryLine storyline : storyLineList){
            ArrayList<StoryLineDescription> textList = storyline.getDescriptions();
            for(StoryLineDescription description : textList){
                if(description.getLanguage().toString().equalsIgnoreCase(currentLanguage)) {

                    Drawable d = Drawable.createFromPath(storyline.getImagePath());

                    Card card =  new Card.Builder(this)
                            .setTag("SMALL_IMAGE_CARD")
                            .withProvider(new CardProvider())
                            .setLayout(R.layout.material_image_with_buttons_card)
                            .setTitle(description.getTitle())
                            .setDescription(description.getDescription())
                            .setDrawable(R.drawable.museum_ex_1)
                            .endConfig()
                            .build();


                    mListView.getAdapter().add(card);

                    break;
                }
            }
        }

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
