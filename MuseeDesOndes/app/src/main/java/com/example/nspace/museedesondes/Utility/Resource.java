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
        Resources resources = context.getResources();
        final int resourceID = resources.getIdentifier(storyLine.getImagePath(), "drawable", context.getPackageName());

        return ContextCompat.getDrawable(context, resourceID);
    }

}
