package BackEnd.Simulation.MCTS;

import BackEnd.GraphComponent.MapEdge;
import BackEnd.GraphComponent.MapVertex;
import BackEnd.GraphComponent.Tour;
import BackEnd.map.MapGraph;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NRPA {
    private static MapGraph G;
    private static int N, C;
    private static double tourCost;

    private static ArrayList<Integer> checkedID;

    private static double[][][] policy;
    private static double[][] globalPolicy;
    private static Tour bestTour;
    static int ALPHA = 1;
    static long start;
    public static void run(MapGraph G, int N, int C) {
        start = System.nanoTime();
        NRPA.G = G;
        NRPA.N = N;
        NRPA.C = C;
        checkedID = new ArrayList<>();
        int level = 3, iterations = 100;

        bestTour = new Tour();
        NRPA.policy = new double[level][N][N];
        NRPA.globalPolicy = new double[N][N];
        bestTour.setTotalDistance(Double.POSITIVE_INFINITY);

        //FILL EACH ROW OF THE FIRST LEVEL WITH 0's
        for (double[][] tubes : policy)
            Arrays.stream(tubes).forEach(row -> Arrays.fill(row, 0));

        for (double[] ints : globalPolicy) {
            Arrays.fill(ints, 0);
        }

        Tour best_tour = search(level, iterations);
        System.out.println(best_tour + "\nTotal Cost: " + best_tour.getTotalDistance());

    }

    private static Tour search(int level, int iterations) {

        //Instant start = Instant.now();


        if (level == 0)
            return rollout();
        else {
            policy[level - 1] = NRPA.globalPolicy;

            for (int i = 0; i < iterations; i++) {
                System.out.println("iterations " + i);
                Tour new_tour = search(level - 1, i);
                if (new_tour.getTotalDistance() < bestTour.getTotalDistance()) {
                    bestTour = new_tour;
                    System.out.println("inside search loop\n" + bestTour);
                    adapt(bestTour, level);
                }

                long end = System.nanoTime();
                double duration_seconds = (end - start) * Math.pow(10, -9);
                System.out.println("Time: " + duration_seconds + "s");

                if (duration_seconds > 60)
                    return bestTour;

            }
            globalPolicy = policy[level - 1];
        }

        return bestTour;
    }

    private static void adapt(Tour currentTour, int level) {
        List<Integer> visitedID = new ArrayList<>();

        for (int i = 0; i < currentTour.getRoute().size(); i++) {
            //in tour have route, in route have list of vertex
            List<MapVertex> route = currentTour.getRoute().get(i);
            for (int j = 0; j < route.size() - 1; j++) {
                MapVertex stop = route.get(j); //currentStop
                MapVertex nextStop = route.get(j + 1);
                double z = 0.0;
                policy[level][stop.ID][nextStop.ID] += ALPHA;
                ArrayList<MapEdge> edge = stop.EdgeList;

                //for every possible move that can be made by stop
                for (MapEdge value : edge) {
                    MapVertex move = value.destination;
                    if (!visitedID.contains(move.ID)) {
                        z += Math.exp(globalPolicy[stop.ID][move.ID]);
                    }
                }

                for (MapEdge value : edge) {
                    MapVertex move = value.destination;
                    if (!visitedID.contains(move.ID)) {
                        policy[level][stop.ID][move.ID] -= ALPHA * (Math.exp(globalPolicy[stop.ID][move.ID]) / z);
                    }
                }

                visitedID.add(stop.ID);
            }


        }

        /*
        for every route in a_tour
            for every stop in route
                policy[level][stop][next_stop] += ALPHA
                z = 0.0
                for every possible move that can be made by stop
                    if the move is not visited yet
                        z += Math.exp(globalPolicy[stop][move]);
                for every possible move that can be made by stop
                    if the move is not visited yet
                        policy[level][stop][move] -= ALPHA * (Math.exp(globalPolicy[stop][move]) / z)
                set stop as visited
         */


    }

    private static Tour rollout() {
        Tour newTour = new Tour();
        List<MapVertex> newRoute = new ArrayList<>();
        newRoute.add(G.getHead());
        newTour.getRoute().add(newRoute);


        List<Integer> visitedForRoute = new ArrayList<>();
        List<Integer> visitedForSuccessor = new ArrayList<>();
        List<Integer> possibleSuccessor = new ArrayList<>();
        int capacity = 0;


        while (true) {
            List<MapVertex> lastRoute = newTour.getRoute().get(newTour.getRoute().size() - 1);
            MapVertex currentStop = lastRoute.get(lastRoute.size() - 1);
            List<MapEdge> currentEdge = currentStop.EdgeList;

            //finding the possibleSuccessors
            for (MapEdge edge : currentEdge) {
                MapVertex destination = edge.destination;
                if (destination.ID != 0 && !visitedForRoute.contains(destination.ID) && !visitedForSuccessor.contains(destination.ID)) {
                    possibleSuccessor.add(destination.ID);
                }
            }

            if (possibleSuccessor.isEmpty()) {
                newRoute.add(G.getVertex(0));
                if (visitedForRoute.size() == G.size() - 1) { //if all stop are visited
                    break;
                }
                newRoute = new ArrayList<>();
                newRoute.add(G.getVertex(0));
                newTour.getRoute().add(newRoute);
                visitedForSuccessor = new ArrayList<>();
                capacity = 0;
                continue;
            }

            MapVertex nextStop = select_next_move(currentStop, possibleSuccessor);
            if (capacity + nextStop.capacity <= C) {
                capacity += nextStop.capacity;
                newRoute.add(nextStop);
                visitedForRoute.add(nextStop.ID);

            } else {
                visitedForSuccessor.add(nextStop.ID);
            }
            possibleSuccessor = new ArrayList<>();
        }
        tourDistanceCalculator(newTour);
        return newTour;
    }

    private static MapVertex select_next_move(MapVertex currentStop, List<Integer> possibleSuccessor) {
        double[] probability = new double[possibleSuccessor.size()];
        double sum = 0;
        for (int i = 0; i < possibleSuccessor.size(); i++) {
            probability[i] = Math.exp(globalPolicy[currentStop.ID][possibleSuccessor.get(i)]);
            sum += probability[i];
        }

        double mRand = new Random().nextDouble() * sum;
        sum = probability[0];
        int i = 0;
        while (sum < mRand) {
            sum += probability[++i];
        }
        return G.getVertex(possibleSuccessor.get(i));

    }

    private static void tourDistanceCalculator(Tour currentTour) {
        double totalDistance = 0;
        List<List<MapVertex>> allRoute = currentTour.getRoute();
        for (List<MapVertex> route : allRoute) {
            for (int j = 0; j < route.size() - 1; j++) {
                MapVertex current = route.get(j);
                MapVertex next = route.get(j + 1);
                double dx = current.coordinateX - next.coordinateX;
                double dy = current.coordinateY - next.coordinateY;
                totalDistance += Math.sqrt(dx * dx + dy * dy);
            }
        }
        currentTour.setTotalDistance(totalDistance);
    }

}
