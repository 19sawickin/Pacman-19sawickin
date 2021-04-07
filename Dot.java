package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot {

    public Dot(Pane gamePane, int i, int j) {
        Circle dot = new Circle(Constants.DOT_RADIUS);
        dot.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        dot.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        dot.setFill(Color.WHITE);
        gamePane.getChildren().add(dot);
    }
}
