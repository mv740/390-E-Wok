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

    //test parsings

    public void testGetNodes()  {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero nodes", map.getNodes().isEmpty());
    }

    public void testGetPointOfInterests()  {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero nodes", map.getNodes().isEmpty());
        if(!Map.getInstance(context).getPointOfInterests().isEmpty())
        {
            assertEquals(PointOfInterest.class,map.getPointOfInterests().get(0).getClass());
        }
    }

    public void testGetEdges()  {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero edges", map.getEdges().isEmpty());
    }

    public void testGetStoryLines() {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero story lines", map.getEdges().isEmpty());
    }

    //test methods
    public void testSearchNodeById() {
        Map map = Map.getInstance(context);
        Node node = new Node(100,1,1.0,1.0);
        map.getNodes().add(node);
        assertNotNull("error map is null", map);
        assertFalse("error : map is containing zero nodes", map.getNodes().isEmpty());
        assertEquals(100, map.searchNodeById(100).getId());
        assertEquals(node.equals(map.searchNodeById(100)), true);
        assertNull(map.searchNodeById(-1));
    }

    public void testSearchPoiById(){
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        PointOfInterest pointOfInterest = new PointOfInterest(10,10,10.0,10.0,null,null,null,null);
        map.getPointOfInterests().add(pointOfInterest);
        assertFalse("error : map is containing zero pointOfInterest", map.getPointOfInterests().isEmpty());
        assertEquals(10, map.searchPoiById(10).getId());
        assertEquals(pointOfInterest.equals(map.searchPoiById(10)), true);
        assertNull(map.searchPoiById(-1));
    }


}