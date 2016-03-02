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


    public void testGetNodes() {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);

        Node testNode = new Node(1234, 1234, 1234, 1234);
        map.getNodes().add(testNode);
        int index = map.getNodes().size() - 1;
        assertEquals(testNode.equals(map.getNodes().get(index)), true);

    }

    public void testGetPointOfInterests() {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);

        PointOfInterest test = new PointOfInterest(1234, 1234, 1234, 1234, null, null, null, null);
        map.getPointOfInterests().add(test);

        int index = map.getPointOfInterests().size() - 1;
        assertEquals(test.equals(map.getPointOfInterests().get(index)), true);

    }

    public void testGetEdges() {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);

        Edge edge = new Edge(1234, 4567, 1234, 1);
        map.getEdges().add(edge);
        int index = map.getEdges().size() - 1;

        assertEquals(edge.equals(map.getEdges().get(index)), true);

    }

    public void testGetStoryLines() {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);

        StoryLine storyLineTest = new StoryLine(1234, "test", null, 1234, 1234, null);
        map.getStoryLines().add(storyLineTest);
        int index = map.getStoryLines().size() - 1;

        assertEquals(storyLineTest.equals(map.getStoryLines().get(index)), true);
    }

    public void testGetLabelledPoints() {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);

        LabelledPoint labelledPoint = new LabelledPoint(1234, 1234, 1234, 1234, Label.ENTRANCE);
        map.getLabelledPoints().add(labelledPoint);
        int index = map.getLabelledPoints().size() - 1;

        assertEquals(labelledPoint.equals(map.getLabelledPoints().get(index)), true);
    }

    public void testGetFloorPlans() {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);

        FloorPlan floorPlan = new FloorPlan("1234", "hello world", 1234, 1234);
        map.getFloorPlans().add(floorPlan);
        int index = map.getFloorPlans().size() - 1;

        assertEquals(floorPlan.equals(map.getFloorPlans().get(index)), true);
    }


    //test methods
    public void testSearchNodeById() {
        Map map = Map.getInstance(context);
        Node node = new Node(100, 1, 1.0, 1.0);
        map.getNodes().add(node);
        assertNotNull("error map is null", map);

        assertEquals(100, map.searchNodeById(100).getId());
        assertEquals(node.equals(map.searchNodeById(100)), true);
        assertNull(map.searchNodeById(-1));
    }

    public void testSearchPoiById() {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);
        PointOfInterest pointOfInterest = new PointOfInterest(10, 10, 10.0, 10.0, null, null, null, null);
        map.getPointOfInterests().add(pointOfInterest);

        assertEquals(10, map.searchPoiById(10).getId());
        assertEquals(pointOfInterest.equals(map.searchPoiById(10)), true);
        assertNull(map.searchPoiById(-1));
    }

    public void testGetPointOfInterestsCurrentFloor() {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);

        PointOfInterest pointOfInterest = new PointOfInterest(1234, 1111, 1234.0, 1234.0, null, null, null, null);
        map.getPointOfInterests().add(pointOfInterest);

        assertEquals(map.getPointOfInterestsCurrentFloor(1111).size(), 1);
        assertEquals(map.getPointOfInterestsCurrentFloor(1111).get(0).getId(), 1234);

    }

    public void testGetLabelledPointsCurrentFloor() {
        Map map = Map.getInstance(context);
        assertNotNull("error map is null", map);

        LabelledPoint labelledPointTest = new LabelledPoint(1234, 3333, 1234, 1234, Label.ENTRANCE);
        map.getLabelledPoints().add(labelledPointTest);

        assertEquals(map.getLabelledPointsCurrentFloor(3333).size(), 1);
        assertEquals(map.getLabelledPointsCurrentFloor(3333).get(0).getFloorID(), 3333);

    }


}