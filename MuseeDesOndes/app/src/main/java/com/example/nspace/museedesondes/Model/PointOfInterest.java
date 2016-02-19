package com.example.nspace.museedesondes.Model;

import com.example.nspace.museedesondes.Deserializer.AudioDeserialize;
import com.example.nspace.museedesondes.Deserializer.ImageDeserialize;
import com.example.nspace.museedesondes.Deserializer.TextDeserialize;
import com.example.nspace.museedesondes.Deserializer.VideoDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
@JsonTypeName("PointOfInterest")
public class PointOfInterest extends Node {

    private BeaconInformation beaconInformation;
    private ArrayList<Language> name;
    @JsonDeserialize(using = TextDeserialize.class)
    private ArrayList<Text> text;
    @JsonDeserialize(using = VideoDeserialize.class)
    private ArrayList<Video> video;
    @JsonDeserialize(using = AudioDeserialize.class)
    private ArrayList<Audio> audio;
    @JsonDeserialize(using = ImageDeserialize.class)
    private ArrayList<Image> images;

    public PointOfInterest(@JsonProperty("id") int id,
                           @JsonProperty("floorPlan") FloorPlan floor,
                           @JsonProperty("x") double x,
                           @JsonProperty("y") double y,
                           @JsonProperty("beaconInformation") BeaconInformation beaconInformation,
                           @JsonProperty("text") ArrayList<Text> text,
                           @JsonProperty("video") ArrayList<Video> video,
                           @JsonProperty("audio") ArrayList<Audio> audio,
                           @JsonProperty("image") ArrayList<Image> images) {
        super(id, floor, x, y);
        this.beaconInformation = beaconInformation;
        this.text = text;
        this.video = video;
        this.audio = audio;
        this.images = images;
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

}
