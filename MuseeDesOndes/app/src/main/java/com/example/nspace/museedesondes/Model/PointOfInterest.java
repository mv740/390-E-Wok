package com.example.nspace.museedesondes.model;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;

/**
 * Created by michal on 1/28/2016.
 */
@JsonTypeName("PointOfInterest")
public class PointOfInterest extends Node {

    private BeaconInformation beaconInformation;
    private ArrayList<PointOfInterestDescription> descriptions;
    private Media media;
    private ArrayList<StoryPoint> storyPoints;


    public PointOfInterest(@JsonProperty("id") int id,
                           @JsonProperty("floorID") int floorID,
                           @JsonProperty("x") double x,
                           @JsonProperty("y") double y,
                           @JsonProperty("beaconInformation") BeaconInformation beaconInformation,
                           @JsonProperty("title") ArrayList<PointOfInterestDescription> descriptions,
                           @JsonProperty("media") Media media,
                           @JsonProperty("storyPoint") ArrayList<StoryPoint> storyPoints) {
        super(id, floorID, x, y);
        this.beaconInformation = beaconInformation;
        this.descriptions = descriptions;
        this.media = media;
        this.storyPoints = storyPoints;
    }

    public ArrayList<StoryPoint> getStoryPoints() {
        return storyPoints;
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


    /**
     * Get specific description related to the storyline, if none found  return the default description
     *
     * @param storylineID
     * @param context
     * @return
     */
    public PointOfInterestDescription getStoryRelatedDescription(int storylineID, Context context)
    {
        for(StoryPoint storyPoint : this.storyPoints)
        {
            if(storyPoint.getStorylineID() == storylineID)
            {

                for (PointOfInterestDescription description : storyPoint.getStorylineAssociatedDescription())
                {
                    if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(description.getLanguage().name()))
                    {
                        return  description;
                    }
                }
            }
        }


        return getLocaleDescription(context);
    }

    /**
     * Get specific Audios related to the storyline, if none found  return the default audios
     *
     * @param storylineID
     * @param context
     * @return
     */
    public ArrayList<Audio> getStoryRelatedAudios(int storylineID, Context context)
    {
        ArrayList<Audio> relatedAudios = new ArrayList<>();
        for(StoryPoint storyPoint : this.storyPoints)
        {
            if(storyPoint.getStorylineID() == storylineID)
            {
                for (Audio audio : storyPoint.getAudios())
                {
                    if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(audio.getLanguage().name()))
                    {
                        relatedAudios.add(audio);
                    }
                }
                if(relatedAudios.size()>0)
                    return relatedAudios;
            }
        }

        return getLocaleAudios(context);
    }

    /**
     * Get specific Videos related to the storyline, if none found  return the default Videos
     *
     * @param storylineID
     * @param context
     * @return
     */
    public ArrayList<Video> getStoryRelatedVideos(int storylineID, Context context)
    {
        ArrayList<Video> relatedVideos = new ArrayList<>();
        for(StoryPoint storyPoint : this.storyPoints)
        {
            if(storyPoint.getStorylineID() == storylineID)
            {
                for (Video video : storyPoint.getVideos())
                {
                    if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(video.getLanguage().name()))
                    {
                        relatedVideos.add(video);
                    }
                }
                if(relatedVideos.size()>0)
                    return relatedVideos;
            }
        }

        return getLocaleVideos(context);
    }

    /**
     * Get specific Images related to the storyline, if none found  return the default images
     *
     * @param storylineID
     * @param context
     * @return
     */
    public ArrayList<Image> getStoryRelatedImages(int storylineID, Context context)
    {
        ArrayList<Image> relatedImages = new ArrayList<>();
        for(StoryPoint storyPoint : this.storyPoints)
        {
            if(storyPoint.getStorylineID() == storylineID)
            {
                for (Image image : storyPoint.getImages())
                {
                    if(context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(image.getLanguage().name()))
                    {
                        relatedImages.add(image);
                    }
                }
                if(relatedImages.size()>0)
                    return relatedImages;
            }
        }

        return getLocaleImages(context);
    }

}
