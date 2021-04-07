package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Energizer {

    public Energizer(Pane gamePane, int i, int j) {
        Circle energizer = new Circle(Constants.ENERGIZER_RADIUS);
        energizer.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        energizer.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        energizer.setFill(Color.WHITE);
        gamePane.getChildren().add(energizer);
    }
}
