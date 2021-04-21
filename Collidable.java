package pacman;

import javafx.scene.*;

public interface Collidable {
    int collide();
    Node getNode();
}
