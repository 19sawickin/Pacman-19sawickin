package pacman;

import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.LinkedList;

public class Energizer implements Collidable{

    private Circle _energizer;

    public Energizer(Pane gamePane, int i, int j) {
        _energizer = new Circle(Constants.ENERGIZER_RADIUS);
        _energizer.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _energizer.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _energizer.setFill(Color.WHITE);
        gamePane.getChildren().add(_energizer);
    }

    public int collide(Ghost ghost, Pacman pacman, LinkedList<Ghost> ghostPen, Pane gamePane) {
        ghost.setFrightMode(true);
        ghost.changeColor(ghost, true);
        return 100;
    }

    public Node getNode() {
        return _energizer;
    }

}
