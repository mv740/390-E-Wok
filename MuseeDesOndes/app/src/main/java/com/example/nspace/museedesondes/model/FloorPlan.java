package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/9/2016.
 */
@JsonTypeName("FloorPlan")
public class FloorPlan {
    private int id;
    private String imagePath;
    private int imageWidth;
    private int imageHeight;

    public FloorPlan(@JsonProperty("floorID")int id,
                     @JsonProperty("imagePath")String imagePath,
                     @JsonProperty("imageWidth")int imageWidth,
                     @JsonProperty("imageHeight")int imageHeight) {
        this.id = id;
        this.imagePath = imagePath;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public int getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
