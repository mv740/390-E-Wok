package com.example.nspace.museedesondes.utility;

import android.graphics.Color;
import android.util.Log;
import com.example.nspace.museedesondes.model.Edge;
import com.example.nspace.museedesondes.model.Map;
import com.example.nspace.museedesondes.model.Node;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michal on 3/20/2016.
 */
public class Navigation {

    public static final String NAVIGATIONLOG = "Navigation";
    private SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph;
    private Map map;
    private int userLocation;
    private Marker selectedStartMarker;
    private String panelTitle;
    private PoiPanelManager currentPanelManager;

    public Navigation(Map map) {
        this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        this.map = map;
        initializeGraph();
    }


    private void initializeGraph() {

        List<Edge> edgeList = map.getEdges();
        List<Node> nodeList = map.getNodes();

        for (Node node : nodeList) {
            graph.addVertex(node.getId());
        }
        for (Edge edge : edgeList) {
            DefaultWeightedEdge weightedEdge = graph.addEdge(edge.getStartID(), edge.getEndID());
            graph.setEdgeWeight(weightedEdge, edge.getDistance());
        }
    }

    /**
     * Find shortest path from vertex start to vertex end, if none found return null
     *
     * @param start vertex
     * @param end   vertex
     * @return path
     */
    public List<DefaultWeightedEdge> findShortestPath(int start, int end) {

        List<DefaultWeightedEdge> shortestPath = DijkstraShortestPath.findPathBetween(graph, start, end);
        if (shortestPath != null) {
            Log.v(NAVIGATIONLOG, shortestPath.toString());
        } else {
            Log.v(NAVIGATIONLOG, "no path found");
        }

        return shortestPath;
    }


    public Integer getEdgeSource(DefaultWeightedEdge edge) {
        return graph.getEdgeSource(edge);
    }

    public Integer getEdgeTarget(DefaultWeightedEdge edge) {
        return graph.getEdgeTarget(edge);
    }

    public Integer getEdgeWeight(DefaultWeightedEdge edge) {
        Double d = graph.getEdgeWeight(edge);
        return d.intValue();
    }

    /**
     * From the list of edge from the shortest path, get the corresponding edges from the map edge list
     *
     * @param edgeList
     * @return
     */
    public List<Edge> getCorrespondingEdgesFromPathSequence(List<DefaultWeightedEdge> edgeList) {

        List<Edge> list = new ArrayList<>();
        for (DefaultWeightedEdge defaultWeightedEdge : edgeList) {
            for (Edge currentEdge : map.getEdges()) {
                if (currentEdge.getStartID() == getEdgeSource(defaultWeightedEdge) && currentEdge.getEndID() == getEdgeTarget(defaultWeightedEdge)) {
                    list.add(currentEdge);
                }
            }
        }

        return list;
    }

    public int getPathDistance(List<DefaultWeightedEdge> weightedEdgeList)
    {
        int distance = 0;
        for(DefaultWeightedEdge edge : weightedEdgeList)
        {
            distance += getEdgeWeight(edge);
        }
        return distance;
    }

    public boolean doesPathExist(List<DefaultWeightedEdge> list)
    {
        return list != null;
    }

    public void setUserLocation(int userLocation) {
        this.userLocation = userLocation;
    }

    public int getUserLocation() {
        return userLocation;
    }

    public void selectedStart(Marker marker) {
        if (selectedStartMarker != null) {
            selectedStartMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        selectedStartMarker = marker;
    }

    public void clear()
    {
        selectedStartMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
    }

    public void startNavigationMode(PoiPanelManager panelManager)
    {
        this.currentPanelManager = panelManager;
        currentPanelManager.close();
        //poiPanelLayout.back
        currentPanelManager.getPoiPanelLayout().setBackgroundColor(Color.DKGRAY);
        panelTitle = currentPanelManager.getTitle();
        currentPanelManager.replaceTitle("NAVIGATION");
        currentPanelManager.getPanel().setTouchEnabled(false);
    }

    public void stopNavigationMode()
    {
        currentPanelManager.getPoiPanelLayout().setBackgroundColor(Color.parseColor("#FFE33C3C"));
        currentPanelManager.replaceTitle(panelTitle);
        currentPanelManager.getPanel().setTouchEnabled(true);
    }

}


