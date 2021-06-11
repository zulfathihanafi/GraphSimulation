package BackEnd.Simulation.MCTS;

import BackEnd.GraphComponent.MapEdge;
import BackEnd.GraphComponent.MapVertex;
import BackEnd.GraphComponent.Tour;
import BackEnd.map.MapGraph;
import Classes.CellNodes;
import Classes.CircleNode;
import Classes.Edge;
import Classes.Model;
import javafx.concurrent.Task;
import javafx.scene.text.Text;


import java.util.*;

public class NRPA extends Task<String> {
    private MapGraph G;
    private int N, C;
    private double tourCost;

    private static ArrayList<Integer> checkedID;
    static Map<Integer, CellNodes> cellNodesMap = Model.cellMap;
    static Map<String, Edge> edgesMap = Model.edgesMap;

    private static double[][][] policy;
    private static double[][] globalPolicy;
    private Tour bestTour;
    static int ALPHA = 1;
    static long start;
    private Text answer;
    private Text secondIndicator;
    double maxTime = 60.0;

    public NRPA(MapGraph G, int N, int C, Text answer, Text secondIndicator) {
        this.G = G;
        this.N = N;
        this.C = C;
        this.answer = answer;
        this.secondIndicator = secondIndicator;
    }

    public void run(MapGraph G, int N, int C) {
        start = System.nanoTime();

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
        StringBuilder answerText = new StringBuilder();
        Tour best_tour = search(level, iterations);
        setAllEdgeFalse();

        List<List<MapVertex>> routeList = best_tour.getRoute();
        for(int i=0;i< routeList.size();i++){
            List<MapVertex> currentRoute = routeList.get(i);
            for(int j=0;j< currentRoute.size()-1;j++){
                edgesMap.get(currentRoute.get(j).ID+" "+currentRoute.get(j+1).ID).setVisible(true);
            }
        }

        System.out.println(best_tour + "\nTotal Cost: " + best_tour.getTotalDistance());
        answerText.append("NRPA MCTS\nTour Cost: ").append(best_tour.getTotalDistance()).append(best_tour);

        answer.setText(answerText.toString());
        colouringTheNodes();
    }

    private Tour search(int level, int iterations) {

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
                secondIndicator.setText((int) duration_seconds + "s");
                updateProgress(duration_seconds, maxTime);
                //update here
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

    private Tour rollout() {
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

    private MapVertex select_next_move(MapVertex currentStop, List<Integer> possibleSuccessor) {
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

    private void colouringTheNodes() {
        List<List<MapVertex>> list = bestTour.getRoute();
        for (int i = 0; i < list.size(); i++) {
            Random r = new Random();
            int red = r.nextInt(256);
            int green = r.nextInt(256);
            int blue = r.nextInt(256);

            List<MapVertex> currentRoute = list.get(i);
            for (int j = 0; j < currentRoute.size(); j++) {
                CircleNode currentCircle = (CircleNode) cellNodesMap.get(currentRoute.get(j).ID);
                currentCircle.setColour(red, green, blue);
            }
        }
    }

    private static void setAllEdgeFalse(){
        for (Map.Entry<String, Edge> entry : edgesMap.entrySet()) {
            Edge currentEdge = entry.getValue();
            currentEdge.setVisible(false);
        }


    }
    @Override
    protected String call() throws Exception {
        run(G, N, C);
        return bestTour.toString();
    }

    @Override
    public void updateProgress(double v, double v1) {
        super.updateProgress(v, v1);
    }
}
