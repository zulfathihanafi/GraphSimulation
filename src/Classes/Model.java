package Classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Model {

    CellNodes graphParent;

    List<CellNodes> allCells;
    List<CellNodes> addedCells;
    List<CellNodes> removedCells;

    List<Edge> allEdges;
    List<Edge> addedEdges;
    List<Edge> removedEdges;

    public static Map<Integer,CellNodes> cellMap; // <id,cell>
    public static Map<String,Edge> edgesMap;
    public Model() {

        //graphParent = new CellNodes( 0,0,0);

        // clear model, create lists
        clear();
    }

    public void clear() {

        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();

        cellMap = new HashMap<>(); // <id,cell>
        edgesMap = new HashMap<>();

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public List<CellNodes> getAddedCells() {
        return addedCells;
    }

    public List<CellNodes> getRemovedCells() {
        return removedCells;
    }

    public List<CellNodes> getAllCells() {
        return allCells;
    }

    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public void addCell(int id,double xCoordinate, double yCoordinate) {

                CircleNode newCell = new CircleNode(id,xCoordinate,yCoordinate);
                addCell(newCell);

    }

    public void addCell(CellNodes cell) {

        addedCells.add(cell);

        cellMap.put( cell.getCellId(), cell);

    }

    public void addEdge(int sourceId, int targetId) {

        CellNodes sourceCell = cellMap.get( sourceId);
        CellNodes targetCell = cellMap.get( targetId);

        Edge edge = new Edge( sourceCell, targetCell);

        addedEdges.add( edge);
        edgesMap.put(sourceId+" "+targetId,edge);
    }

    /**
     * Attach all cells which don't have a parent to graphParent
     * @param cellList
     */
    public void attachOrphansToGraphParent( List<CellNodes> cellList) {

        for( CellNodes cell: cellList) {
            if( cell.getCellParents().size() == 0) {
                graphParent.addCellChild( cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     * @param cellList
     */
    public void disconnectFromGraphParent( List<CellNodes> cellList) {

        for( CellNodes cell: cellList) {
            graphParent.removeCellChild( cell);
        }
    }

    public void merge() {

        // cells
        allCells.addAll( addedCells);
        allCells.removeAll( removedCells);

        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll( addedEdges);
        allEdges.removeAll( removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }
}
