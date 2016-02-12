package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;

/**
 * Created by michal on 2/6/2016.
 */
@JsonTypeName("ServicePoint")
public class ServicePoint extends Node{

    private Label serviceType;

    public ServicePoint(@JsonProperty("id") int id,
                        @JsonProperty("floor") int floor,
                        @JsonProperty("edge") ArrayList edge,
                        @JsonProperty("coordinate") Coordinate coordinate,
                        @JsonProperty("locationType") String serviceType) {
        super(id, floor, edge, coordinate);

        this.serviceType = Label.getEnum(serviceType);
    }

    public Label getServiceType() {
        return serviceType;
    }
}
