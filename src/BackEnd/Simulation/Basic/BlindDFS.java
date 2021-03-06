package BackEnd.Simulation.Basic;





import BackEnd.GraphComponent.*;
import BackEnd.map.MapGraph;
import Classes.CellNodes;
import Classes.CircleNode;
import Classes.Edge;
import Classes.Model;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class BlindDFS {

    private static MapGraph G;
    private static int N, C, lorries;
    private static double tourCost;
    static Map<Integer, CellNodes> cellNodesMap = Model.cellMap;
    static Map<String, Edge> edgesMap = Model.edgesMap;
    public static String[] run(MapGraph G, int N, int C, int numberOfLorries) {
        BlindDFS.lorries = numberOfLorries;
        BlindDFS.G = G;
        BlindDFS.N = N;
        BlindDFS.C = C;

        System.out.println("---Blind DFS Search---\n");
        long start = System.nanoTime();
        tourCost = 0; // to ensure that repetitive run wil not effect result
        setAllEdgeFalse();
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

        int vehicleCount = 0, lorryCount = 0;
        while (visitedID.size() != N - 1) {
            Random r = new Random();
            int red = r.nextInt(256);
            int green = r.nextInt(256);
            int blue = r.nextInt(256);
            //while all vertices haven't been visited
            int tempC;
            boolean lorryUsed = false;
            outString.append("---------------------\n");
            if (lorries != 0) {
                //if there are still lorries to be dispatched out:
                tempC = 2 * C; //to deduct the capacity in lorry whenever a vertex is visited
                outString.append("Vehicle ").append(++vehicleCount).append(" (Lorry ").append(++lorryCount).append(")\n");
                lorries--;
                lorryUsed = true;
            } else {
                tempC = C;
                outString.append("Vehicle ").append(++vehicleCount).append("\n");
            }
            double dT = 0; //the total distance travelled

            MapVertex currentVertex = G.getHead();
            MapVertex nextVertex = G.getHead();

            outString.append(currentVertex);
            int totalCap = 0;
            double tempD = 0;
            boolean dispatched = false;
            for (int i = 0; i < N; i++) {
                //go through every vertices in the graph
                for (int j = 0; j < currentVertex.EdgeList.size(); j++) {
                    //go through every edges connected to current vertex
                    MapEdge currentEdge = currentVertex.EdgeList.get(j); //starting from the first edge

                    if (lorryUsed && currentEdge.destination.narrowArea)
                        //if lorry is currently used and the destination is in the narrow area, don't go here
                        continue;

                    if (tempC >= currentEdge.destination.capacity && !visitedID.contains(currentEdge.destination.ID)) {
                        /* IF (capacity >= demand) AND (the destination hasn't been visited yet):
                                choose this path.
                        */
                        dispatched = true;
                        nextVertex = currentEdge.destination; // path to go
                        tempD = currentEdge.dist;  //update the distance travelled
                        //break;
                    }
                }

                if (!dispatched && nextVertex.ID == 0) {
                    outString.append(" --X-> NOT DISPATCHED");
                    break;
                }

                visitedID.add(nextVertex.ID); //the nextVertex has been visited.
                outString.append(" --> ").append(nextVertex);
                CircleNode currentCircle = (CircleNode) cellNodesMap.get(nextVertex.ID);
                currentCircle.setColour(red,green,blue);
                edgesMap.get(currentVertex.ID+" "+nextVertex.ID).setVisible(true);
                //update the values
                dT += tempD; //new total distance travelled
                tempC -= nextVertex.capacity; //deduct capacity
                totalCap += nextVertex.capacity;
                currentVertex = nextVertex;

                if (nextVertex.ID == 0)
                    break;
            }
            visitedID.remove((Integer) 0); //used Integer to make it as an object
            outString.append("\nCapacity: ").append(totalCap);
            outString.append("\nCost: ").append(dT).append("\n");
            tourCost += dT;
        }
        return outString.toString();
    }

    private static void setAllEdgeFalse(){
        for (Map.Entry<String, Edge> entry : edgesMap.entrySet()) {
            Edge currentEdge = entry.getValue();
            currentEdge.setVisible(false);
        }


    }
}
