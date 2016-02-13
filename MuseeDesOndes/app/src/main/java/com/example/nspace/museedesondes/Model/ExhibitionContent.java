package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("ExhibitionContent")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value=Audio.class, name="Audio"),
        @JsonSubTypes.Type(value=Video.class, name="Video"),
        @JsonSubTypes.Type(value=Image.class, name="Image"),
        @JsonSubTypes.Type(value=QRCode.class, name="QRCode"),
        @JsonSubTypes.Type(value=Text.class, name="Text")
})
public class ExhibitionContent {
    private String id;
    private String title;
    private String language;

    public ExhibitionContent(@JsonProperty("id") String id,
                             @JsonProperty("title") String title,
                             @JsonProperty("language")String language) {
        this.id = id;
        this.title = title;
        this.language = language;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLanguage() {
        return language;
    }
}
