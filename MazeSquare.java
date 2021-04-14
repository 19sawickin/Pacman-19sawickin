package pacman;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;

public class MazeSquare {

    private ArrayList _smartSquare;
    private Object[][] _map;
    private boolean _isAWall = false;

    public MazeSquare(Pane gamePane, Color color, int i, int j) {
        Rectangle mazeSquare = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        mazeSquare.setX(j*Constants.SQUARE_WIDTH);
        mazeSquare.setY(i*Constants.SQUARE_WIDTH);
        mazeSquare.setFill(color);
        gamePane.getChildren().add(mazeSquare);
        //_smartSquare = new ArrayList();
        //_map[i][j] = _smartSquare;
    }

    public void setIsAWall(Boolean isAWall) {
        _isAWall = isAWall;
    }

    public boolean getIsAWall() {
        return _isAWall;
    }
}
