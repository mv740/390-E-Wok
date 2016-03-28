package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Image")
public class Image extends Content {

    private String path;

    public Image(@JsonProperty("caption") String title,
                 @JsonProperty("language")Language language,
                 @JsonProperty("path")String filePath) {
        super(title, language);
        this.path = filePath;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
