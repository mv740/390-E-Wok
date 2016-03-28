package com.example.nspace.museedesondes.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nspace.museedesondes.MapActivity;
import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.model.Content;
import com.example.nspace.museedesondes.model.Image;
import com.example.nspace.museedesondes.model.Video;
import com.example.nspace.museedesondes.utility.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 3/1/2016.
 */

public class HorizontalRecycleViewAdapter extends RecyclerView.Adapter<HorizontalRecycleViewAdapter.SingleItemRowHolder> {

    private List<Content> contentList;
    private MapActivity mContext;

    public HorizontalRecycleViewAdapter(MapActivity context, List<Image> imageList, List<Video> videoList) {
        this.contentList = new ArrayList<>();
        this.contentList.addAll(videoList);
        this.contentList.addAll(imageList);
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

        //Image item = imageList.get(i);
        Content contentItem = contentList.get(i);
        if (contentItem instanceof Video) {

            holder.MediaResource.setTag("VIDEO");
            holder.MediaResource.setImageResource(R.drawable.videounlock_thumbnail_small);
            holder.VideoRessourceID = Resource.getRawResourceID(((Video) contentItem).getPath(), mContext);
            holder.VideoResourceFilePath = Resource.getVideoFilePath(mContext,((Video) contentItem).getPath());
        }
        if (contentItem instanceof Image) {
            int id = Resource.getDrawableResourceIDFromPath(((Image) contentItem).getPath(), mContext);
            String filePath = Resource.getImageFilePath(mContext, ((Image) contentItem).getPath());
            //holder.MediaResource.setImageResource(id);
            holder.MediaResource.setImageURI(Uri.parse(filePath));
            holder.MediaResource.setTag(filePath);
        }

    }


    @Override
    public int getItemCount() {
        return null != contentList ? contentList.size() : 0;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected ImageView MediaResource;
        protected int VideoRessourceID;
        protected String VideoResourceFilePath;

        public SingleItemRowHolder(View view) {
            super(view);
            this.MediaResource = (ImageView) view.findViewById(R.id.poi_panel_pic_item_imageview);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.poiPanelMediaOnClick(v, MediaResource, VideoRessourceID, VideoResourceFilePath);
                }
            });
        }
    }
}

