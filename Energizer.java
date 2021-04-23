package pacman;

import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Energizer implements Collidable{

    private Circle _energizer;

    public Energizer(Pane gamePane, int i, int j) {
        _energizer = new Circle(Constants.ENERGIZER_RADIUS);
        _energizer.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _energizer.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _energizer.setFill(Color.WHITE);
        gamePane.getChildren().add(_energizer);
    }

    public int collide(Pacman pacman, Pane gamePane, Game game) {
        gamePane.getChildren().remove(this.getNode());
        game.setFrightMode(true);
        game.subtractDotCounter();
        return 100;
    }

    public Node getNode() {
        return _energizer;
    }

}
