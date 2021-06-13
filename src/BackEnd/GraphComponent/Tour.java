package BackEnd.GraphComponent;


import Classes.CellNodes;
import Classes.CircleNode;
import Classes.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tour {

    private double totalDistance;
    private List<List<MapVertex>> route;

    public Tour() {
        this.totalDistance = 0;
        route = new ArrayList<>();
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public List<List<MapVertex>> getRoute() {
        return route;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();


        for (int i = 0; i < route.size(); i++) {
            sb.append("\n------------------------\nVehicle ").append(i + 1).append("\n");
            sb.append(properPrintRoute(route.get(i))).append("\n");
            sb.append("Cost: ").append(distanceRoute(route.get(i)));
        }

        return sb.toString();
    }

    double distanceRoute(List<MapVertex> vertexList) {
        double distance = 0;
        for (int i = 0; i < vertexList.size() - 1; i++) {
            MapVertex current = vertexList.get(i);
            MapVertex next = vertexList.get(i + 1);
            double dx = current.coordinateX - next.coordinateX;
            double dy = current.coordinateY - next.coordinateY;
            distance += Math.sqrt(dx * dx + dy * dy);
        }
        return distance;
    }

    String properPrintRoute(List<MapVertex> vertexList){
        StringBuilder sb = new StringBuilder();

        for(int i=0;i<vertexList.size();i++){
            sb.append("{ ID ").append(vertexList.get(i).ID).append(" }");
            if(i != vertexList.size()-1){
                sb.append(" --> ");
            }
        }
        return sb.toString();
    }
}
