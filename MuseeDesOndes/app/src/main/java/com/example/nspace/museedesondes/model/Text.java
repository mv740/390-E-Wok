package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Text")
public class Text extends Content {
    private String content;

    public Text(@JsonProperty("caption") String caption,
                @JsonProperty("language")Language language,
                @JsonProperty("content")String content) {
        super(caption, language);
        this.content = content;
    }

    public String getContent() {
        return content;
    }


}
