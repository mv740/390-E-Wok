package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
public class StoryLine {

    private int id;
    private String imagePath;
    private int walkingTimeInMinutes;
    private int floorsCovered;
    private ArrayList<Integer> path; // used only to get reference node
    private ArrayList<Node> nodes;
    private ArrayList<StoryLineDescription> descriptions;

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

    public ArrayList<Integer> getPath() {
        return path;
    }

    public void addNodeReference(Node node) {
        this.nodes.add(node);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<StoryLineDescription> getDescriptions() {
        return descriptions;
    }

    @JsonSetter("description")
    public void setDescriptions(ArrayList<StoryLineDescription> descriptions) {

        for(int i =0; i<descriptions.size(); i++)
        {
            this.descriptions.get(i).setDescription(descriptions.get(i).getDescription());
        }
    }
}
