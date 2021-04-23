package pacman;

import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The dot class generates a circle that pacman can eat and get 10 points for. Pacman
 * can also win the game if all of the dots and energizers are eaten.
 */
public class Dot implements Collidable {

    private Circle _dot;

    /**
     * The same dot is created all throughout the board depending on the map support class
     */
    public Dot(Pane gamePane, int i, int j) {
        _dot = new Circle(Constants.DOT_RADIUS);
        _dot.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _dot.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _dot.setFill(Color.WHITE);
        gamePane.getChildren().add(_dot);
    }

    /**
     * The collide method removes the dot from the gamePane when it's collided with
     * and also decrements the dotCounter. It also returns a score of 10 when pacman
     * collides with a dot.
     */
    public int collide(Pacman pacman, Pane gamePane, Game game) {
        gamePane.getChildren().remove(this.getNode());
        game.subtractDotCounter();
        return 10;
    }

    public Node getNode() {
        return _dot;
    }
}
