package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/3/2016.
 */
@JsonTypeName("BeaconInformation")
public class BeaconInformation {

    private String UUID;
    private int major;
    private int minor;

    public BeaconInformation(@JsonProperty("uuid") String UUID,
                             @JsonProperty("major") int major,
                             @JsonProperty("minor") int minor) {
        this.UUID = UUID;
        this.major = major;
        this.minor = minor;
    }

    public String getUUID() {
        return UUID;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }
}
