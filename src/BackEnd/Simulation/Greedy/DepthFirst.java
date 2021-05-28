package BackEnd.Simulation.Greedy;

import BackEnd.GraphComponent.*;
import BackEnd.map.*;

import java.util.List;

public class DepthFirst {
    //the id is array
    private static MapGraph G;
    private static int C;
    private static double tourCost;
    private static List<List<Integer>> tree;

    public static void run(MapGraph G, int C) {
        tourCost = 0;
        DepthFirst.G = G;
        DepthFirst.C = C;
        System.out.println("---DF Search---\n");
        long start = System.nanoTime();
        //String result = search();
        long end = System.nanoTime();
        System.out.println("Tour Cost: " + tourCost);
        //System.out.println(result);
        System.out.println("Execution time: " + (double) (end - start) * Math.pow(10, -6) + "ms\n");

       // tree = Tree.getTree();


    }

    private static void search() {

        for (int i = 0; i < tree.size(); i++) {//tree contains list of list, we get the list here
            MapVertex currentVertex = G.getHead();
            MapVertex nextVertex = G.getHead();
            for (int j = 0; j < tree.get(i).size(); j++) { //getting the integer(ID)
                int ID = tree.get(i).get(j); //possible path for route
                for(int k=0;k<currentVertex.EdgeList.size();k++){
                    MapEdge currentEdge = currentVertex.EdgeList.get(k);
                    if(currentEdge.destination.ID==ID){

                    }
                }
            }
        }
    }

}
