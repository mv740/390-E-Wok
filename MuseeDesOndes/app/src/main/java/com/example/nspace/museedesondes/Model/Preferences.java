package com.example.nspace.museedesondes.Model;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.nspace.museedesondes.MainActivity;

import java.util.Locale;

/**
 * Created by sebastian on 2016-02-05.
 */
public class Preferences {

    public static Context appContext;
    public static String appLanguage;

    public static void setAppContext(Context context) {
       appContext = context;
    }

    public static void savePreferences(String appLanguage) {
        SharedPreferences sharedPrefs = appContext.getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("appLanguage", appLanguage);
        editor.commit();
    }

    public static void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = appContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static void loadLanguagePreferences() {
        SharedPreferences sharedPrefs = appContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        appLanguage = sharedPrefs.getString("appLanguage", appLanguage);
    }
}
