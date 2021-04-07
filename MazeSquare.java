package pacman;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class MazeSquare {

    public MazeSquare(Pane gamePane, Color color, int i, int j) {
        Rectangle mazeSquare = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        mazeSquare.setX(j*Constants.SQUARE_WIDTH);
        mazeSquare.setY(i*Constants.SQUARE_WIDTH);
        mazeSquare.setFill(color);
        gamePane.getChildren().add(mazeSquare);
    }
}