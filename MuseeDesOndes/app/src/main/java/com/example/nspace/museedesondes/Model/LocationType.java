package com.example.nspace.museedesondes.Model;

/**
 * Created by michal on 2/6/2016.
 */
public enum  LocationType {
    STAIRS, ENTRANCE, EXIT, MAIN_OFFICE, WASHROOM;

    //required to parse json locationType string to a enum
    public static LocationType getEnum(String code){
        switch (code)
        {
            case "STAIRS" : return STAIRS;
            case "ENTRANCE" : return ENTRANCE;
            case "EXIT" : return EXIT;
            case "MAIN_OFFICE" : return MAIN_OFFICE;
            case "WASHROOM" : return WASHROOM;
            default: return  null;
        }
    }
}
