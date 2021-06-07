package Classes;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class CircleNode extends CellNodes{
    public Circle view;
    public CircleNode(Integer cellID,double xCoordinate, double yCoordinate) {
        super(cellID,xCoordinate,yCoordinate);

        view = new Circle(18);

        view.setStroke(Color.DARKBLUE);
        view.setFill(Color.DARKBLUE);

        setView(view);
    }

    public void setColour (int red, int green, int blue){
        view.setFill(Color.rgb(red, green, blue));
    }
    public void setDiameter (double diameter){
        view.setRadius(diameter);
    }


}
