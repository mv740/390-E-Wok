package com.example.nspace.museedesondes.Model;

import com.example.nspace.museedesondes.Deserializer.AudioDeserialize;
import com.example.nspace.museedesondes.Deserializer.ImageDeserialize;
import com.example.nspace.museedesondes.Deserializer.VideoDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

/**
 * Created by michal on 2/19/2016.
 */
public class Media {

    @JsonDeserialize(using = VideoDeserialize.class)
    private ArrayList<Video> videos;
    @JsonDeserialize(using = AudioDeserialize.class)
    private ArrayList<Audio> audios;
    @JsonDeserialize(using = ImageDeserialize.class)
    private ArrayList<Image> images;

    public Media(@JsonProperty("video") ArrayList<Video> videos,
                 @JsonProperty("audio") ArrayList<Audio> audios,
                 @JsonProperty("image") ArrayList<Image> images) {

        this.videos = videos;
        this.audios = audios;
        this.images = images;
    }

    public ArrayList<Video> getVideos() {
        return videos;
    }

    public ArrayList<Audio> getAudios() {
        return audios;
    }

    public ArrayList<Image> getImages() {
        return images;
    }
}
