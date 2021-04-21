package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.LinkedList;

public class Ghost {

    private Rectangle _ghost;
    private Direction _direction;
    private Boolean _firstRun;

    public Ghost(Pane gamePane, int i, int j, Color color, int xOffset, int yOffset) {
        _firstRun = true;
        _direction = Direction.UP;
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
                if(ghost.getX()==1) {
                    ghost.setX(22*Constants.SQUARE_WIDTH);
                } else {
                    ghost.setX(ghost.getX() - Constants.SQUARE_WIDTH);
                }
                break;
            case RIGHT:
                if(ghost.getX()==21*Constants.SQUARE_WIDTH) {
                    System.out.println("Right side");
                    ghost.setX(0);
                } else {
                    ghost.setX(ghost.getX() + Constants.SQUARE_WIDTH);
                }
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
        _direction = direction;
    }

    public Direction bfs(BoardCoordinate target, BoardCoordinate root, MazeSquare[][] map) {
        LinkedList<BoardCoordinate> Q = new LinkedList();
        Direction[][] directionArray = new Direction[Constants.ROWS][Constants.COLUMNS];
        BoardCoordinate closestSquare = root; // SET ORIGINAL DISTANCE TO BE REALLY LARGE SO IT GOES INTO THE IF STATEMENT
        _firstRun = true;
        BoardCoordinate current = root;
        Direction direction = Direction.UP;
        directionArray[root.getRow()][root.getColumn()] = _direction;
        this.populateNeighbors(current, map, directionArray, Q);
        while(!Q.isEmpty()) {
            current = Q.removeFirst();
            if(_firstRun) {
                closestSquare = current;
                direction = directionArray[closestSquare.getRow()][closestSquare.getColumn()];
                _firstRun = false;
            } else if(this.distanceToTarget(current, target) <
                this.distanceToTarget(closestSquare, target)) {
                closestSquare = current;
                direction = directionArray[closestSquare.getRow()][closestSquare.getColumn()];
            }
            this.visitNeighbors(current, map, directionArray, Q, direction);
        }
        return direction;
    }

    public void populateNeighbors(BoardCoordinate current, MazeSquare[][] map,
                          Direction[][] directionArray, LinkedList<BoardCoordinate> Q) {
        this.checkNeighbors(0, -1, current, directionArray, map, Q, true,  Direction.LEFT); // LEFT
        this.checkNeighbors(0, 1, current, directionArray, map, Q, true,  Direction.RIGHT); // RIGHT
        this.checkNeighbors(-1, 0, current, directionArray, map, Q, true, Direction.UP); // UP
        this.checkNeighbors(1, 0, current, directionArray, map, Q, true, Direction.DOWN); // DOWN
    }

    public void visitNeighbors(BoardCoordinate current, MazeSquare[][] map,
                                  Direction[][] directionArray, LinkedList<BoardCoordinate> Q, Direction direction) {
        this.checkNeighbors(0, -1, current, directionArray, map, Q, false, Direction.LEFT); // LEFT
        this.checkNeighbors(0, 1, current, directionArray, map, Q, false, Direction.RIGHT); // RIGHT
        this.checkNeighbors(-1, 0, current, directionArray, map, Q, false, Direction.UP); // UP
        this.checkNeighbors(1, 0, current, directionArray, map, Q, false, Direction.DOWN); // DOWN
    }

    public void checkNeighbors(int i, int j, BoardCoordinate current,
                               Direction[][] directionArray, MazeSquare[][] map,
                               LinkedList<BoardCoordinate> Q, boolean first, Direction direction) {

        if(current.getColumn()==22 && j==1) {
            j=-22;
        } else if (current.getColumn()==0 && j==-1) {
            j=22;
        }
        if(!first) {
            if (!map[current.getRow()+i][current.getColumn()+j].getIsAWall() &&
                    directionArray[current.getRow()+i][current.getColumn()+j]==null
                    && this.getOpposite(direction)!=_direction) {

                directionArray[current.getRow()+i][current.getColumn()+j] =
                        directionArray[current.getRow()][current.getColumn()];

                Q.add(new BoardCoordinate(current.getRow() + i,
                        current.getColumn() + j, false));
            }
        } else {
            if(!map[current.getRow()+i][current.getColumn()+j].getIsAWall()
                    && this.getOpposite(direction)!=_direction) {
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
            Q.add(new BoardCoordinate(current.getRow()+i,
                        current.getColumn()+j, false));
            }
        }
    }

    public int distanceToTarget(BoardCoordinate current, BoardCoordinate target) {
        double distance = Math.sqrt(Math.pow((current.getRow()-target.getRow()),2) +
                Math.pow((current.getColumn()-target.getColumn()),2));
        return (int)distance;
    }

    public Direction getOpposite(Direction direction) {
        switch(direction) {
            case LEFT:
                direction = Direction.RIGHT;
                break;
            case RIGHT:
                direction = Direction.LEFT;
                break;
            case UP:
                direction = Direction.DOWN;
                break;
            case DOWN:
                direction = Direction.UP;
                break;
            default:
                break;
        }
        return direction;
    }
}