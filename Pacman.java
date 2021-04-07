package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pacman {

    public Pacman(Pane gamePane, int i, int j) {
        Circle pacman = new Circle(Constants.PACMAN_RADIUS);
        pacman.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        pacman.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        pacman.setFill(Color.YELLOW);
        gamePane.getChildren().add(pacman);
    }
}
