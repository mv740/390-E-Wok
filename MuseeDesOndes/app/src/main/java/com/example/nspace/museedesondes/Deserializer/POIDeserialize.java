package com.example.nspace.museedesondes.Deserializer;

import com.example.nspace.museedesondes.Model.PointOfInterest;
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
 * Created by micha on 2/18/2016.
 */
public class POIDeserialize extends JsonDeserializer<ArrayList<PointOfInterest>> {
    @Override
    public ArrayList<PointOfInterest> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ArrayList<PointOfInterest> pointOfInterests = new ArrayList<>();
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
