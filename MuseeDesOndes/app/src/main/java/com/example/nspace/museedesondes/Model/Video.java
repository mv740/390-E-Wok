package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Video")
public class Video extends ExhibitionContent{
    private String filePath;
    private int durationInSeconds;
    private int resolution;
    private String encoding;

    public Video(@JsonProperty("id") String id,
                 @JsonProperty("title") String title,
                 @JsonProperty("language")String language,
                 @JsonProperty("language")String filePath,
                 @JsonProperty("durationInSeconds")int durationInSeconds,
                 @JsonProperty("resolution")int resolution,
                 @JsonProperty("encoding")String encoding) {
        super(id, title, language);
        this.filePath = filePath;
        this.durationInSeconds = durationInSeconds;
        this.resolution = resolution;
        this.encoding = encoding;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public int getResolution() {
        return resolution;
    }

    public String getEncoding() {
        return encoding;
    }
}