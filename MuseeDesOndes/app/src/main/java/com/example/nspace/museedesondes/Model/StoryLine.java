package com.example.nspace.museedesondes.model;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 1/28/2016.
 */
public class StoryLine {

    private int id;
    private String imagePath;
    private int walkingTimeInMinutes;
    private int floorsCovered;
    private List<Integer> path; // used only to get reference node
    private List<Node> nodes;
    private List<StoryLineDescription> descriptions;

    public StoryLine(@JsonProperty("id") int id,
                     @JsonProperty("thumbnail") String imagePath,
                     @JsonProperty("title") ArrayList<StoryLineDescription> descriptions,
                     @JsonProperty("walkingTimeInMinutes") int walkingTimeInMinutes,
                     @JsonProperty("floorsCovered") int floorsCovered,
                     @JsonProperty("path") ArrayList<Integer> path) {
        this.id = id;
        this.walkingTimeInMinutes = walkingTimeInMinutes;
        this.floorsCovered = floorsCovered;
        this.path = path;
        this.imagePath = imagePath;
        this.nodes = new ArrayList<>();
        this.descriptions = descriptions;

    }

    public int getId() {
        return id;
    }

    public int getWalkingTimeInMinutes() {
        return walkingTimeInMinutes;
    }

    public int getFloorsCovered() {
        return floorsCovered;
    }

    public String getImagePath() {
        return imagePath;
    }

    public List<Integer> getPath() {
        return path;
    }

    public void addNodeReference(Node node) {
        this.nodes.add(node);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<StoryLineDescription> getDescriptions() {
        return descriptions;
    }

    @JsonSetter("description")
    public void setDescriptions(ArrayList<StoryLineDescription> descriptions) {

        for(int i =0; i<descriptions.size(); i++)
        {
            this.descriptions.get(i).setDescription(descriptions.get(i).getDescription());
        }
    }


    /**
     * Get Description in the language that was saved in your profile.
     *
     * @param context current activity context
     * @return description in locale language
     */
    public StoryLineDescription getLocaleDescription(Context context)
    {
        for (StoryLineDescription description : descriptions)
        {
            if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(description.getLanguage().name()))
            {
                return  description;
            }
        }
        return null;
    }
}
