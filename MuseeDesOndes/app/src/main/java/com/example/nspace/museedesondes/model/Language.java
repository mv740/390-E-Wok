package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/3/2016.
 */
@JsonTypeName("Language")
public enum  Language {
    //http://stackoverflow.com/questions/7973023/what-is-the-list-of-supported-languages-locales-on-android
    //http://developer.android.com/training/basics/supporting-devices/languages.html
    //setting directly the enum using jackson
    @JsonProperty("en_US")en_US,
    @JsonProperty("fr")fr,
    @JsonProperty("de_DE")de_DE;  //German, Germany (de_DE)

}