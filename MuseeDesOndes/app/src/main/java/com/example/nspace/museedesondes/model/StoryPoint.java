package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 2/19/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("StoryPoint")
public class StoryPoint {
    private  int storylineID;
    private List<PointOfInterestDescription> storylineAssociatedDescription;
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

    public List<PointOfInterestDescription> getStorylineAssociatedDescription() {
        return storylineAssociatedDescription;
    }

    public List<Video> getVideos() {
        return media.getVideos();
    }

    public List<Audio> getAudios() {
        return media.getAudios();
    }

    public List<Image> getImages() {
        return media.getImages();
    }

}
