package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;

/**
 * Created by michal on 2/6/2016.
 */
@JsonTypeName("BuildingPoint")
public class BuildingPoint extends Node{

    private LocationType buildingLocationType;

    public BuildingPoint(@JsonProperty("id") int id,
                         @JsonProperty("floor") int floor,
                         @JsonProperty("edge") ArrayList edge,
                         @JsonProperty("coordinate") Coordinate coordinate,
                         @JsonProperty("locationType") String locationType) {
        super(id, floor, edge, coordinate);

        this.buildingLocationType = LocationType.getEnum(locationType);
    }

    public LocationType getBuildingLocationType() {
        return buildingLocationType;
    }

}
