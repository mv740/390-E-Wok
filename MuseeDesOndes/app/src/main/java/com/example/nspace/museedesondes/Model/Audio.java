package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Audio")
public class Audio extends Content {
    private String filePath;
    private int durationInSeconds;
    private String encoding;

    public Audio(@JsonProperty("id") String id,
                 @JsonProperty("title") String title,
                 @JsonProperty("language")Language language,
                 @JsonProperty("filePath")String filePath,
                 @JsonProperty("durationInSeconds")int durationInSeconds,
                 @JsonProperty("encoding")String encoding) {
        super(id, title, language);
        this.filePath = filePath;
        this.durationInSeconds = durationInSeconds;
        this.encoding = encoding;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public String getEncoding() {
        return encoding;
    }
}
