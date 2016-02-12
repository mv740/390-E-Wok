package com.example.nspace.museedesondes.Model;

/**
 * Created by michal on 2/11/2016.
 */
public class QRCode extends ExhibitionContent {
    private String encodingString;
    private String artifactTitle;
    private String artifactDescription;
    private String artifactImagePath;

    public QRCode(String id, String title, String language, String encodingString, String artifactTitle, String artifactDescription, String artifactImagePath) {
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
