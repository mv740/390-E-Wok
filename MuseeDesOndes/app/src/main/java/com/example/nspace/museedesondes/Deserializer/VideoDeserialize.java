package com.example.nspace.museedesondes.deserializer;

import com.example.nspace.museedesondes.model.Video;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 2/19/2016.
 */
public class VideoDeserialize extends JsonDeserializer<List<Video>> {
    @Override
    public List<Video> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        List<Video> videos = new ArrayList<>();
        ObjectCodec objectCodec = p.getCodec();

        JsonNode node = objectCodec.readTree(p);
        ObjectMapper mapper = new ObjectMapper();

        for(JsonNode n : node)
        {
            videos.add(mapper.treeToValue(n,Video.class));
        }

        return videos;
    }
}