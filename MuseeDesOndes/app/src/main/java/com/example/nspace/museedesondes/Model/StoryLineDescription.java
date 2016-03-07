package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/18/2016.
 */

@JsonTypeName("StoryLineDescription")
public class StoryLineDescription {
    private String title;
    private String description;
    private Language language;

    public StoryLineDescription(@JsonProperty("language") Language language,
                                @JsonProperty("description") String description,
                                @JsonProperty("title") String title) {
        this.title = title;
        this.description = description;
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Language getLanguage() {
        return language;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
