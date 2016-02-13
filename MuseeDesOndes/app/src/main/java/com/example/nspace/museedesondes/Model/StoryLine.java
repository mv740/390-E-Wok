package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
public class StoryLine {

    private int id;
    private ArrayList<Text> text;
    private String imagePath;
    private int walkingTimeInMinutes;
    private int floorsCovered;
    private ArrayList IdList;

    public StoryLine(@JsonProperty("id") int id,
                     @JsonProperty("text") ArrayList<Text> text,
                     @JsonProperty("imagePath") String imagePath,
                     @JsonProperty("walkingTimeInMinutes") int walkingTimeInMinutes,
                     @JsonProperty("floorsCovered") int floorsCovered,
                     @JsonProperty("nodeIds") ArrayList idList) {
        this.id = id;
        this.text = text;
        this.imagePath = imagePath;
        this.walkingTimeInMinutes = walkingTimeInMinutes;
        this.floorsCovered = floorsCovered;
        this.IdList = idList;

    }

    public int getId() {
        return id;
    }

    public ArrayList<Text> getText() {
        return text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getWalkingTimeInMinutes() {
        return walkingTimeInMinutes;
    }

    public int getFloorsCovered() {
        return floorsCovered;
    }

    public ArrayList getIdList() {
        return IdList;
    }
}
