package pacman;

import javafx.scene.*;
import javafx.scene.layout.Pane;

import java.util.LinkedList;

public interface Collidable {
    int collide(Ghost ghost, Pacman pacman, LinkedList<Ghost> ghostPen, Pane gamePane);
    Node getNode();
}
