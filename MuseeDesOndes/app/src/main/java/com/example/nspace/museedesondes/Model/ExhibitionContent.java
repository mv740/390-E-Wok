package com.example.nspace.museedesondes.Model;

/**
 * Created by michal on 2/11/2016.
 */
public class ExhibitionContent {
    private String id;
    private String title;
    private String language;

    public ExhibitionContent(String id, String title, String language) {
        this.id = id;
        this.title = title;
        this.language = language;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }
}
