package pacman;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.ArrayList;

public class MazeSquare {

    private ArrayList<Collidable> _smartSquare;
    private boolean _isAWall;

    public MazeSquare(Pane gamePane, Color color, int i, int j) {
        _isAWall = false;
        _smartSquare = new ArrayList();
        Rectangle mazeSquare = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        mazeSquare.setX(j*Constants.SQUARE_WIDTH);
        mazeSquare.setY(i*Constants.SQUARE_WIDTH);
        mazeSquare.setFill(color);
        gamePane.getChildren().add(mazeSquare);
    }

    public ArrayList<Collidable> getArrayList() {
        return _smartSquare;
    }

    public void setIsAWall(Boolean isAWall) {
        _isAWall = isAWall;
    }

    public boolean getIsAWall() {
        return _isAWall;
    }
}
