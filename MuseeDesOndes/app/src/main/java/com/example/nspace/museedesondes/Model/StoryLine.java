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
    private ArrayList<Integer> IdList; // used only to get reference node
    private ArrayList<Node> nodes;

    public StoryLine(@JsonProperty("id") int id,
                     @JsonProperty("text") ArrayList<Text> text,
                     @JsonProperty("imagePath") String imagePath,
                     @JsonProperty("walkingTimeInMinutes") int walkingTimeInMinutes,
                     @JsonProperty("floorsCovered") int floorsCovered,
                     @JsonProperty("nodeIds") ArrayList<Integer> idList) {
        this.id = id;
        this.text = text;
        this.imagePath = imagePath;
        this.walkingTimeInMinutes = walkingTimeInMinutes;
        this.floorsCovered = floorsCovered;
        this.IdList = idList;
        this.nodes = new ArrayList<>();

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

    public ArrayList<Integer> getIdList() {
        return IdList;
    }

    public void addNodeReference(Node node) {
        this.nodes.add(node);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }
}
