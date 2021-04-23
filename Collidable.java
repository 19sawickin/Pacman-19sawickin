package pacman;

import javafx.scene.*;
import javafx.scene.layout.Pane;

/**
 * The collidable interface is implemented by the Ghost, Dot, and Energizer class as they
 * are all collidable objects. The most important method that they all share is the collide
 * method. Each object behaves differently depending when pacman collides with them.
 */
public interface Collidable {
    int collide(Pacman pacman, Pane gamePane, Game game);
    Node getNode();
}
