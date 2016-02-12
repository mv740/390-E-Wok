package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("QRCode")
public class QRCode extends ExhibitionContent {
    private String encodingString;
    private String artifactTitle;
    private String artifactDescription;
    private String artifactImagePath;

    public QRCode(@JsonProperty("id") String id,
                  @JsonProperty("title") String title,
                  @JsonProperty("language")String language,
                  @JsonProperty("encodingString")String encodingString,
                  @JsonProperty("artifactTitle")String artifactTitle,
                  @JsonProperty("artifactDescription")String artifactDescription,
                  @JsonProperty("artifactImagePath")String artifactImagePath) {
        super(id, title, language);
        this.encodingString = encodingString;
        this.artifactTitle = artifactTitle;
        this.artifactDescription = artifactDescription;
        this.artifactImagePath = artifactImagePath;

    }

    public String getEncodingString() {
        return encodingString;
    }

    public String getArtifactTitle() {
        return artifactTitle;
    }

    public String getArtifactDescription() {
        return artifactDescription;
    }

    public String getArtifactImagePath() {
        return artifactImagePath;
    }
}
