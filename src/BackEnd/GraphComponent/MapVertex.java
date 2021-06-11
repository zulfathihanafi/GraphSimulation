package BackEnd.GraphComponent;

import java.util.ArrayList;

public class MapVertex {
    public double coordinateX, coordinateY;
    public int capacity, ID;
    public boolean visited = false, narrowArea = false;
    public ArrayList<MapEdge> EdgeList;

    public MapVertex() {
    }

    public MapVertex(double coordinateX, double coordinateY, int capacity, int ID) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.ID = ID;
        this.capacity = capacity;
        EdgeList = new ArrayList<>();
    }

    public MapVertex visit() {
        visited = true;
        return this;
    }

    public MapVertex unvisit() {
        visited = false;
        return this;
    }

    public boolean isVisited() {
        return visited;
    }
    public void setNarrowArea(boolean narrowArea) {
        this.narrowArea = narrowArea;
    }

    public boolean isNarrowArea() {
        return narrowArea;
    }

    @Override
    public String toString() {
        return "{ID " + ID + "} [NARROW: " + narrowArea + "] ";
    }


}
