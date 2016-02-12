package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Image")
public class Image extends ExhibitionContent {

    private String filePath;
    private int width;
    private int heigth;

    public Image(@JsonProperty("id") String id,
                 @JsonProperty("title") String title,
                 @JsonProperty("language")String language,
                 @JsonProperty("filePath")String filePath,
                 @JsonProperty("width")int width,
                 @JsonProperty("heigth")int heigth) {
        super(id, title, language);
        this.filePath = filePath;
        this.width = width;
        this.heigth = heigth;
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
