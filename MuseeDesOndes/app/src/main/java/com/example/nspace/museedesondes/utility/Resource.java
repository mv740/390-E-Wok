package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.nspace.museedesondes.model.StoryLine;


/**
 * Created by michal on 3/2/2016.
 */
public class Resource {

    public static Drawable getDrawableImageFromFileName(StoryLine storyLine, Context context)
    {
        final int resourceID = getResourceID(storyLine, context);

        return ContextCompat.getDrawable(context, resourceID);
    }


    public static int getResourceID(StoryLine storyLine, Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(storyLine.getImagePath(), "drawable", context.getPackageName());
    }

    public static int getVideoResourceID(String path, Context mContext) {
        return mContext.getResources().getIdentifier(path, "raw", mContext.getPackageName());
    }

    public static int getResourceIDFromPath(String path, Context mContext) {
        return mContext.getResources().getIdentifier(path, "drawable", mContext.getPackageName());
    }



}
