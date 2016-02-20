package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Content")
@JsonSubTypes({
        @JsonSubTypes.Type(value=Audio.class, name="Audio"),
        @JsonSubTypes.Type(value=Video.class, name="Video"),
        @JsonSubTypes.Type(value=Image.class, name="Image"),
        @JsonSubTypes.Type(value=Text.class, name="Text")
})
public abstract class Content {
    private String caption;
    private Language language;

    public Content(@JsonProperty("caption") String caption,
                   @JsonProperty("language") Language language) {
        this.caption = caption;
        this.language = language;
    }

    public String getCaption() {
        return caption;
    }

    public Language getLanguage() {
        return language;
    }
}
