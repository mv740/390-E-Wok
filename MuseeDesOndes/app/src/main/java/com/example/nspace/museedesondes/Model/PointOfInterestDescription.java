package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/18/2016.
 */
@JsonTypeName("PointOfInterestDescription")
public class PointOfInterestDescription {

    private String title;
    private String summary;
    private Language language;

    public PointOfInterestDescription(@JsonProperty("language") Language language,
                                      @JsonProperty("summary") String summary,
                                      @JsonProperty("title") String title) {
        this.title = title;
        this.summary = summary;
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public Language getLanguage() {
        return language;
    }
}
