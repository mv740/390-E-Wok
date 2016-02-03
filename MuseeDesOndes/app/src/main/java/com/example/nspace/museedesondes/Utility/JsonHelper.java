package com.example.nspace.museedesondes.Utility;

import android.app.Activity;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by michal on 1/29/2016.
 */
public class JsonHelper {

    public static String loadJSON(String filename, Activity activity) {
        String json = null;
        try {

            InputStream is = activity.getAssets().open((filename));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();

        }
        return json;
    }
}
