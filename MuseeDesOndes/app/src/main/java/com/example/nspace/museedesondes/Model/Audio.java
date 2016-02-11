package com.example.nspace.museedesondes.Model;

/**
 * Created by michal on 2/11/2016.
 */
public class Audio extends ExhibitionContent {
    private String filePath;
    private int durationInSeconds;
    private String encoding;

    public Audio(String id, String title, String language, String filePath, int durationInSeconds, String encoding) {
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
