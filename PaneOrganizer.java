package pacman;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

public class PaneOrganizer {

    BorderPane _root;

    public PaneOrganizer() {
        _root = new BorderPane();
        Pane gamePane = new Pane();
        HBox bottomPane = new HBox();
        this.addButton(bottomPane);
        _root.setCenter(gamePane);
        _root.setBottom(bottomPane);
        new Game(gamePane, bottomPane);
    }

    public BorderPane getRoot() {
        return _root;
    }

    /**
     * The addButton method is responsible for adding the quit button to the
     * bottomPane.
     */
    public void addButton(HBox bottomPane) {
        Button b1 = new Button("Quit");
        b1.setOnAction(new PaneOrganizer.QuitHandler());
        bottomPane.getChildren().add(b1);
        bottomPane.setFocusTraversable(false);
        b1.setFocusTraversable(false);
    }

    /**
     * The quit handler is responsible for exiting the game when the 'quit'
     * button is pressed.
     */
    private class QuitHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent event) {
            System.exit(0);
        }
    }

}
