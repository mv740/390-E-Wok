package com.example.nspace.museedesondes.Model;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.example.nspace.museedesondes.MainActivity;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

/**
 * Created by michal on 1/30/2016.
 */
public class MapTest extends ActivityInstrumentationTestCase2<MainActivity> {

    MainActivity activity;

    public MapTest() {
        super(MainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }


    public void testGetNodes() throws Exception {
        Map map = Map.getInstance(activity);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero nodes", map.getNodes().isEmpty());
    }

    public void testGetPointOfInterests() throws Exception {
        Map map = Map.getInstance(activity);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero nodes", map.getNodes().isEmpty());
        if(!Map.getInstance(activity).getPointOfInterest().isEmpty())
        {
            assertEquals(PointOfInterest.class,map.getPointOfInterest().get(0).getClass());
        }
    }

    public void testSearchNodeById() throws Exception {
        Map map = Map.getInstance(activity);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero nodes", map.getNodes().isEmpty());
        assertEquals(1,map.searchNodeById(1).getId());
    }

    public void testGetEdges() throws Exception {
        Map map = Map.getInstance(activity);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero edges", map.getEdges().isEmpty());
    }

    public void testGetStorylines() throws Exception {
        Map map = Map.getInstance(activity);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero story lines", map.getEdges().isEmpty());

    }

    //todo : see how the whole activity workspace is done to be able to correctly unit test the loading file
    private String loadJSON(String filename) {
        String json = null;
        try {

            InputStream is = getActivity().getAssets().open((filename));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();

        }
        return json;
    }
}