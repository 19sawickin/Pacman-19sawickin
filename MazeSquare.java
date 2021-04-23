package pacman;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.util.ArrayList;

/**
 * The MazeSquare class is responsible for constructing all of the squares in the
 * pacman board and setting their location/colors.
 */
public class MazeSquare {

    private ArrayList<Collidable> _smartSquare;
    private boolean _isAWall;

    /**
     * The maze square constructor adds all of the maze squares to the board in their
     * proper locations and adds them to the pane. Each time a new square is generated,
     * an array list is also generated, so each smart square has its own array list that
     * will contain collidable objects.
     */
    public MazeSquare(Pane gamePane, Color color, int i, int j) {
        _isAWall = false;
        _smartSquare = new ArrayList();
        Rectangle mazeSquare = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        mazeSquare.setX(j*Constants.SQUARE_WIDTH);
        mazeSquare.setY(i*Constants.SQUARE_WIDTH);
        mazeSquare.setFill(color);
        gamePane.getChildren().add(mazeSquare);
    }

    /**
     * This method returns the array list in a particular smart square
     */
    public ArrayList<Collidable> getArrayList() {
        return _smartSquare;
    }

    /**
     * This method sets if a square is a wall or not
     */
    public void setIsAWall(Boolean isAWall) {
        _isAWall = isAWall;
    }

    /**
     * This getter returns whether or not a square is a wall or a free space
     */
    public boolean getIsAWall() {
        return _isAWall;
    }
}
