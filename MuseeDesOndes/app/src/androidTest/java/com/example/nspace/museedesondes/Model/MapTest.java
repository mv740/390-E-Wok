package com.example.nspace.museedesondes.Model;


import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import com.example.nspace.museedesondes.MainActivity;


/**
 * Created by michal on 1/30/2016.
 */
public class MapTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Context context;

    public MapTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getActivity();
    }


    public void testGetNodes() throws Exception {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero nodes", map.getNodes().isEmpty());
    }

    public void testGetPointOfInterests() throws Exception {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero nodes", map.getNodes().isEmpty());
        if(!Map.getInstance(context).getPointOfInterests().isEmpty())
        {
            assertEquals(PointOfInterest.class,map.getPointOfInterests().get(0).getClass());
        }
    }


    public void testSearchNodeById() throws Exception {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero nodes", map.getNodes().isEmpty());
        assertEquals(1, map.searchNodeById(1).getId());
    }

    public void testGetEdges() throws Exception {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero edges", map.getEdges().isEmpty());
    }

    public void testGetStoryLines() throws Exception {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero story lines", map.getEdges().isEmpty());
    }

//    public void testTEST() throws Exception {
//        Map map = Map.getInstance(context);
//        assertNotNull("error map is null", map);
//        assertEquals("test",map.getStoryLines().get(0).getDescriptions().get(0).getDescription());
//        assertEquals("hello",map.getStoryLines().get(0).getDescriptions().get(1).getDescription());
//        assertEquals("titre",map.getStoryLines().get(0).getDescriptions().get(1).getTitle());
//    }
}