package com.example.nspace.museedesondes.Model;

import com.example.nspace.museedesondes.Deserializer.AudioDeserialize;
import com.example.nspace.museedesondes.Deserializer.ImageDeserialize;
import com.example.nspace.museedesondes.Deserializer.VideoDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
@JsonTypeName("PointOfInterest")
public class PointOfInterest extends Node {

    private BeaconInformation beaconInformation;
    private ArrayList<PointOfInterestDescription> descriptions;
    private Media media;
    private ArrayList<StoryPoint> storyPoint;


    public PointOfInterest(@JsonProperty("id") int id,
                           @JsonProperty("floorID") int floorID,
                           @JsonProperty("x") double x,
                           @JsonProperty("y") double y,
                           @JsonProperty("beaconInformation") BeaconInformation beaconInformation,
                           @JsonProperty("title") ArrayList<PointOfInterestDescription> descriptions,
                           @JsonProperty("media") Media media,
                           @JsonProperty("storyPoint") ArrayList<StoryPoint> storyPoint) {
        super(id, floorID, x, y);
        this.beaconInformation = beaconInformation;
        this.descriptions = descriptions;
        this.media = media;
        this.storyPoint = storyPoint;
    }

    public ArrayList<StoryPoint> getStoryPoint() {
        return storyPoint;
    }

    public BeaconInformation getBeaconInformation() {
        return beaconInformation;
    }

    public ArrayList<Video> getVideo() {
        return media.getVideos();
    }

    public ArrayList<Audio> getAudio() {
        return media.getAudios();
    }

    public ArrayList<Image> getImages() {
        return media.getImages();
    }

    @JsonSetter("description")
    public void setDescriptions(ArrayList<PointOfInterestDescription> descriptions) {

        for (int i = 0; i < descriptions.size(); i++) {
            this.descriptions.get(i).setDescription(descriptions.get(i).getDescription());
        }
    }

    public ArrayList<PointOfInterestDescription> getDescriptions() {
        return descriptions;
    }
}
