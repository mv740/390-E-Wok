package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/3/2016.
 */
@JsonTypeName("Language")
public enum Language {
    //http://stackoverflow.com/questions/7973023/what-is-the-list-of-supported-languages-locales-on-android
    //http://developer.android.com/training/basics/supporting-devices/languages.html
    //setting directly the enum using jackson
    en_US, fr;

    public static Language getLanguage(String language) {
        switch (language.toLowerCase()) {
            case "en":
                return en_US;
            case "fr":
                return fr;
            default:
                return null;
        }
    }
    }
