package com.example.nspace.museedesondes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.provider.ListCardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.example.nspace.museedesondes.model.Map;
import com.example.nspace.museedesondes.model.StoryLine;
import com.example.nspace.museedesondes.model.StoryLineDescription;
import com.example.nspace.museedesondes.utility.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sebastian on 2016-02-08.
 */

public class StoryLineActivity extends AppCompatActivity {

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

        buildStorylineList(storyLineList);
    }

    /**
     * Build a list of cards that contains information about a storyline
     *
     * @param storyLineList
     */
    private void buildStorylineList(ArrayList<StoryLine> storyLineList) {

        MaterialListView mListView = (MaterialListView) findViewById(R.id.material_listview);
        List<Card> cards = new ArrayList<>();

        Card cardFreeExploration = new Card.Builder(this)
                .setTag("free_exploration")
                .setDismissible()
                .withProvider(new ListCardProvider())
                .setLayout(R.layout.material_list_card_layout)
                .setTitle(R.string.free_exploration)
                .setTitleGravity(Gravity.CENTER_HORIZONTAL)
                .setTitleColor(Color.BLACK)
                .endConfig()
                .build();

        cards.add(cardFreeExploration);

        for (StoryLine storyline : storyLineList) {
            StoryLineDescription localeDescription = storyline.getLocaleDescription(getApplicationContext());

            Card card = new Card.Builder(this)
                    .setTag("storyline_card")
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.storyline_cards)
                    .setTitle(localeDescription.getTitle())
                    .setTitleColor(Color.WHITE)
                    .setDescription(localeDescription.getDescription())
                    .setDescriptionColor(Color.DKGRAY)
                    .setDrawable(Resource.getDrawableImageFromFileName(storyline, getApplicationContext()))
                    .endConfig()
                    .build();

            cards.add(card);
            //mListView.getAdapter().add(card);

        }
        mListView.getAdapter().addAll(cards);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        //on storyline click, open confirmation dialog
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(Card card, int position) {
                Log.d("CARD_TYPE", card.getTag().toString());

                final Intent startMap = new Intent(StoryLineActivity.this, MapActivity.class);
                startMap.putExtra("Story line list position", position);

                String message = getResources().getString(R.string.dialogMsg) + card.getProvider().getTitle();

                if (card.getTag() == "free_exploration") {
                    message = getResources().getString(R.string.dialogFree);
                }

                dialogBuilder.setTitle(R.string.dialogTitle)
                        .setMessage(message)
                        .setPositiveButton(R.string.dialogOk, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("AlertDialog", "Positive");
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
