package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
public class Storyline {

    private int id;
    private ArrayList<Language> name;
    private ArrayList<Language> description;
    private ArrayList IdList;

    public Storyline(@JsonProperty("id") int id, @JsonProperty("name") ArrayList<Language> name, @JsonProperty("description") ArrayList<Language> description, @JsonProperty("nodeIds") ArrayList idList) {
        this.id = id;
        this.name = name;
        this.description = description;
        IdList = idList;


    }

    public int getId() {
        return id;
    }

    public ArrayList<Language> getName() {
        return name;
    }

    public ArrayList<Language> getDescription() {
        return description;
    }

    public ArrayList getIdList() {
        return IdList;
    }
}
