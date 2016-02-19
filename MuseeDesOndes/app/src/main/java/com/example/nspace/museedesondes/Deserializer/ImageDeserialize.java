package com.example.nspace.museedesondes.Deserializer;

import com.example.nspace.museedesondes.Model.Image;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by michal on 2/19/2016.
 */
public class ImageDeserialize extends JsonDeserializer<ArrayList<Image>> {
    @Override
    public ArrayList<Image> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ArrayList<Image> images = new ArrayList<>();
        ObjectCodec objectCodec = p.getCodec();

        JsonNode node = objectCodec.readTree(p);
        ObjectMapper mapper = new ObjectMapper();

        for(JsonNode n : node)
        {
            images.add(mapper.treeToValue(n,Image.class));
        }

        return images;
    }
}