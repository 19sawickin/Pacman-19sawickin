package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.LinkedList;
import java.util.Queue;

public class Ghost {

    private Rectangle _ghost;
    private Direction[][] _directionArray;
    private LinkedList<BoardCoordinate> Q;
    private BoardCoordinate _root;
    private BoardCoordinate _target;
    private BoardCoordinate _closestSquare;

    public Ghost(Pane gamePane, int i, int j, Color color, int xOffset, int yOffset) {
        _directionArray = new Direction[Constants.ROWS][Constants.COLUMNS];
        _ghost = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        _ghost.setX(j*Constants.SQUARE_WIDTH + xOffset*Constants.SQUARE_WIDTH);
        _ghost.setY(i*Constants.SQUARE_WIDTH + yOffset*Constants.SQUARE_WIDTH);
        _ghost.setFill(color);
        gamePane.getChildren().add(_ghost);
    }

    public int getX() {
        return (int)_ghost.getX();
    }

    public int getY() {
        return (int)_ghost.getY();
    }

    public void setX(int x) {
        _ghost.setX(x);
    }

    public void setY(int y) {
        _ghost.setY(y);
    }

    public void move(Direction direction, Ghost ghost) {
        switch(direction) {
            case LEFT:
                ghost.setX(ghost.getX()/Constants.SQUARE_WIDTH-1);
                break;
            case RIGHT:
                ghost.setX(ghost.getX()/Constants.SQUARE_WIDTH+1);
                break;
            case UP:
                ghost.setY(ghost.getY()/Constants.SQUARE_WIDTH-1);
                break;
            case DOWN:
                ghost.setY(ghost.getY()/Constants.SQUARE_WIDTH+1);
                break;
            default:
                break;
        }
    }

    public Direction bfs(BoardCoordinate target, BoardCoordinate root, MazeSquare[][] map) {
        _root = root;
        _closestSquare = root;
        _target = target;
        Q = new LinkedList<BoardCoordinate>();
        this.checkNeighbors(map);
        Direction direction = Direction.LEFT;
        return direction;
    }

    public void checkNeighbors(MazeSquare[][] map) {

    }
}
