package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Text")
public class Text extends ExhibitionContent{
    private String content;

    public Text(@JsonProperty("id") String id,
                @JsonProperty("title") String title,
                @JsonProperty("language")String language,
                @JsonProperty("htmlContent")String content) {
        super(id, title, language);
        this.content = content;
    }

    public String getContent() {
        return content;
    }


}
