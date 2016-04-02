package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * Created by michal on 3/18/2016.
 *
 * custom with bigger text size
 */
public class TextPicker extends android.widget.NumberPicker {

    public TextPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view) {
        if(view instanceof EditText){
            ((EditText) view).setTextSize(25);
        }
    }

}
