package com.example.nspace.museedesondes.Model;

/**
 * Created by micha on 2/9/2016.
 */
public enum TransitionType {

    CORRIDOR, DOOR, RAMP, STAIRS,ELEVATOR;

    //required to parse json TransiTionType string to a enum
    public static TransitionType getEnum(String code){
        switch (code)
        {
            case "CORRIDOR" : return CORRIDOR;
            case "DOOR" : return DOOR;
            case "RAMP" : return RAMP;
            case "STAIRS" : return STAIRS;
            case "ELEVATOR" : return ELEVATOR;
            default: return  null;
        }
    }
}
