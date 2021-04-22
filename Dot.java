package pacman;

import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot implements Collidable {

    private Circle _dot;

    public Dot(Pane gamePane, int i, int j) {
        _dot = new Circle(Constants.DOT_RADIUS);
        _dot.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _dot.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _dot.setFill(Color.WHITE);
        gamePane.getChildren().add(_dot);
    }

    public int collide(Pacman pacman, Pane gamePane, Game game) {
        gamePane.getChildren().remove(this.getNode());
        return 10;
    }

    public Node getNode() {
        return _dot;
    }
}
