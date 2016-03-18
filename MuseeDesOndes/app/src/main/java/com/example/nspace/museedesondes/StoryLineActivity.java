package com.example.nspace.museedesondes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.action.TextViewAction;
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
    int cardsNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storyline);

        information = Map.getInstance(getApplicationContext());

        Locale currentLocale = getResources().getConfiguration().locale;
        storyLineActivityLang = currentLocale.getLanguage();

        List<StoryLine> storyLineList = information.getStoryLines();

        buildStorylineList(storyLineList);
    }

    /**
     * Build a list of cards that contains information about a storyline
     *
     * @param storyLineList
     */
    private void buildStorylineList(List<StoryLine> storyLineList) {

        MaterialListView mListView = (MaterialListView) findViewById(R.id.material_listview);
        List<Card> cards = new ArrayList<>();


        for (StoryLine storyline : storyLineList) {
            StoryLineDescription localeDescription = storyline.getLocaleDescription(getApplicationContext());

            Card card = new Card.Builder(this)
                    .setTag(storyline)
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.storyline_cards)
                    .setTitle(localeDescription.getTitle())
                    .setTitleColor(Color.WHITE)
                    .setDescription(localeDescription.getDescription())
                    .setDescriptionColor(Color.DKGRAY)
                    .setDrawable(Resource.getDrawableImageFromFileName(storyline, getApplicationContext()))
                    .addAction(R.id.right_text_button, new TextViewAction(this)
                                    .setText(R.string.start_storyline)
                                    .setTextResourceColor(R.color.rca_primary)
                    )
                    .endConfig()
                    .build();

            cards.add(card);
            //mListView.getAdapter().add(card);

        }

        Card cardFreeExploration = new Card.Builder(this)
                .setTag("free_exploration")
                .setDismissible()
                .withProvider(new ListCardProvider())
                .setLayout(R.layout.free_exploration_card)
                .setTitle(R.string.free_exploration)
                .setTitleGravity(Gravity.CENTER)
                .setTitleColor(Color.BLACK)
                .endConfig()
                .build();

        cards.add(cardFreeExploration);

        cardsNumbers = cards.size();

        mListView.getAdapter().addAll(cards);
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);

        //on storyline click, open confirmation dialog
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(@NonNull Card card, int position) {

                final Intent startMap = new Intent(StoryLineActivity.this, MapActivity.class);
                startMap.putExtra("Story line list position", position);

                String message = getResources().getString(R.string.dialogMsg);

                if (card.getTag() == "free_exploration") {
                    message = getResources().getString(R.string.dialogFree);
                }

                AlertDialog.Builder builder = dialogBuilder.setTitle(card.getProvider().getTitle())
                        .setMessage(message)
                        .setPositiveButton(R.string.dialogOk, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("AlertDialog", "Positive");
                                startActivity(startMap);
                            }
                        })
                        .setNegativeButton(R.string.dialogCancel, null);
                builder.show();


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

    public int getCardsNumbers() {
        return cardsNumbers;
    }
}
