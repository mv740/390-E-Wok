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

    public PointOfInterest(@JsonProperty("id") int id,
                           @JsonProperty("floorPlan") FloorPlan floor,
                           @JsonProperty("x") double x,
                           @JsonProperty("y") double y,
                           @JsonProperty("beaconInformation") BeaconInformation beaconInformation,
                           @JsonProperty("text") ArrayList<Text> text,
                           @JsonProperty("video") ArrayList<Video> video,
                           @JsonProperty("audio") ArrayList<Audio> audio,
                           @JsonProperty("image") ArrayList<Image> images,
                           @JsonProperty("qrcode") ArrayList<QRCode> qrCodes) {
        super(id, floor, x, y);
        this.beaconInformation = beaconInformation;
        this.text = text;
        this.video = video;
        this.audio = audio;
        this.images = images;
        this.qrCodes = qrCodes;
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

    public ArrayList<QRCode> getQrCodes() {
        return qrCodes;
    }
}
