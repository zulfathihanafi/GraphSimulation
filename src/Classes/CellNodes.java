package Classes;



import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class CellNodes extends Pane {

    Integer cellID;
    double xCoordinate,yCoordinate;

    List<CellNodes> child = new ArrayList<>();
    List<CellNodes> parent = new ArrayList<>();

    Node view;

    public CellNodes(Integer cellID,double xCoordinate,double yCoordinate) {
        this.cellID = cellID;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public void addCellChild(CellNodes cell) {
        child.add(cell);
    }

    public List<CellNodes> getCellChildren() {
        return child;
    }

    public void addCellParent(CellNodes cell) {
        parent.add(cell);
    }

    public List<CellNodes> getCellParents() {
        return parent;
    }

    public void removeCellChild(CellNodes cell) {
        child.remove(cell);
    }

    public void setView(Node view) {
        Label idLabel = new Label();
        idLabel.setText(cellID+"");
        idLabel.setTextFill(Color.WHITE);
        this.view = view;
        getChildren().add(view);
        getChildren().add(idLabel);
    }

    public Node getView() {
        return this.view;
    }

    public int getCellId() {
        return cellID;
    }
}
