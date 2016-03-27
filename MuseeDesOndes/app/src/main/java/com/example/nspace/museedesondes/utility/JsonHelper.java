package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by michal on 1/29/2016.
 */
public class JsonHelper {

    //based on source https://gist.github.com/nisrulz/47bd5c44a05db54e3628
    public static String loadJSON(String filename, Context context) {
        String json;
        try {

            //InputStream is = context.getAssets().open(filename);
            FileInputStream is = new FileInputStream(new File(context.getCacheDir(),filename));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Log.d("JsonHelper", Log.getStackTraceString(ex));
            return null;
        }
        return json;
    }

    public static FileInputStream loadJsonFile(String filename, Context context)
    {
        FileInputStream is = null;
        try {
             is = new FileInputStream(new File(context.getCacheDir(),filename));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return is;
    }
}
