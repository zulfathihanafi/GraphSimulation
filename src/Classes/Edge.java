package Classes;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Edge extends Group {

    protected CellNodes source;
    protected CellNodes target;

    Line line;

    public Edge(CellNodes source, CellNodes target) {

        this.source = source;
        this.target = target;

        source.addCellChild(target);
        target.addCellParent(source);

        line = new Line();

        line.startXProperty().bind( source.layoutXProperty().add(source.getBoundsInParent().getWidth() / 5.0));
        line.startYProperty().bind( source.layoutYProperty().add(source.getBoundsInParent().getHeight() / 5.0));

        line.endXProperty().bind( target.layoutXProperty().add( target.getBoundsInParent().getWidth() / 5.0));
        line.endYProperty().bind( target.layoutYProperty().add( target.getBoundsInParent().getHeight() / 5.0));

        getChildren().add( line);

    }

    public CellNodes getSource() {
        return source;
    }

    public CellNodes getTarget() {
        return target;
    }

}