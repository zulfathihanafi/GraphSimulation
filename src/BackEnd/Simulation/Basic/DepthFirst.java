package BackEnd.Simulation.Basic;


import BackEnd.GraphComponent.*;
import BackEnd.map.*;
import Classes.CellNodes;
import Classes.CircleNode;
import Classes.Edge;
import Classes.Model;
import Controllers.Simulation;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;


import java.util.*;

public class DepthFirst extends Task<String[]> {
    //the id is array
    static Map<Integer, CellNodes> cellNodesMap = Model.cellMap;
    private  MapGraph G;
    private  int C;
    private  List<String> tree;
    private  HashMap<String, Path> pathMap;
    private List<Path> pathList;
    private  List<String> route;
    private  double tourDistance = Double.MAX_VALUE;
    private  long  start,end,time;
    private  final long maxTime = 60;
    private String[] finalAnswer;
    private Text answer;
    private Text secondIndicator;
    static Map<String, Edge> edgesMap = Model.edgesMap;
    public DepthFirst(MapGraph G, int C,Text answer,Text secondIndicator) {
        this.G = G;
        this.C = C;
        this.answer = answer;
        this.secondIndicator = secondIndicator;
    }

    public void run(MapGraph G, int C) {

        StringBuilder sb = new StringBuilder();
        //initiazlize everything

        pathMap = new HashMap<>();
        pathList = new ArrayList<>();
        route = new ArrayList<>();
        tree = new ArrayList<>();

        System.out.println("---DF Search---\n");
        start = System.currentTimeMillis();
        //System.out.println(result);
        generateTree(0, 0, "");
        search();


        //finding the best tour, the last in list of "route" is considered the lowest
        end = System.currentTimeMillis();
        for(int i=0;i< pathList.size() && time < maxTime ;i++){
            bestTour(i,"","",0.0);

        }

        //printAllEdge();
        //printing the best tour
        for( String element : route){
            System.out.println(element);
        }
        secondIndicator.setText("DONE!");
    }
    //generate tree (limiting the capacity)
    private void generateTree(int capacity, int vertexID, String currentList) {
        MapVertex currentVertex = G.getVertex(vertexID);
        capacity += currentVertex.capacity;
        if (vertexID != 0) {
            currentList += vertexID + " ";
        }
        for (int i = 0; i < currentVertex.EdgeList.size(); i++) {
            MapEdge currentEdge = currentVertex.EdgeList.get(i);
            MapVertex destination = currentEdge.destination;
            if (capacity + destination.capacity > C || currentList.contains(destination.ID + " ") || destination.ID == 0) {
                //put something here?
            } else {
                // System.out.println(capacity);
                generateTree(capacity, destination.ID, currentList);
            }
        }
        if (vertexID != 0) {
            tree.add("0 " + currentList + "0");
        }

    }

    //search for the tree
    private void search() {
        //putting the
        for (String element : tree) {

            double distance = 0;
            int capacity = 0;

            //Generate array of nodes
            String[] strArray = element.split(" ");
            Integer[] nodes = new Integer[strArray.length];
            for (int i = 0; i < strArray.length; i++) {
                nodes[i] = Integer.parseInt(strArray[i]);
                capacity += G.getVertex(nodes[i]).capacity;
            }

            //compute distance fore every path
            for (int i = 0; i < nodes.length - 1; i++) {
                distance += computeDistance(G.getVertex(nodes[i]), G.getVertex(nodes[i + 1]));
            }

            //use map to detect if visited nodes is same
            //example visited nodes 2 3 4 with 50 distance
            //vs visited nodes 3 2 4 with 40 distance
            //the second will replace the first one
            Integer[] nodesSort = nodes.clone();
            Arrays.sort(nodesSort);
            Path newPath = new Path(nodes, distance, capacity);
            Path path = pathMap.putIfAbsent(Arrays.toString(nodesSort), newPath);
            if (path != null) {
                if (path.getDistance() > distance) {
                    pathMap.replace(Arrays.toString(nodesSort), path, newPath);
                }
            }


        }
        //insert everything into list of path with ID
        int currentID = 0;
        for (Map.Entry<String, Path> entry : pathMap.entrySet()) {
            Path currentPath = entry.getValue();
            currentPath.setID(currentID);
            pathList.add(currentPath);
            currentID++;
        }

        for (Path path : pathList) {
            pathGraph(path);
        }

    }

    //calculate distance between two vertex
    private double computeDistance(MapVertex v1, MapVertex v2) {
        double dx = v1.coordinateX - v2.coordinateX;
        double dy = v1.coordinateY - v2.coordinateY;

        return Math.sqrt((dx * dx) + (dy * dy));
    }
    private boolean arrayPathChecker(Path from, Path dest) {
        Integer[] a = from.getNodes();
        Integer[] b = dest.getNodes();

        for (Integer integer : a) {
            if (integer.equals(0)) continue;
            for (Integer value : b) {
                if (integer.equals(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    //creating the graph ( edges for every path )
    private void pathGraph(Path currentPath) {
        List<Path> edges = currentPath.getPathList();

        for (Path edgePath : pathList) {
            if (!currentPath.equals(edgePath) && arrayPathChecker(currentPath, edgePath)) {
                edges.add(edgePath);
            }
        }
    }

    //finding the best tour
    private void bestTour(int pathID, String visitedPath,String visitedNodes,double distance) {
        end = System.currentTimeMillis();
        time = ((end - start)/1000);

        secondIndicator.setText(time+"s");
        updateProgress(time,maxTime);
        if(time < maxTime) {
            Path currentPath = pathList.get(pathID);
            visitedPath += pathID + " ";
            visitedNodes += Arrays.toString(currentPath.getNodes());
            distance += currentPath.getDistance();
            for (int i = 0; i < currentPath.getPathList().size(); i++) {
                Path nextPath = currentPath.getPathList().get(i);

                if (!haveIntegerInString(nextPath.getNodes(), visitedNodes)) {

                    bestTour(nextPath.getID(), visitedPath, visitedNodes, distance);
                }
            }
            if (StringContainAllNodes(visitedNodes)) {
                if (distance < tourDistance) {
                    tourDistance = distance;
                    route.add(visitedPath);
                }
            }

        }
    }
    private boolean haveIntegerInString(Integer[] nodes, String visited) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] == 0) {
                continue;
            }
            if (visited.contains(" " + nodes[i]+",")) {
                return true;
            }

        }

        return false;
    }
    private boolean StringContainAllNodes(String visited){
        for(int i=0;i<G.size();i++){
            if(!visited.contains(" "+i) || !visited.contains(" 1,")) {
                return false;
            }

        }
        return true;
    }

    private static void setAllEdgeFalse(){
        for (Map.Entry<String, Edge> entry : edgesMap.entrySet()) {
            Edge currentEdge = entry.getValue();
            currentEdge.setVisible(false);
        }


    }

    @Override
    protected String[] call() throws Exception {
        run(G,C);
        return finalAnswer;
    }

    @Override
    public void updateProgress(double v, double v1) {
        super.updateProgress(v,v1);
    }

    public void printing(){
        StringBuilder sb = new StringBuilder();
        String[] bestTour = route.get(route.size()-1).split(" ");
        setAllEdgeFalse();
        for(int i=0; i< bestTour.length;i++){
            Random r = new Random();
            int red = r.nextInt(256);
            int green = r.nextInt(256);
            int blue = r.nextInt(256);
            Path currPath = pathList.get(Integer.parseInt(bestTour[i]));
            sb.append("\n-------------------\nVehicle ").append(i + 1).append("\n");
            sb.append(currPath);
            Integer[] nodeId = currPath.getNodes();

            for (Integer integer : nodeId) {

                CircleNode currentCircle = (CircleNode) cellNodesMap.get(integer);
                currentCircle.setColour(red, green, blue);
            }

            for(int j=0;j<nodeId.length-1;j++){
                edgesMap.get(nodeId[j]+" "+nodeId[j+1]).setVisible(true);
            }
        }

        String text = "Depth First Search\n"+"Tour Cost : "+tourDistance+sb.toString();

        System.out.println(text);
        answer.setText(text);
    }

}
