package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Image")
public class Image extends Content {

    private String filePath;
    private int width;
    private int height;

    public Image(@JsonProperty("id") String id,
                 @JsonProperty("title") String title,
                 @JsonProperty("language")Language language,
                 @JsonProperty("filePath")String filePath,
                 @JsonProperty("width")int width,
                 @JsonProperty("height")int height) {
        super(id, title, language);
        this.filePath = filePath;
        this.width = width;
        this.height = height;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
