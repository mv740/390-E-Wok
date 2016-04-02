package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

/**
 * Created by sebastian on 2016-02-05.
 */
public class Preferences {

    private Preferences()
    {

    }

    public final static String DEFAULT_LANG = "en_US";

    private static Context appContext = null;
    private static String savedLang = null;

    public static void setAppContext(Context context) {
       appContext = context;
    }

    public static void saveLanguagePreference(String appLanguage) {
        SharedPreferences sharedPrefs = appContext.getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("savedLang", appLanguage);
        editor.commit();
        Log.d("Lang", "saved");
    }


    public static void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = appContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static void loadLanguagePreference() {

        //load saved language preference
        SharedPreferences sharedPrefs = appContext.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        savedLang = sharedPrefs.getString("savedLang", DEFAULT_LANG);

        //verify saved preference matches current language
        Locale currentLocale = appContext.getResources().getConfiguration().locale;
        String currentLanguage = currentLocale.getLanguage();

        if(!currentLanguage.equals(savedLang)) {
            setLocale(savedLang);
        }
    }
}
