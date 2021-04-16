package pacman;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot implements Collidable {

    private Pane _gamePane;
    private Label _label;

    public Dot(Pane gamePane, int i, int j) {
        _gamePane = gamePane;
        _label = new Label();
        Circle dot = new Circle(Constants.DOT_RADIUS);
        dot.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        dot.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        dot.setFill(Color.WHITE);
        gamePane.getChildren().add(dot);
    }

    public void collide(int score) {
        score = score + 10;

    }
}
