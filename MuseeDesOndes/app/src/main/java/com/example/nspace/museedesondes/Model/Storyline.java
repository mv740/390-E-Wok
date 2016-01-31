package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
/**
 * Created by michal on 1/28/2016.
 */
public class Storyline {

    private String name;
    private String description;
    private ArrayList IdList;

    public Storyline(@JsonProperty("name")String name, @JsonProperty("description")String description,@JsonProperty("ids") ArrayList idList) {
        this.name = name;
        this.description = description;
        IdList = idList;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList getIdList() {
        return IdList;
    }

    @Override
    public String toString() {
        return "Storyline{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", IdList=" + IdList +
                '}';
    }
}
