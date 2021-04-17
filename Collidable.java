package pacman;

import javafx.scene.*;

public interface Collidable {
    void collide(int score);
    Node getNode();
}
