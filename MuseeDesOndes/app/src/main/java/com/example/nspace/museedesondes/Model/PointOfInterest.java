package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
@JsonTypeName("PointOfInterest")
public class PointOfInterest extends Node {

    private BeaconInformation beaconInformation;
    private ArrayList<Language> name;
    private ArrayList<Text> text;
    private ArrayList<Video> video;
    private ArrayList<Audio> audio;
    private ArrayList<Image> images;
    private ArrayList<QRCode> qrCodes;
    private String room;
    private ArrayList<String> tags;

    public PointOfInterest(@JsonProperty("id") int id,
                           @JsonProperty("floorPlan") FloorPlan floor,
                           @JsonProperty("edge") ArrayList edge,
                           @JsonProperty("coordinate") Coordinate coordinate,
                           @JsonProperty("beaconInformation") BeaconInformation beaconInformation,
                           @JsonProperty("name") ArrayList<Language> name,
                           @JsonProperty("text") ArrayList text,
                           @JsonProperty("video") ArrayList video,
                           @JsonProperty("audio") ArrayList audio,
                           @JsonProperty("image") ArrayList images,
                           @JsonProperty("qrcode") ArrayList qrCodes,
                           @JsonProperty("room") String room,
                           @JsonProperty("tags") ArrayList<String> tags) {
        super(id, floor, edge, coordinate);
        this.beaconInformation = beaconInformation;
        this.name = name;
        this.text = text;
        this.video = video;
        this.audio = audio;
        this.images = images;
        this.room = room;
        this.tags = tags;
    }


    public BeaconInformation getBeaconInformation() {
        return beaconInformation;
    }

    public ArrayList<Language> getName() {
        return name;
    }

    public ArrayList<Text> getText() {

//        Locale currentLocale = getResources().getConfiguration().locale;
//        String currentLanguage = currentLocale.getLanguage();
//        if(currentLanguage.equals("en_US")) {
//            return englishDescription;
//        } else if (currentLanguage.equals("fr")) {
//            return frenchDescription;
//        }

        return text;
    }

    public ArrayList<Video> getVideo() {
        return video;
    }

    public ArrayList<Audio> getAudio() {
        return audio;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public String getRoom() {
        return room;
    }

    public ArrayList<String> getTags() {
        return tags;
    }
}
