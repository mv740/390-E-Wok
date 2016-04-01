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

import java.io.File;
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
        return Drawable.createFromPath(getAbsoluteFilePath(context, storyLine.getImagePath()));
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

    /**
     * Get the width and height of a floorPlan's image
     *
     * @param floorId
     * @param floorPlans
     * @param context
     * @return
     */
    @NonNull
    public static BitmapFactory.Options getFloorImageDimensionOptions(int floorId, List<FloorPlan> floorPlans, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        String filePath = getAbsoluteFilePath(context, searchFloorPlanById(floorId, floorPlans).getImagePath());
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

    public static String getAbsoluteFilePath(Context context, String fileName)
    {
        return context.getFilesDir()+"/"+fileName;
    }

    /**
     * Remove all the path before the filename from the string
     *
     * @param path
     * @return
     */
    public static String getFilenameWithoutDirectories(String path)
    {
        int index = path.lastIndexOf(File.separatorChar);
        return path.substring(index+1);
    }

    /**
     * http://stackoverflow.com/questions/1184176/how-can-i-safely-encode-a-string-in-java-to-use-as-a-filename
     * @param inputName
     * @return
     */
    private static String sanitizeFilename(String inputName) {
        return inputName.replaceAll("^[a-zA-Z0-9-_\\.]", "_");
    }


    /**
     *  sanitized fileName
     * @param fileName
     * @return
     */
    public static String getSanitizedFileNameWithoutDirectories(String fileName)
    {
        return getFilenameWithoutDirectories(sanitizeFilename(fileName));
    }


}
