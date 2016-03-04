package com.example.nspace.museedesondes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ListView;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.provider.ListCardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.example.nspace.museedesondes.Model.Map;
import com.example.nspace.museedesondes.Model.StoryLine;
import com.example.nspace.museedesondes.Model.StoryLineDescription;
import com.example.nspace.museedesondes.Utility.Resource;


import java.util.ArrayList;
import java.util.List;
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
        //populateAdapterArrays(storyLineList, storyLineActivityLang);

        buildStorylineList(storyLineList);
    }

    private void buildStorylineList(ArrayList<StoryLine> storyLineList) {

        MaterialListView mListView = (MaterialListView) findViewById(R.id.material_listview);
        List<Card> cards = new ArrayList<>();

        Card cardFreeExploration = new Card.Builder(this)
                .setTag("LIST_CARD")
                .setDismissible()
                .withProvider(new ListCardProvider())
                .setLayout(R.layout.material_list_card_layout)
                .setTitle(R.string.free_exploration)
                .setTitleGravity(Gravity.CENTER_HORIZONTAL)
                .setDescription("Take a list")
                .endConfig()
                .build();

        cards.add(cardFreeExploration);


        for (StoryLine storyline : storyLineList) {
            StoryLineDescription localeDescription = storyline.getLocaleDescription(getApplicationContext());

            Card card = new Card.Builder(this)
                    .setTag("BASIC_IMAGE_BUTTONS_CARD")
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.material_image_with_buttons_card)
                    .setTitle(localeDescription.getTitle())
                    .setTitleColor(Color.WHITE)
                    .setDescription(localeDescription.getDescription())
                    .setDrawable(Resource.getDrawableImageFromFileName(storyline, getApplicationContext()))
                    .endConfig()
                    .build();

            cards.add(card);
            //mListView.getAdapter().add(card);

        }
        mListView.getAdapter().addAll(cards);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {


            @Override
            public void onItemClick(Card card, int position) {
                Log.d("CARD_TYPE", card.getTag().toString());

                final Intent startMap = new Intent(StoryLineActivity.this, MapActivity.class);
                startMap.putExtra("Story line list position", position);
                //startActivity(startMap);

                String message = getResources().getString(R.string.dialogMsg) + card.getProvider().getTitle();

                if(card.getTag()=="LIST_CARD")
                {
                    message = getResources().getString(R.string.dialogFree);
                }

                builder.setTitle(R.string.dialogTitle)
                        .setMessage(message)
                        .setPositiveButton(R.string.dialogOk, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d( "AlertDialog", "Positive" );
                                startActivity(startMap);
                            }
                        })
                        .setNegativeButton(R.string.dialogCancel, null)
                        .show();
            }

            @Override
            public void onItemLongClick(Card card, int position) {
                Log.d("LONG_CLICK", card.getTag().toString());
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

        for (StoryLine storyline : storyLineList) {
            ArrayList<StoryLineDescription> textList = storyline.getDescriptions();
            for (StoryLineDescription description : textList) {
                if (description.getLanguage().toString().equalsIgnoreCase(currentLanguage)) {
                    titleArray[index] = description.getTitle();
                    descriptionArray[index] = description.getDescription();
                    break;
                }
            }
            imageName = storyline.getImagePath();
            imageIdArray[index] = getResources().getIdentifier(imageName, "drawable", getPackageName());
            index++;
        }
    }

    @Override
    protected void onResume() {
        Locale currentLocale = getResources().getConfiguration().locale;
        String currentAppLanguage = currentLocale.getLanguage();

        if (!currentAppLanguage.equalsIgnoreCase(storyLineActivityLang)) {
            storyLineActivityLang = currentAppLanguage;
            recreate();
        }
        super.onResume();
    }
}
