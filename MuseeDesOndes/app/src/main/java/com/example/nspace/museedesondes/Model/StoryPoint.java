package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

/**
 * Created by michal on 2/19/2016.
 */
@JsonTypeName("StoryPoint")
public class StoryPoint {
    private  int storylineID;
    private ArrayList<StoryLineDescription> storyLineDescriptions;
    private Media media;

    public StoryPoint( @JsonProperty("storylineID")int storylineID,
                       @JsonProperty("title") ArrayList<StoryLineDescription> storyLineDescriptions,
                       @JsonProperty("media") Media media) {
        this.storylineID = storylineID;
        this.storyLineDescriptions = storyLineDescriptions;
        this.media = media;
    }

    @JsonSetter("description")
    public void setStoryLineDescriptions(ArrayList<StoryLineDescription> descriptions) {

        for(int i =0; i<descriptions.size(); i++)
        {
            this.storyLineDescriptions.get(i).setDescription(descriptions.get(i).getDescription());
        }
    }

    public int getStorylineID() {
        return storylineID;
    }

    public ArrayList<StoryLineDescription> getStoryLineDescriptions() {
        return storyLineDescriptions;
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

}
