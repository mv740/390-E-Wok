package com.example.nspace.museedesondes.Deserializer;

import com.example.nspace.museedesondes.Model.Audio;
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
public class AudioDeserialize extends JsonDeserializer<ArrayList<Audio>> {
    @Override
    public ArrayList<Audio> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        ArrayList<Audio> audios = new ArrayList<>();
        ObjectCodec objectCodec = p.getCodec();

        JsonNode node = objectCodec.readTree(p);
        ObjectMapper mapper = new ObjectMapper();

        for(JsonNode n : node)
        {
            audios.add(mapper.treeToValue(n,Audio.class));
        }

        return audios;
    }
}