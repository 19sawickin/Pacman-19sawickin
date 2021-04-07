package pacman;

import javafx.scene.layout.BorderPane;

public class PaneOrganizer {

    BorderPane _root;

    public PaneOrganizer() {
        _root = new BorderPane();
        new Game();
    }

    public BorderPane getRoot() {
        return _root;
    }

}
