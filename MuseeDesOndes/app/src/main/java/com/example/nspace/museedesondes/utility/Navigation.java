package com.example.nspace.museedesondes.utility;

import android.util.Log;

import com.example.nspace.museedesondes.model.Edge;
import com.example.nspace.museedesondes.model.Map;
import com.example.nspace.museedesondes.model.Node;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;

/**
 * Created by michal on 3/20/2016.
 */
public class Navigation {

    public static final String NAVIGATIONLOG = "Navigation";
    private SimpleWeightedGraph<Integer,DefaultWeightedEdge> graph;
    private Map map;

    public Navigation(Map map) {
        this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        this.map = map;
       initializeGraph();
    }


    private void initializeGraph() {

        List<Edge> edgeList = map.getEdges();
        List<Node> nodeList = map.getNodes();

        for(Node node : nodeList)
        {
            graph.addVertex(node.getId());
        }
        for(Edge edge :edgeList)
        {
            DefaultWeightedEdge weightedEdge = graph.addEdge(edge.getStartID(), edge.getEndID());
            graph.setEdgeWeight(weightedEdge, edge.getDistance());
        }
    }

    /**
     * Find shortest path from vertex start to vertex end, if none found return null
     *
     * @param start vertex
     * @param end vertex
     * @return path
     */
    public List findShortestPath(int start, int end)
    {

        List shortestPath =   DijkstraShortestPath.findPathBetween(graph, start, end);
        if(shortestPath != null)
        {
            Log.v(NAVIGATIONLOG, shortestPath.toString());
        }else
        {
            Log.v(NAVIGATIONLOG, "no path found");
        }
        return shortestPath;
    }
}
