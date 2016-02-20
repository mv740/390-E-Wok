package com.example.nspace.museedesondes.Model;

import android.content.Context;

import com.example.nspace.museedesondes.Deserializer.AudioDeserialize;
import com.example.nspace.museedesondes.Deserializer.ImageDeserialize;
import com.example.nspace.museedesondes.Deserializer.VideoDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
@JsonTypeName("PointOfInterest")
public class PointOfInterest extends Node {

    private BeaconInformation beaconInformation;
    private ArrayList<PointOfInterestDescription> descriptions;
    private Media media;
    private ArrayList<StoryPoint> storyPoint;


    public PointOfInterest(@JsonProperty("id") int id,
                           @JsonProperty("floorID") int floorID,
                           @JsonProperty("x") double x,
                           @JsonProperty("y") double y,
                           @JsonProperty("beaconInformation") BeaconInformation beaconInformation,
                           @JsonProperty("title") ArrayList<PointOfInterestDescription> descriptions,
                           @JsonProperty("media") Media media,
                           @JsonProperty("storyPoint") ArrayList<StoryPoint> storyPoint) {
        super(id, floorID, x, y);
        this.beaconInformation = beaconInformation;
        this.descriptions = descriptions;
        this.media = media;
        this.storyPoint = storyPoint;
    }

    public ArrayList<StoryPoint> getStoryPoint() {
        return storyPoint;
    }

    public BeaconInformation getBeaconInformation() {
        return beaconInformation;
    }

    public ArrayList<Video> getVideos() {
        return media.getVideos();
    }

    public ArrayList<Audio> getAudios() {
        return media.getAudios();
    }

    public ArrayList<Image> getImages() {
        return media.getImages();
    }

    @JsonSetter("description")
    private void setDescriptions(ArrayList<PointOfInterestDescription> descriptions) {

        for (int i = 0; i < descriptions.size(); i++) {
            this.descriptions.get(i).setDescription(descriptions.get(i).getDescription());
        }
    }


    public ArrayList<PointOfInterestDescription> getDescriptions() {
        return descriptions;
    }


    /**
     * Get every Image in the language that was saved in your profile.
     *
     * @param context current activity context
     * @return image in locale language
     */
    public ArrayList<Image> getLocaleImages(Context context)
    {
        ArrayList<Image> images = new ArrayList<>();
        for (Image image : media.getImages())
        {
            if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(image.getLanguage().name()))
            {
                  images.add(image);
            }
        }
        return images;
    }

    /**
     * Get every Audio in the language that was saved in your profile.
     *
     * @param context current activity context
     * @return audio in locale language
     */
    public ArrayList<Audio> getLocaleAudios(Context context)
    {
        ArrayList<Audio> audios = new ArrayList<>();
        for (Audio audio : media.getAudios())
        {
            if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(audio.getLanguage().name()))
            {
                audios.add(audio);
            }
        }
        return audios;
    }

    /**
     * Get Video in the language that was saved in your profile.
     *
     * @param context current activity context
     * @return video in locale language
     */
    public ArrayList<Video> getLocaleVideos(Context context)
    {
        ArrayList<Video> videos = new ArrayList<>();
        for (Video video : media.getVideos())
        {
            if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(video.getLanguage().name()))
            {
                  videos.add(video);
            }
        }
        return videos;
    }

    /**
     * Get Description in the language that was saved in your profile.
     *
     * @param context current activity context
     * @return description in locale language
     */
    public PointOfInterestDescription getLocaleDescription(Context context)
    {
        for (PointOfInterestDescription description : descriptions)
        {
            if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(description.getLanguage().name()))
            {
                return  description;
            }
        }
        return null;
    }
}
