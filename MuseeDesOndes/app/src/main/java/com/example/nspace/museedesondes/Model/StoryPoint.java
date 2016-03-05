package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;

/**
 * Created by michal on 2/19/2016.
 */
@JsonTypeName("StoryPoint")
public class StoryPoint {
    private  int storylineID;
    private ArrayList<PointOfInterestDescription> storylineAssociatedDescription;
    private Media media;

    public StoryPoint( @JsonProperty("storylineID")int storylineID,
                       @JsonProperty("title") ArrayList<PointOfInterestDescription> storylineAssociatedDescription,
                       @JsonProperty("media") Media media) {
        this.storylineID = storylineID;
        this.storylineAssociatedDescription = storylineAssociatedDescription;
        this.media = media;
    }

    @JsonSetter("description")
    private void setStorylineAssociatedDescription(ArrayList<StoryLineDescription> descriptions) {

        for(int i =0; i<descriptions.size(); i++)
        {
            this.storylineAssociatedDescription.get(i).setDescription(descriptions.get(i).getDescription());
        }
    }

    public int getStorylineID() {
        return storylineID;
    }

    public ArrayList<PointOfInterestDescription> getStorylineAssociatedDescription() {
        return storylineAssociatedDescription;
    }

    public ArrayList<Video> getVideos() {
        return media.getVideos();
    }

    public ArrayList<Audio> getAudios() {
        return media.getAudios();
    }

    public ArrayList<Image> getImages() {
        return media.getImages();
    }

}
