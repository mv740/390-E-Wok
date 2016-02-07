package com.example.nspace.museedesondes.Utility;

import android.content.Context;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by michal on 1/29/2016.
 */
public class JsonHelper {

    //based on source https://gist.github.com/nisrulz/47bd5c44a05db54e3628
    public static String loadJSON(String filename, Context context) {
        String json;
        try {

            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
