package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.nspace.museedesondes.model.FloorPlan;
import com.example.nspace.museedesondes.model.StoryLine;

import java.util.List;


/**
 * Created by michal on 3/2/2016.
 */
public class Resource {

    public static Drawable getDrawableImageFromFileName(StoryLine storyLine, Context context)
    {
        final int resourceID = getDrawableResourceID(storyLine, context);

        return ContextCompat.getDrawable(context, resourceID);
    }

    public static Drawable getDrawableFromFileAbsolutePath(StoryLine storyLine, Context context)
    {
        String pathName = storyLine.getImagePath();
        String absolutePath = context.getCacheDir().getAbsolutePath()+"/"+pathName+".jpg";
        return Drawable.createFromPath(absolutePath);
    }


    public static int getDrawableResourceID(StoryLine storyLine, Context context) {
        Resources resources = context.getResources();
        return resources.getIdentifier(storyLine.getImagePath(), "drawable", context.getPackageName());
    }

    public static int getRawResourceID(String path, Context mContext) {
        return mContext.getResources().getIdentifier(path, "raw", mContext.getPackageName());
    }

    public static int getDrawableResourceIDFromPath(String path, Context mContext) {
        return mContext.getResources().getIdentifier(path, "drawable", mContext.getPackageName());
    }

    @NonNull
    public static BitmapFactory.Options getFloorImageDimensionOptions(int floorId, List<FloorPlan> floorPlans, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        String filePath = getImageFloorFilePath(context,searchFloorPlanById(floorId,floorPlans).getImagePath());
        Log.e("decode", filePath);
        BitmapFactory.decodeFile(filePath,options);

        return options;
    }

    public static int getFloorPlanResourceID(int id, List<FloorPlan> floorPlans, Context context) {

        FloorPlan floorPlan = searchFloorPlanById(id, floorPlans);
        return Resource.getDrawableResourceIDFromPath(floorPlan.getImagePath(), context);
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

    public static String getImageFloorFilePath(Context context, String fileName)
    {
        return context.getCacheDir().getAbsolutePath()+"/"+fileName+".png";
    }

    public static String getImageJPGFilePath(Context context, String fileName)
    {
        return context.getCacheDir().getAbsolutePath()+"/"+fileName+".jpg";
    }

    public static String getVideoFilePath(Context context, String fileName)
    {
        return context.getCacheDir().getAbsolutePath()+"/"+fileName+".mp4";
    }


}
