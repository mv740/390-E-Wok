package com.example.nspace.museedesondes;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import com.example.nspace.museedesondes.utility.Preferences;
import com.github.clans.fab.FloatingActionButton;

public class SettingsActivity extends AppCompatActivity {

    /**
     * Created by sebastian on 2/02/2016.
     */

    private int selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final FloatingActionButton okButton = (FloatingActionButton) findViewById(R.id.ok_button);

        final String languagesDisplay[] = { "French", "English" };
        final String languagesType[] = { "fr", "en_US"};

        int savedChoice = 0;

        for(int i = 0;i<languagesType.length;i++)
        {
            if(getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(languagesType[i]))
            {
                savedChoice = i;
            }
        }

        NumberPicker myNumberPicker = (NumberPicker) findViewById(R.id.language_selection_view); // new NumberPicker(this);
        myNumberPicker.setMinValue(0);
        myNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        myNumberPicker.setMaxValue(languagesDisplay.length - 1);
        myNumberPicker.setValue(savedChoice);
        selectedLanguage = savedChoice;
        myNumberPicker.setDisplayedValues(languagesDisplay);
        setDividerColor(myNumberPicker, Color.WHITE);

        NumberPicker.OnValueChangeListener myValueChangedListener  = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectedLanguage = newVal;
            }
        };
        myNumberPicker.setOnValueChangedListener(myValueChangedListener);

        //set english or french language
        okButton.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View view) {
                        String selectedLanguageType = null;

                        {
                            switch (selectedLanguage) {
                                case 0:
                                    selectedLanguageType = "fr";
                                    break;
                                case 1:
                                    selectedLanguageType = "en_US";
                                    break;
                            }
                        }
                        Log.d("Lang", String.valueOf(selectedLanguage));
                        Log.d("Lang", selectedLanguageType);

                        //save if different
                        if (!getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(selectedLanguageType)) {
                            Preferences.setLocale(selectedLanguageType);
                            Preferences.saveLanguagePreference(selectedLanguageType);
                        }
                        finish();
                    }
                }
        );
    }
    //http://stackoverflow.com/questions/24233556/changing-numberpicker-divider-color
    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException | Resources.NotFoundException | IllegalAccessException e) {
                    e.printStackTrace();
                    Log.e("SettingActivity", Log.getStackTraceString(e));
                }
                break;
            }
        }
    }
}
