package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
/**
 * Created by michal on 1/28/2016.
 */
@JsonTypeName("PointOfInterest")
public class PointOfInterest extends Node {

    private int iBeacon_id;
    private String name;
    private String description;
    private String video;
    private String audio;
    private String picture;
    //todo int or string
    private String room;
    private ArrayList<String> tags;

    public PointOfInterest(@JsonProperty("id") int id, @JsonProperty("floor") int floor,
                           @JsonProperty("edge") ArrayList edge, @JsonProperty("coordinate.x") int coordinate_X, @JsonProperty("coordinate.y") int coordinate_Y,
                           @JsonProperty("iBeacon_id") int iBeacon_id, @JsonProperty("name") String name, @JsonProperty("description") String description,
                           @JsonProperty("video") String video, @JsonProperty("audio") String audio, @JsonProperty("picture") String picture,
                           @JsonProperty("room") String room,
                           @JsonProperty("tags")ArrayList<String> tags) {
        super(id, floor, edge, coordinate_X, coordinate_Y);
        this.iBeacon_id = iBeacon_id;
        this.name = name;
        this.description = description;
        this.video = video;
        this.audio = audio;
        this.picture = picture;
        this.room = room;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "iBeacon_id=" + iBeacon_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", video='" + video + '\'' +
                ", audio='" + audio + '\'' +
                ", picture='" + picture + '\'' +
                ", room='" + room + '\'' +
                ", tags=" + tags +
                '}';
    }

    public int getiBeacon_id() {
        return iBeacon_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVideo() {
        return video;
    }

    public String getAudio() {
        return audio;
    }

    public String getPicture() {
        return picture;
    }

    public String getRoom() {
        return room;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}
