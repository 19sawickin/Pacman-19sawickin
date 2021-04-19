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
                ghost.setX(ghost.getX() - Constants.SQUARE_WIDTH);
                break;
            case RIGHT:
                ghost.setX(ghost.getX() + Constants.SQUARE_WIDTH);
                break;
            case UP:
                ghost.setY(ghost.getY() - Constants.SQUARE_WIDTH);
                break;
            case DOWN:
                ghost.setY(ghost.getY() + Constants.SQUARE_WIDTH);
                break;
            default:
                break;
        }
    }

    public Direction bfs(BoardCoordinate target, BoardCoordinate root, MazeSquare[][] map) {
        LinkedList<BoardCoordinate> Q = new LinkedList();
        Direction[][] directionArray = new Direction[Constants.ROWS][Constants.COLUMNS];
        BoardCoordinate closestSquare = root;
        BoardCoordinate current = root;
        Direction direction = Direction.UP;
        directionArray[root.getRow()][root.getColumn()] = direction;
        this.populateNeighbors(current, map, directionArray, Q);
        while(!Q.isEmpty()) {
            current = Q.remove();
            if(this.distanceToTarget(current, target) <
                this.distanceToTarget(closestSquare, target)) {
                closestSquare = current;
                direction = directionArray[closestSquare.getRow()][closestSquare.getColumn()];
            }
            this.visitNeighbors(current, map, directionArray, Q);
        }
        return direction;
    }

    public void populateNeighbors(BoardCoordinate current, MazeSquare[][] map,
                          Direction[][] directionArray, LinkedList<BoardCoordinate> Q) {
        this.checkNeighbors(0, -1, current, directionArray, map, Q, true); // LEFT
        this.checkNeighbors(0, 1, current, directionArray, map, Q, true); // RIGHT
        this.checkNeighbors(-1, 0, current, directionArray, map, Q, true); // UP
        this.checkNeighbors(1, 0, current, directionArray, map, Q, true); // DOWN
    }

    public void visitNeighbors(BoardCoordinate current, MazeSquare[][] map,
                                  Direction[][] directionArray, LinkedList<BoardCoordinate> Q) {
        this.checkNeighbors(0, -1, current, directionArray, map, Q, false); // LEFT
        this.checkNeighbors(0, 1, current, directionArray, map, Q, false); // RIGHT
        this.checkNeighbors(-1, 0, current, directionArray, map, Q, false); // UP
        this.checkNeighbors(1, 0, current, directionArray, map, Q, false); // DOWN
    }

    public void checkNeighbors(int i, int j, BoardCoordinate current,
                               Direction[][] directionArray, MazeSquare[][] map,
                               LinkedList<BoardCoordinate> Q, boolean first) {

        if(!first) {
            if(j==1 && current.getColumn()==22 && directionArray[current.getRow()+i][0]==null) {
                directionArray[current.getRow()][0] = directionArray[current.getRow()][current.getColumn()];
                Q.add(new BoardCoordinate(current.getRow(),0,false));
            } else if(j==-1 && current.getColumn()==0 && directionArray[current.getRow()+i][22]==null) {
                directionArray[current.getRow()][22] = directionArray[current.getRow()][current.getColumn()];
                Q.add(new BoardCoordinate(current.getRow(),22,false));
            } else if (!map[current.getRow()+i][current.getColumn()+j].getIsAWall() &&
            directionArray[current.getRow()+i][current.getColumn()+j]==null) {

                directionArray[current.getRow()+i][current.getColumn()+j] =
                        directionArray[current.getRow()][current.getColumn()];

                Q.add(new BoardCoordinate(current.getRow() + i,
                        current.getColumn() + j, false));
            }
        } else {
            if(j==1 && current.getColumn()==22 && directionArray[current.getRow()+i][0]==null) {
                directionArray[current.getRow()][0] = Direction.RIGHT;
                Q.add(new BoardCoordinate(current.getRow(),0,false));
            } else if(j==-1 && current.getColumn()==0 && directionArray[current.getRow()+i][22]==null) {
                directionArray[current.getRow()][22] = Direction.LEFT;
                Q.add(new BoardCoordinate(current.getRow(),22,false));
            } else if(!map[current.getRow()+i][current.getColumn()+j].getIsAWall()) {
                switch(j) {
                    case -1:
                        directionArray[current.getRow()+i][current.getColumn()+j] =
                                Direction.LEFT;
                        break;
                    case 1:
                        directionArray[current.getRow()+i][current.getColumn()+j] =
                                Direction.RIGHT;
                        break;
                    default:
                        break;
                }
                switch(i) {
                    case -1:
                        directionArray[current.getRow()+i][current.getColumn()+j] =
                                Direction.UP;
                        break;
                    case 1:
                        directionArray[current.getRow()+i][current.getColumn()+j] =
                                Direction.DOWN;
                        break;
                    default:
                        break;
                }
            }
            Q.add(new BoardCoordinate(current.getRow()+i,
                    current.getColumn()+j, false));
        }
    }

    public int distanceToTarget(BoardCoordinate current, BoardCoordinate target) {
        double distance = Math.sqrt((current.getRow()-target.getRow())^2 +
                                (current.getColumn()-target.getColumn())^2);
        return (int)distance;
    }
}