package com.example.nspace.museedesondes.model;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 1/28/2016.
 */
@JsonTypeName("PointOfInterest")
public class PointOfInterest extends Node {

    private BeaconInformation beaconInformation;
    private List<PointOfInterestDescription> descriptions;
    private Media media;
    private List<StoryPoint> storyPoints;


    public PointOfInterest(@JsonProperty("id") int id,
                           @JsonProperty("floorID") int floorID,
                           @JsonProperty("x") double x,
                           @JsonProperty("y") double y,
                           @JsonProperty("ibeacon") BeaconInformation beaconInformation,
                           @JsonProperty("title") ArrayList<PointOfInterestDescription> descriptions,
                           @JsonProperty("media") Media media,
                           @JsonProperty("storyPoint") ArrayList<StoryPoint> storyPoints) {
        super(id, floorID, x, y);
        this.beaconInformation = beaconInformation;
        this.descriptions = descriptions;
        this.media = media;
        this.storyPoints = storyPoints;
    }

    public List<StoryPoint> getStoryPoints() {
        return storyPoints;
    }

    public BeaconInformation getBeaconInformation() {
        return beaconInformation;
    }

    @JsonSetter("description")
    private void setDescriptions(ArrayList<PointOfInterestDescription> descriptions) {

        for (int i = 0; i < descriptions.size(); i++) {
            this.descriptions.get(i).setDescription(descriptions.get(i).getDescription());
        }
    }


    public List<PointOfInterestDescription> getDescriptions() {
        return descriptions;
    }


    /**
     * Get every Image in the language that was saved in your profile.
     *
     * @param context current activity context
     * @return image in locale language
     */
    public List<Image> getLocaleImages(Context context) {
        List<Image> images = new ArrayList<>();
        for (Image image : media.getImages()) {
            if (context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(image.getLanguage().name())) {
                images.add(image);
            }
        }
        return images;
    }

    /**
     * @param context
     * @return
     */
    public List<Image> getAllImages(Context context) {
        List<Image> images = new ArrayList<>();
        for (Image image : media.getImages()) {
                images.add(image);
        }
        for(StoryPoint storyPointCurrent: storyPoints)
        {
            for (Image image : storyPointCurrent.getImages())
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
    public List<Audio> getLocaleAudios(Context context) {
        List<Audio> audios = new ArrayList<>();
        for (Audio audio : media.getAudios()) {
            if (context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(audio.getLanguage().name())) {
                audios.add(audio);
            }
        }
        return audios;
    }

    public List<Audio> getAllAudios(Context context) {
        List<Audio> audios = new ArrayList<>();
        for (Audio audio : media.getAudios()) {
                audios.add(audio);
        }
        for(StoryPoint storyPointCurrent: storyPoints)
        {
            for (Audio audio : storyPointCurrent.getAudios())
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
    public List<Video> getLocaleVideos(Context context) {
        List<Video> videos = new ArrayList<>();
        for (Video video : media.getVideos()) {
            if (context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(video.getLanguage().name())) {
                videos.add(video);
            }
        }
        return videos;
    }

    public List<Video> getAllVideos(Context context) {
        List<Video> videos = new ArrayList<>();
        for (Video video : media.getVideos()) {
                videos.add(video);
        }
        for(StoryPoint storyPointCurrent: storyPoints)
        {
            for (Video video : storyPointCurrent.getVideos())
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
    public PointOfInterestDescription getLocaleDescription(Context context) {
        for (PointOfInterestDescription description : descriptions) {
            if (context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(description.getLanguage().name())) {
                return description;
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
    public PointOfInterestDescription getStoryRelatedDescription(int storylineID, Context context) {
        for (StoryPoint storyPoint : this.storyPoints) {
            if (storyPoint.getStorylineID() == storylineID) {

                for (PointOfInterestDescription description : storyPoint.getStorylineAssociatedDescription()) {
                    if (context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(description.getLanguage().name())) {
                        return description;
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
    public List<Audio> getStoryRelatedAudios(int storylineID, Context context) {
        List<Audio> relatedAudios = new ArrayList<>();
        for (StoryPoint storyPoint : this.storyPoints) {
            if (storyPoint.getStorylineID() == storylineID) {
                for (Audio audio : storyPoint.getAudios()) {
                    if (context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(audio.getLanguage().name())) {
                        relatedAudios.add(audio);
                    }
                }
                if (relatedAudios.size() > 0) {
                    return relatedAudios;
                }

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
    public List<Video> getStoryRelatedVideos(int storylineID, Context context) {
        List<Video> relatedVideos = new ArrayList<>();
        for (StoryPoint storyPoint : this.storyPoints) {
            if (storyPoint.getStorylineID() == storylineID) {
                for (Video video : storyPoint.getVideos()) {
                    if (context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(video.getLanguage().name())) {
                        relatedVideos.add(video);
                    }
                }
                if (relatedVideos.size() > 0) {
                    return relatedVideos;
                }
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
    public List<Image> getStoryRelatedImages(int storylineID, Context context) {
        List<Image> relatedImages = new ArrayList<>();
        for (StoryPoint storyPoint : this.storyPoints) {
            if (storyPoint.getStorylineID() == storylineID) {
                for (Image image : storyPoint.getImages()) {
                    if (context.getResources().getConfiguration().locale.getLanguage().equalsIgnoreCase(image.getLanguage().name())) {
                        relatedImages.add(image);
                    }
                }
                if (relatedImages.size() > 0) {
                    return relatedImages;
                }
            }
        }

        return getLocaleImages(context);
    }



}
