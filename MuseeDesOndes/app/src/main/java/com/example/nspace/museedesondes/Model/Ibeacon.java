package com.example.nspace.museedesondes.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/3/2016.
 */
@JsonTypeName("iBeacon")
public class Ibeacon {

    private String beacon_uuid;
    private int beacon_major;
    private int beacon_minor;

    public Ibeacon(@JsonProperty("beacon_uuid") String beacon_uuid,
                   @JsonProperty("beacon_major")int beacon_major,
                   @JsonProperty("beacon_minor")int beacon_minor) {
        this.beacon_uuid = beacon_uuid;
        this.beacon_major = beacon_major;
        this.beacon_minor = beacon_minor;
    }

    public String getBeacon_uuid() {
        return beacon_uuid;
    }

    public int getBeacon_major() {
        return beacon_major;
    }

    public int getBeacon_minor() {
        return beacon_minor;
    }

    @Override
    public String toString() {
        return "Ibeacon{" +
                "beacon_uuid='" + beacon_uuid + '\'' +
                ", beacon_major=" + beacon_major +
                ", beacon_minor=" + beacon_minor +
                '}';
    }
}
