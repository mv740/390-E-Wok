package com.example.nspace.museedesondes.model;


import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 2/19/2016.
 */
public class Media {

    private List<Video> videos;
    private List<Audio> audios;
    private List<Image> images;

    public Media(@JsonProperty("video") ArrayList<Video> videos,
                 @JsonProperty("audio") ArrayList<Audio> audios,
                 @JsonProperty("image") ArrayList<Image> images) {

        this.videos = videos;
        this.audios = audios;
        this.images = images;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public List<Audio> getAudios() {
        return audios;
    }

    public List<Image> getImages() {
        return images;
    }
}
