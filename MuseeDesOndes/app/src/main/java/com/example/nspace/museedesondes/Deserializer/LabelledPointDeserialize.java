package com.example.nspace.museedesondes.deserializer;

import com.example.nspace.museedesondes.model.LabelledPoint;
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
 * Created by michal on 2/18/2016.
 */
public class LabelledPointDeserialize extends JsonDeserializer<ArrayList<LabelledPoint>> {
    @Override
    public ArrayList<LabelledPoint> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ArrayList<LabelledPoint> labelledPoints = new ArrayList<>();
        ObjectCodec objectCodec = p.getCodec();

        JsonNode node = objectCodec.readTree(p);
        ObjectMapper mapper = new ObjectMapper();

        for(JsonNode n : node)
        {
            labelledPoints.add(mapper.treeToValue(n,LabelledPoint.class));
        }

        return labelledPoints;
    }
}
