package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.Enumeration;

/**
 * Created by michal on 2/3/2016.
 */
@JsonTypeName("Language")
public class Language {

    private String language;
    private String data;

    public Language(@JsonProperty("language") String language, @JsonProperty("data") String data) {
        this.language = language;
        this.data = data;
    }

    public String getLanguage() {
        return language;
    }

    public String getData() {
        return data;
    }

}
