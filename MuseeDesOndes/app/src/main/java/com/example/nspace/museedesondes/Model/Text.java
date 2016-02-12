package com.example.nspace.museedesondes.Model;

/**
 * Created by michal on 2/11/2016.
 */
public class Text extends ExhibitionContent{
    private String content;

    public Text(String id, String title, String language, String content) {
        super(id, title, language);
        this.content = content;
    }

}
