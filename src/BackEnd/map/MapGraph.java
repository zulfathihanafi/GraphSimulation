package BackEnd.map;

import BackEnd.GraphComponent.*;

import java.util.ArrayList;

public class MapGraph {
    public ArrayList<MapVertex> vertexArrayList; //holds vertices arraylist

    public MapGraph() {
        vertexArrayList = new ArrayList<>();
    }

    public int size() {
        return vertexArrayList.size();
    }

    public void addVertex(MapVertex vertex) {
        vertexArrayList.add(vertex);
    }

    public MapVertex getHead() {
        return vertexArrayList.get(0);
    }

    public MapVertex getVertex(int idx) {
        if(idx < 0 || idx >= size())
            return null;

        return vertexArrayList.get(idx);
    }

    public MapVertex getLast() {
        return vertexArrayList.get(vertexArrayList.size() - 1);
    }

    public void unvisitAll(){
        for(MapVertex V: vertexArrayList){
            V.unvisit();
        }
    }

    public boolean isAllVisited() {
        boolean out = false;
        for(MapVertex V: vertexArrayList)
            out = V.isVisited();

        return out;
    }

    public void completeConnect() {
        //to make a complete graph
        for (int i = 0; i < vertexArrayList.size(); i++) {
            MapVertex sourceVertex = vertexArrayList.get(i);
            for (MapVertex destination : vertexArrayList) {
                double dist = computeDistance(sourceVertex, destination);

                if (sourceVertex.ID != destination.ID)
                /*(sourceVertex.ID == (destination.ID)) {
                    //maybe for future use?
                } else */ {
                    //avoid duplication
                    MapEdge newEdge = new MapEdge(destination, dist);

                    sourceVertex.EdgeList.add(newEdge);

                    // destination.EdgeList.add(new Edge(sourceVertex,dist));
                }
            }
        }
    }

    public void printConnections() {
        System.out.println(vertexArrayList.get(0));
        for (MapVertex curr : vertexArrayList) {
            System.out.println("Vertex ID" + curr.ID + " " + curr.EdgeList.toString());
        }
    }

    public static double computeDistance(MapVertex v1, MapVertex v2) {
        double dx = v1.coordinateX - v2.coordinateX;
        double dy = v1.coordinateY - v2.coordinateY;

        return Math.sqrt((dx * dx) + (dy * dy));
    }

    public void clear(){
        for(int i=0;i< vertexArrayList.size();i++){
            MapVertex removing = vertexArrayList.get(i);
            removing = null;
        }
        vertexArrayList.clear();

    }
}
