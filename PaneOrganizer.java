package pacman;

import javafx.scene.layout.*;

public class PaneOrganizer {

    BorderPane _root;

    public PaneOrganizer() {
        _root = new BorderPane();
        Pane gamePane = new Pane();
        _root.setCenter(gamePane);
        new Game(gamePane);
    }

    public BorderPane getRoot() {
        return _root;
    }

}
