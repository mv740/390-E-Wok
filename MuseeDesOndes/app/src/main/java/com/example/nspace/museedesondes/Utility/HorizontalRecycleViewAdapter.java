package com.example.nspace.museedesondes.utility;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nspace.museedesondes.MapActivity;
import com.example.nspace.museedesondes.model.Image;
import com.example.nspace.museedesondes.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 3/1/2016.
 */

    public class HorizontalRecycleViewAdapter extends RecyclerView.Adapter<HorizontalRecycleViewAdapter.SingleItemRowHolder> {

        private List<Image> itemsList;
        private MapActivity mContext;

        public HorizontalRecycleViewAdapter(MapActivity context, List<Image> itemsList) {
            this.itemsList = itemsList;
            this.mContext = context;
        }

        @Override
        public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.poi_panel_pic_item, null);
            SingleItemRowHolder mh = new SingleItemRowHolder(v);
            return mh;
        }

        @Override
        public void onBindViewHolder(SingleItemRowHolder holder, int i) {
            Image item = itemsList.get(i);
            int id = mContext.getResources().getIdentifier(item.getPath(), "drawable", mContext.getPackageName());
            holder.imageResource.setImageResource(id);
        }

        @Override
        public int getItemCount() {
            return (null != itemsList ? itemsList.size() : 0);
        }

        public class SingleItemRowHolder extends RecyclerView.ViewHolder {

            protected TextView title;
            protected ImageView imageResource;
            public SingleItemRowHolder(View view) {
                super(view);
                this.imageResource = (ImageView) view.findViewById(R.id.poi_panel_pic_item_imageview);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.poiImgOnClick(v);
                    }
                });
            }
        }
    }

