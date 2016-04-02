package com.example.nspace.museedesondes.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Created by michal on 2/11/2016.
 */
@JsonTypeName("Label")
public enum Label {
    //setting directly the enum using jackson
    RAMP,
    STAIRS,
    ELEVATOR,
    NONE,
    WASHROOM,
    EXIT,
    ENTRANCE,
    EMERGENCY_EXIT;

    public static Label getLabel(String label)
    {
        switch (label.toUpperCase())
        {
            case "RAMP" : return RAMP;
            case "STAIRS" : return STAIRS;
            case "ELEVATOR" : return ELEVATOR;
            case "NONE" : return  NONE;
            case "WASHROOM" : return WASHROOM;
            case "EXIT" : return  EXIT;
            case "ENTRANCE" : return  ENTRANCE;
            case "EMERGENCY_EXIT" : return EMERGENCY_EXIT;
            default: return null;
        }
    }
}
