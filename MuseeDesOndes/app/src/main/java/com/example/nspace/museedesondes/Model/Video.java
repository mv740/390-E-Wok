package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Video")
public class Video extends Content {
    private String path;

    public Video(@JsonProperty("caption") String caption,
                 @JsonProperty("language")Language language,
                 @JsonProperty("path")String path) {
        super(caption, language);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}