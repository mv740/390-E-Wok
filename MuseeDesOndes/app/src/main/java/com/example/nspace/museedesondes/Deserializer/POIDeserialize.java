package com.example.nspace.museedesondes.deserializer;

import com.example.nspace.museedesondes.model.PointOfInterest;
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
 * Created by micha on 2/18/2016.
 */
public class POIDeserialize extends JsonDeserializer<List<PointOfInterest>> {
    @Override
    public List<PointOfInterest> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        List<PointOfInterest> pointOfInterests = new ArrayList<>();
        ObjectCodec objectCodec = p.getCodec();

        JsonNode node = objectCodec.readTree(p);
        ObjectMapper mapper = new ObjectMapper();

        for(JsonNode n : node)
        {
            pointOfInterests.add(mapper.treeToValue(n,PointOfInterest.class));
        }

        return pointOfInterests;
    }
}
