package Classes;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class CircleNode extends CellNodes{

    public CircleNode(Integer cellID,int xCoordinate, int yCoordinate) {
        super(cellID,xCoordinate,yCoordinate);

        Circle view = new Circle();


        view.setRadius(15.0);
        view.setStroke(Color.DARKBLUE);
        view.setFill(Color.DARKBLUE);

        setView(view);
    }

}
