package com.example.nspace.museedesondes.utility;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import com.example.nspace.museedesondes.MainActivity;
import com.example.nspace.museedesondes.model.Edge;
import com.example.nspace.museedesondes.model.Map;
import com.example.nspace.museedesondes.model.PointOfInterest;
import junit.framework.Assert;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 3/20/2016.
 */
public class NavigationTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Context context;

    public NavigationTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getActivity();
    }

    @Test
    public void testFindShortestPath() throws Exception {

        Map information = Map.getInstance(context);
        information.getEdges().clear();
        information.getNodes().clear();

        information.getNodes().add(new PointOfInterest(1, 0, 0, 0, null, null, null, null));
        information.getNodes().add(new PointOfInterest(2, 0, 0, 0, null, null, null, null));
        information.getNodes().add(new PointOfInterest(3, 0, 0, 0, null, null, null, null));
        information.getNodes().add(new PointOfInterest(4, 0, 0, 0, null, null, null, null));

        information.getEdges().add(new Edge(1, 2, 5, 0));
        information.getEdges().add(new Edge(2, 4, 5, 0));
        information.getEdges().add(new Edge(1, 3, 5, 0));
        information.getEdges().add(new Edge(3, 4, 10, 0));

        Navigation navigation = new Navigation(information);

        //mockup graph
        List list = new ArrayList();
        SimpleWeightedGraph<Integer,DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        DefaultWeightedEdge weightedEdge = graph.addEdge(1, 2);
        DefaultWeightedEdge weightedEdge1 = graph.addEdge(2, 4);
        list.add(weightedEdge);
        list.add(weightedEdge1);

        Assert.assertEquals(list.toString(),navigation.findShortestPath(1,4).toString());

    }

    public void testPathNotFound() throws Exception {


        Map information = Map.getInstance(context);
        information.getEdges().clear();
        information.getNodes().clear();


        information.getNodes().add(new PointOfInterest(1, 0, 0, 0, null, null, null, null));
        information.getNodes().add(new PointOfInterest(2, 0, 0, 0, null, null, null, null));
        information.getNodes().add(new PointOfInterest(3, 0, 0, 0, null, null, null, null));
        information.getNodes().add(new PointOfInterest(4, 0, 0, 0, null, null, null, null));
        information.getNodes().add(new PointOfInterest(5, 0, 0, 0, null, null, null, null));

        information.getEdges().add(new Edge(1, 2, 5, 0));
        information.getEdges().add(new Edge(2, 4, 5, 0));
        information.getEdges().add(new Edge(1, 3, 5, 0));
        information.getEdges().add(new Edge(3, 4, 10, 0));

        Navigation navigation = new Navigation(information);

        assertEquals(null, navigation.findShortestPath(1, 5));
    }
}