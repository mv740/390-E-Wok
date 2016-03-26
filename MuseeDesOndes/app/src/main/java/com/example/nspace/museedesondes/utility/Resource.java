package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.StoryLine;

import java.util.List;


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

    @NonNull
    public static BitmapFactory.Options getFloorImageDimensionOptions(int floorId, List<FloorPlan> floorPlans, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), getFloorPlanResourceID(floorId, floorPlans, context), options);
        return options;
    }

    public static int getFloorPlanResourceID(int id, List<FloorPlan> floorPlans, Context context) {

        FloorPlan floorPlan = searchFloorPlanById(id, floorPlans);
        return Resource.getResourceIDFromPath(floorPlan.getImagePath(),context);
    }

    /**
     * Search correct floorPlan
     * floorPlan index is not the floorPlan id,  The Json could have floorPlan 3,5,8,1 in random order
     *
     * @param id
     * @return
     */
    public static FloorPlan searchFloorPlanById(int id, List<FloorPlan> floorPlans) {
        for (FloorPlan currentFloor : floorPlans) {
            if (currentFloor.getId() == id) {
                return currentFloor;
            }
        }
        return null;
    }


}
