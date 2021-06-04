package BackEnd.Simulation.Basic;





import BackEnd.GraphComponent.*;
import BackEnd.map.MapGraph;
import Classes.CellNodes;
import Classes.CircleNode;
import Classes.Model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class BlindDFS {

    private static MapGraph G;
    private static int N, C;
    private static double tourCost;
    static Map<Integer, CellNodes> cellNodesMap = Model.cellMap;
    public static String[] run(MapGraph G, int N, int C) {
        BlindDFS.G = G;
        BlindDFS.N = N;
        BlindDFS.C = C;

        System.out.println("---Blind DFS Search---\n");
        long start = System.nanoTime();
        String result = search();
        long end = System.nanoTime();

        System.out.println("Tour Cost: " + tourCost);
        System.out.println(result);
        System.out.println("Execution time: " + (double) (end - start) * Math.pow(10, -6) + "ms\n");

        return new String[]{"Tour Cost: "+tourCost,result,"Execution time: " + (double) (end - start) * Math.pow(10, -6) + "ms\n"};
    }

    private static String search() {
        ArrayList<Integer> visitedID = new ArrayList<>(); //a list of visited vertices (based on ID) except depot
        StringBuilder outString = new StringBuilder();

        int vehicleCount = 0;
        while (visitedID.size() != N - 1) {
            Random r = new Random();
            int red = r.nextInt(256);
            int green = r.nextInt(256);
            int blue = r.nextInt(256);
            //while all vertices haven't been visited
            outString.append("Vehicle ").append(++vehicleCount).append("\n"); //EACH LOOP REPRESENTS ONE DELIVERY VEHICLE

            double dT = 0; //the total distance travelled

            MapVertex currentVertex = G.getHead();
            MapVertex nextVertex = G.getHead();
            int tempC = C; //to deduct the capacity in vehicle whenever a vertex is visited

            outString.append(currentVertex);
            double tempD = 0;
            for (int i = 0; i < N; i++) {
                //go through every vertices in the graph
                for (int j = 0; j < currentVertex.EdgeList.size(); j++) {
                    //go through every edges connected to current vertex
                    MapEdge currentEdge = currentVertex.EdgeList.get(j); //starting from the first edge

                    if (tempC >= currentEdge.destination.capacity && !visitedID.contains(currentEdge.destination.ID)) {
                        /* IF (capacity >= demand) AND (the destination hasn't been visited yet):
                                choose this path.
                        */
                        nextVertex = currentEdge.destination; // path to go
                        tempD = currentEdge.dist;  //update the distance travelled
                        //break;
                    }
                }
                visitedID.add(nextVertex.ID); //the nextVertex has been visited.
                outString.append(" --> ").append(nextVertex);
                CircleNode currentCircle = (CircleNode) cellNodesMap.get(nextVertex.ID);
                currentCircle.setColour(red,green,blue);
                //update the values
                dT += tempD; //new total distance travelled
                tempC -= nextVertex.capacity; //deduct capacity
                currentVertex = nextVertex;

                if (currentVertex.ID == 0)
                    //if the vehicle returns to the depot, break the loop/go to the next vehicle
                    break;
            }
            visitedID.remove((Integer) 0); //used Integer to make it as an object
            outString.append("\nCapacity: ").append(C - tempC);
            outString.append("\nCost: ").append(dT).append("\n");
            tourCost += dT;
        }
        return outString.toString();
    }
}