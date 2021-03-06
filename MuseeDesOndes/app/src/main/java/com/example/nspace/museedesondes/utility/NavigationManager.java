package com.example.nspace.museedesondes.utility;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.nspace.museedesondes.R;
import com.example.nspace.museedesondes.model.Edge;
import com.example.nspace.museedesondes.model.Label;
import com.example.nspace.museedesondes.model.LabelledPoint;
import com.example.nspace.museedesondes.model.MuseumMap;
import com.example.nspace.museedesondes.model.Node;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by michal on 3/20/2016.
 */
public class NavigationManager {

    public static final String NAVIGATIONLOG = "NavigationManager";
    private SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph;
    private MuseumMap map;
    private Marker selectedStartMarker;
    private String panelTitle;
    private PoiPanelManager currentPanelManager;
    private boolean endTour;

    public NavigationManager(MuseumMap map) {
        this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        this.map = map;
        this.endTour = false;
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

    public int getPathDistance(List<DefaultWeightedEdge> weightedEdgeList) {
        int distance = 0;
        for (DefaultWeightedEdge edge : weightedEdgeList) {
            distance += getEdgeWeight(edge);
        }
        return distance;
    }

    public boolean doesPathExist(List<DefaultWeightedEdge> list) {
        return list != null;
    }


    public List<DefaultWeightedEdge> getShortestExitPath(int startNodeID) {
        List<DefaultWeightedEdge> currentWeightedEdgeList;
        List<DefaultWeightedEdge> shortestWeightedEdgeList = null;
        int minDistance = Integer.MAX_VALUE;
        int currentDistance;

        //compute shortest path to each exit and find one with min distance
        for (LabelledPoint labelledPoint : map.getLabelledPoints()) {
            if (labelledPoint.getLabel() == Label.EXIT) {
                currentWeightedEdgeList = findShortestPath(startNodeID, labelledPoint.getId());
                if (currentWeightedEdgeList != null) {
                    currentDistance = getPathDistance(currentWeightedEdgeList);
                    if (currentDistance < minDistance) {
                        shortestWeightedEdgeList = currentWeightedEdgeList;
                        minDistance = currentDistance;
                    }
                }
            }
        }
        return shortestWeightedEdgeList;
    }

    public void selectedStart(Marker marker) {
        if (selectedStartMarker != null) {
            selectedStartMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.walk));
        selectedStartMarker = marker;
    }

    public void clear() {
        if (selectedStartMarker != null) {
            selectedStartMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        }
    }

    public void startNavigationMode(PoiPanelManager panelManager) {
        this.currentPanelManager = panelManager;
        currentPanelManager.close();
        //poiPanelLayout.back
        currentPanelManager.getPoiPanelLayout().setBackgroundColor(Color.DKGRAY);
        panelTitle = currentPanelManager.getTitle();
        currentPanelManager.replaceTitle("NAVIGATION");
        currentPanelManager.getPanel().setTouchEnabled(false);

        //todo refactoring to use R.String for message 
        MaterialShowcaseView.Builder tutorial =  new MaterialShowcaseView.Builder(panelManager.getActivity())
                .setTarget(panelManager.getActivity().findViewById(R.id.map))
                .setTitleText("Navigation Helper")
                .withRectangleShape(true)
                .setShapePadding(-370)
                .setDismissOnTouch(true)
                .setMaskColour(Color.parseColor("#E6444444"))
                .setContentText("Please select a marker as your starting location");

        if (currentPanelManager.getActivity().isSearchingExit()) {
            currentPanelManager.getNavigationButton().setVisibility(View.VISIBLE);
            tutorial.setTitleText("Find Exit");
        }

        if(!endTour)
        {
            tutorial.show();
            endTour = false;
        }

        currentPanelManager.getNavigationButton().setColorNormal(ContextCompat.getColor(currentPanelManager.getActivity(), R.color.rca_primary));
        currentPanelManager.getNavigationButton().setImageResource(R.drawable.ic_clear_white_48dp);
    }

    public void stopNavigationMode() {
        currentPanelManager.getPoiPanelLayout().setBackgroundColor(Color.parseColor("#FFE33C3C"));
        currentPanelManager.replaceTitle(panelTitle);
        currentPanelManager.getPanel().setTouchEnabled(true);
        currentPanelManager.getNavigationButton().setImageResource(R.drawable.location_icon);
        currentPanelManager.getNavigationButton().setColorNormal(Color.WHITE);

        if (currentPanelManager.getActivity().isSearchingExit()) {
            currentPanelManager.getNavigationButton().setVisibility(View.INVISIBLE);
            if(currentPanelManager.getActivity().isFreeExploration())
            {
                currentPanelManager.getNavigationButton().setVisibility(View.VISIBLE);
            }
        }
    }


    public void setEndTour() {
        endTour = true;
    }

    public boolean isEndTour() {
        return endTour;
    }
}


