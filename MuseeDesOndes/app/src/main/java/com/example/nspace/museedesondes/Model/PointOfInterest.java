package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by michal on 1/28/2016.
 */
@JsonTypeName("PointOfInterest")
public class PointOfInterest extends Node {

    private Ibeacon ibeacon;
    private ArrayList<Language> name;
    private ArrayList<Language> description;
    private ArrayList<Language> video;
    private ArrayList<Language> audio;
    private String picture;
    private String room;
    private ArrayList<String> tags;

    public PointOfInterest(@JsonProperty("id") int id,
                           @JsonProperty("floor") int floor,
                           @JsonProperty("edge") ArrayList edge,
                           @JsonProperty("coordinate") Coordinate coordinate,
                           @JsonProperty("iBeacon") Ibeacon ibeacon,
                           @JsonProperty("name") ArrayList<Language> name,
                           @JsonProperty("description") ArrayList<Language> description,
                           @JsonProperty("video") ArrayList<Language> video,
                           @JsonProperty("audio") ArrayList<Language> audio,
                           @JsonProperty("picture") String picture,
                           @JsonProperty("room") String room,
                           @JsonProperty("tags") ArrayList<String> tags) {
        super(id, floor, edge, coordinate);
        this.ibeacon = ibeacon ;
        this.name = name;
        this.description = description;
        this.video = video;
        this.audio = audio;
        this.picture = picture;
        this.room = room;
        this.tags = tags;
    }


    public Ibeacon getIbeacon() {
        return ibeacon;
    }

    public ArrayList<Language> getName() {
        return name;
    }

    public ArrayList<Language> getDescription() {

//        Locale currentLocale = getResources().getConfiguration().locale;
//        String currentLanguage = currentLocale.getLanguage();
//        if(currentLanguage.equals("en_US")) {
//            return englishDescription;
//        } else if (currentLanguage.equals("fr")) {
//            return frenchDescription;
//        }

        return description;
    }

    public ArrayList<Language> getVideo() {
        return video;
    }

    public ArrayList<Language> getAudio() {
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


    @Override
    public String toString() {
        return "PointOfInterest{" +
                "ibeacon=" + ibeacon +
                ", name=" + name +
                ", description=" + description +
                ", video='" + video + '\'' +
                ", audio='" + audio + '\'' +
                ", picture='" + picture + '\'' +
                ", room='" + room + '\'' +
                ", tags=" + tags +
                '}';
    }
}
