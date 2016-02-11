package com.example.nspace.museedesondes.Model;

/**
 * Created by michal on 2/11/2016.
 */
public class Video extends ExhibitionContent{
    private String filePath;
    private int durationInSeconds;
    private int resolution;
    private String encoding;

    public Video(String id, String title, String language, String filePath, int durationInSeconds, int resolution, String encoding) {
        super(id, title, language);
        this.filePath = filePath;
        this.durationInSeconds = durationInSeconds;
        this.resolution = resolution;
        this.encoding = encoding;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public int getResolution() {
        return resolution;
    }

    public String getEncoding() {
        return encoding;
    }
}