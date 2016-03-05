package com.example.nspace.museedesondes.deserializer;

import com.example.nspace.museedesondes.model.Text;
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
public class TextDeserialize extends JsonDeserializer<ArrayList<Text>> {
    @Override
    public ArrayList<Text> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ArrayList<Text> texts = new ArrayList<>();
        ObjectCodec objectCodec = p.getCodec();

        JsonNode node = objectCodec.readTree(p);
        ObjectMapper mapper = new ObjectMapper();

        for(JsonNode n : node)
        {
            texts.add(mapper.treeToValue(n,Text.class));
        }

        return texts;
    }
}