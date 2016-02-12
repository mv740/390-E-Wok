package com.example.nspace.museedesondes.Model;

/**
 * Created by michal on 2/11/2016.
 */
public class Image extends ExhibitionContent {

    private String filePath;
    private int width;
    private int heigth;

    public Image(String id, String title, String language) {
        super(id, title, language);
    }

    public String getFilePath() {
        return filePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeigth() {
        return heigth;
    }
}
