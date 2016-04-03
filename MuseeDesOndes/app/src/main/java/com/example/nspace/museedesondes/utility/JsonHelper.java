package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by michal on 1/29/2016.
 */
public class JsonHelper {

    private JsonHelper(){

    }

    //based on source https://gist.github.com/nisrulz/47bd5c44a05db54e3628
    public static FileInputStream loadJsonFile(String filename, Context context) {
        FileInputStream is = null;
        try {
            File jsonFile = new File(context.getFilesDir(), filename);
            Log.d("JsonHelper Path", jsonFile.getAbsolutePath());
            is = new FileInputStream(jsonFile);

        } catch (FileNotFoundException e) {
            Log.d("JsonHelper", Log.getStackTraceString(e));
        }

        return is;
    }
}
