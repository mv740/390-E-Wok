package com.example.nspace.museedesondes.Utility;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nspace.museedesondes.R;

/**
 * Created by sebastian on 2016-02-09.
 */

public class StoryListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] title;
    private final String[] description;
    private final Integer[] imageId;
    public StoryListAdapter(Activity context,
                            String[] web, String[] description, Integer[] imageId) {
        super(context, R.layout.storyline_item, web);
        this.context = context;
        this.title = web;
        this.description = description;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView;
        if (position == 0){
             rowView = inflater.inflate(R.layout.storyline_free_exploration_item, null, true);
        }
        else {
            rowView = inflater.inflate(R.layout.storyline_item, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.storyline_item_title);
            TextView descriptField = (TextView) rowView.findViewById(R.id.storyline_item_description);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.storyline_item_pic_background);


            txtTitle.setText(title[position]);
            descriptField.setText(description[position]);
            imageView.setImageResource(imageId[position]);
        }

        return rowView;
    }
}
