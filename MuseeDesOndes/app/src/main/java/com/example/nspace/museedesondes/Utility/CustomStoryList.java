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

public class CustomStoryList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] title;
    private final String[] description;
    private final Integer[] imageId;
    public CustomStoryList(Activity context,
                           String[] web, String[] description, Integer[] imageId) {
        super(context, R.layout.storyline_item_layout, web);
        this.context = context;
        this.title = web;
        this.description = description;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.storyline_item_layout, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView descriptField = (TextView) rowView.findViewById(R.id.description);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);


        txtTitle.setText(title[position]);
        descriptField.setText(description[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
