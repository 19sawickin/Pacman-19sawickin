package pacman;

import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The energizer class creates energizers which are just larger dots. However,
 * when an energizer is collided with, the game switches into frightened mode.
 */
public class Energizer implements Collidable{

    private Circle _energizer;

    /**
     * The energizer constructor sets the original locations of all of the energizers
     * in the game and adds them to the gamePane.
     */
    public Energizer(Pane gamePane, int i, int j) {
        _energizer = new Circle(Constants.ENERGIZER_RADIUS);
        _energizer.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _energizer.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _energizer.setFill(Color.WHITE);
        gamePane.getChildren().add(_energizer);
    }

    /**
     * The energizer's collide method switches the game into frightened mode and
     * also returns a value of 100 when pacman collides with it. The node is also
     * removed here and the dot counter is decremented.
     */
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
