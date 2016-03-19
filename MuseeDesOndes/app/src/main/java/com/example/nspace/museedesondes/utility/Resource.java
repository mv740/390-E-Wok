package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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


    public static Bitmap retriveVideoFrameFromVideo(String p_videoPath)
    {
        Bitmap m_bitmap = null;
        MediaMetadataRetriever m_mediaMetadataRetriever = null;
        try
        {
            m_mediaMetadataRetriever = new MediaMetadataRetriever();
            m_mediaMetadataRetriever.setDataSource(p_videoPath);
            m_bitmap = m_mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception m_e)
        {
            Log.e("ResourceError", Log.getStackTraceString(m_e));
        }
        finally
        {
            if (m_mediaMetadataRetriever != null)
            {
                m_mediaMetadataRetriever.release();
            }
        }
        return m_bitmap;
    }


}
