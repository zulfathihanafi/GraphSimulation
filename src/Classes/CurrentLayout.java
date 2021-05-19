package Classes;

import java.util.List;
import java.util.Random;

public class CurrentLayout extends Layout{

    Graph graph;


    public CurrentLayout(Graph g){

        this.graph = g;

    }
    @Override
    public void execute() {

        List<CellNodes> cellNodes = graph.getModel().getAllCells();

        for(CellNodes cell:cellNodes){
            //Random rnd = new Random();

            double x = cell.xCoordinate*40;
            double y = cell.yCoordinate*40;

           // double x = rnd.nextDouble()*500;
            //double y = rnd.nextDouble()*500;
            cell.relocate(x,y);
        }

    }
}
