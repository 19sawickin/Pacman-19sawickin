package pacman;

import javafx.scene.*;
import javafx.scene.layout.Pane;

public interface Collidable {
    int collide(Pacman pacman, Pane gamePane, Game game);
    Node getNode();
}
