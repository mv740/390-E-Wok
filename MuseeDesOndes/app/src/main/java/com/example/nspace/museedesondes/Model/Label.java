package com.example.nspace.museedesondes.Model;

/**
 * Created by michal on 2/11/2016.
 */
public enum Label {
    RAMP, STAIRS, ELEVATOR, NONE, WASHROOM, EXIT, ENTRANCE, EMERGENCY_EXIT;

    //required to parse json label string to a enum
    public static Label getEnum(String code) {
        switch (code) {
            case "RAMP":
                return RAMP;
            case "STAIRS":
                return STAIRS;
            case "ENTRANCE":
                return ENTRANCE;
            case "EXIT":
                return EXIT;
            case "NONW":
                return NONE;
            case "WASHROOM":
                return WASHROOM;
            case "ELEVATOR":
                return ELEVATOR;
            case "EMERGENCY_EXIT":
                return EMERGENCY_EXIT;
            default:
                return null;
        }
    }
}
