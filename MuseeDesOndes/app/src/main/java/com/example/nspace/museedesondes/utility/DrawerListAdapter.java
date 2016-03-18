package com.example.nspace.museedesondes.utility;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nspace.museedesondes.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by NSPACE on 2016-02-09.
 */

public class DrawerListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> items;
    private final List<Drawable> icons;
    public DrawerListAdapter(Activity context,
                             List<String> items, List<Drawable> icons) {
        super(context, R.layout.drawer_list_item, items);
        this.context = context;
        this.items = items;
        this.icons = icons;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.drawer_list_item, null, true);

        TextView itemText = (TextView) rowView.findViewById(R.id.drawer_list_item_text);
        ImageView icon = (ImageView) rowView.findViewById(R.id.drawer_list_item_icon);

        itemText.setText(items.get(position));
        icon.setImageDrawable(icons.get(position));

        return rowView;
    }
}
