package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.LinkedList;

public class Ghost {

    private Rectangle _ghost;

    public Ghost(Pane gamePane, int i, int j, Color color, int xOffset, int yOffset) {
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
                ghost.setX(ghost.getX()/Constants.SQUARE_WIDTH - 1);
                break;
            case RIGHT:
                ghost.setX(ghost.getX()/Constants.SQUARE_WIDTH + 1);
                break;
            case UP:
                ghost.setY(ghost.getY()/Constants.SQUARE_WIDTH - 1);
                break;
            case DOWN:
                ghost.setY(ghost.getY()/Constants.SQUARE_WIDTH + 1);
                break;
            default:
                break;
        }
    }

    public Direction bfs(BoardCoordinate target, BoardCoordinate root, MazeSquare[][] map) {
        LinkedList Q = new LinkedList<BoardCoordinate>();
        Direction[][] directionArray = new Direction[Constants.ROWS][Constants.COLUMNS];
        BoardCoordinate closestSquare = root;
        Direction direction = Direction.LEFT;
        this.initialNeighbors(root, map, directionArray, Q);
        while(!Q.isEmpty()) {
            BoardCoordinate current = Q.remove();
            if(this.distanceToTarget(current, target) <
                    this.distanceToTarget(closestSquare, target)) {
                closestSquare = current;
            }
            this.checkNeighbors();
        }
        return direction;
    }

    public void initialNeighbors(BoardCoordinate root, MazeSquare[][] map,
                               Direction[][] directionArray,
                               LinkedList<BoardCoordinate> Q) {
        MazeSquare leftNeighbor = map[root.getRow()][root.getColumn() - 1];
        MazeSquare rightNeighbor = map[root.getRow()][root.getColumn() + 1];
        MazeSquare upNeighbor = map[root.getRow() - 1][root.getColumn()];
        MazeSquare downNeighbor = map[root.getRow() + 1][root.getColumn()];
        if(!leftNeighbor.getIsAWall()) {
            directionArray[root.getRow()][root.getColumn() - 1] = Direction.LEFT;
            Q.add(new BoardCoordinate(root.getRow(), root.getColumn() - 1,
                    false));
        }
        if(!rightNeighbor.getIsAWall()) {
            directionArray[root.getRow()][root.getColumn() + 1] = Direction.RIGHT;
            Q.add(new BoardCoordinate(root.getRow(), root.getColumn() + 1,
                    false));
        }
        if(!upNeighbor.getIsAWall()) {
            directionArray[root.getRow() - 1][root.getColumn()] = Direction.UP;
            Q.add(new BoardCoordinate(root.getRow() - 1, root.getColumn(),
                    false));
        }
        if(!downNeighbor.getIsAWall()) {
            directionArray[root.getRow() + 1][root.getColumn()] = Direction.DOWN;
            Q.add(new BoardCoordinate(root.getRow() + 1, root.getColumn(),
                    false));
        }
    }

    public int distanceToTarget(BoardCoordinate current, BoardCoordinate target) {

    }

    public void checkNeighbors() {


    }
}
