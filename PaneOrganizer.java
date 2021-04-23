package pacman;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

/**
 * The pane organizer sets up all of the panes and also sets
 * up the quit button function. The gamePane and bottomPane
 * are passed into the Game constructor so that the game class
 * can add various labels and update the score more efficiently.
 */
public class PaneOrganizer {

    BorderPane _root;

    /**
     * PaneOrganizer constructor constructs the root, gamePane,
     * bottomPane, and adds the quit button.
     */
    public PaneOrganizer() {
        _root = new BorderPane();
        Pane gamePane = new Pane();
        HBox bottomPane = new HBox();
        this.addButton(bottomPane);
        _root.setCenter(gamePane);
        _root.setBottom(bottomPane);
        new Game(gamePane, bottomPane);
    }

    /**
     * method returns the root
     */
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
