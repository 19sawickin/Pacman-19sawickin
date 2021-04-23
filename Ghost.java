package pacman;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.LinkedList;

/**
 * The Ghost class contains a lot of the game logic in the bfs method which is responsible for
 * controlling the direction that ghosts move in during chase and scatter mode.
 */
public class Ghost implements Collidable {

    private Rectangle _ghost;
    private Direction _redDirection;
    private Direction _pinkDirection;
    private Direction _blueDirection;
    private Direction _orangeDirection;
    private Boolean _firstRun;

    /**
     * Each time a ghost is constructed, its direction variable is set to up, subject
     * to change once bfs is run.
     */
    public Ghost(Pane gamePane, int i, int j, Color color, int xOffset, int yOffset) {
        _firstRun = true;
        _redDirection = Direction.UP;
        _pinkDirection = Direction.UP;
        _blueDirection = Direction.UP;
        _orangeDirection = Direction.UP;
        _ghost = new Rectangle(Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        _ghost.setX(j*Constants.SQUARE_WIDTH + xOffset*Constants.SQUARE_WIDTH);
        _ghost.setY(i*Constants.SQUARE_WIDTH + yOffset*Constants.SQUARE_WIDTH);
        _ghost.setFill(color);
        gamePane.getChildren().add(_ghost);
    }

    /**
     * The collide method is responsible for handling what happens when pacman collides with a ghost.
     * If the game is in fright mode, the score will increase by 200 points and the ghost will be
     * added back to the ghost pen. But if the game is in chase or scatter mode (i.e. not in fright mode)
     * then the game resets everyone back to their initial positions and pacman loses a life.
     */
    public int collide(Pacman pacman, Pane gamePane, Game game) {
        if(game.getFrightMode()) {
            game.addToPen(this);
            return 200;
        } else {
            game.resetGame();
            pacman.subtractLife();
            return 0;
        }
    }

    /**
     * This method graphically removes the ghosts from the ghost pen and adds them to the
     * array list of the square right outside of the ghost pen.
     */
    public void removeFromPen(Ghost ghost, MazeSquare[][] map) {
        map[11][8].getArrayList().add(ghost);
        ghost.setX(11*Constants.SQUARE_WIDTH);
        ghost.setY(8*Constants.SQUARE_WIDTH);
    }

    public Node getNode() {
        return _ghost;
    }

    public Rectangle getGhost() {
        return _ghost;
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

    /**
     * The move method checks to see if the ghost is at column 0 or 22 and wraps the ghost if
     * it is. This method also takes in the direction from bfs which dictates what direction
     * the ghost is to be moved in. Notable, the ghost is removed from the square's array list
     * then added back to the new square's array list at the end of this method.
     */
    public void move(Direction direction, Ghost ghost, MazeSquare[][] map, GhostColor ghostColor) {
        map[ghost.getY()/Constants.SQUARE_WIDTH][ghost.getX()/Constants.SQUARE_WIDTH].getArrayList().remove(ghost);
        switch(direction) {
            case LEFT:
                if(ghost.getX()==0) {
                    ghost.setX(22*Constants.SQUARE_WIDTH);
                } else {
                    ghost.setX(ghost.getX() - Constants.SQUARE_WIDTH);
                }
                break;
            case RIGHT:
                if(ghost.getX()==22*Constants.SQUARE_WIDTH) {
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
        map[ghost.getY()/Constants.SQUARE_WIDTH][ghost.getX()/Constants.SQUARE_WIDTH].getArrayList().add(ghost);
        this.setDirection(ghostColor, direction);
    }

    /**
     * The bfs method takes in a target and a root and uses these board coordinates to find the quickest
     * route from the root to the target. Every time bfs is called (so in every move method of chase or
     * scatter modes), the ghost is on the square he's on searching for the direction that he should go in
     * in order to be closest to the target. All this method does is return a direction.
     */
    public Direction bfs(BoardCoordinate target, BoardCoordinate root, MazeSquare[][] map, GhostColor ghostColor) {
        LinkedList<BoardCoordinate> Q = new LinkedList();
        Direction[][] directionArray = new Direction[Constants.ROWS][Constants.COLUMNS];
        BoardCoordinate closestSquare = root;
        _firstRun = true;
        BoardCoordinate current = root;
        Direction direction = Direction.UP;
        directionArray[root.getRow()][root.getColumn()] = this.getDirection(ghostColor);
        this.populateNeighbors(current, map, directionArray, Q, ghostColor);
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
            this.visitNeighbors(current, map, directionArray, Q, ghostColor);
        }
        return direction;
    }

    /**
     * This method checks the right/left/up/down neighbors of the ghost trying to move, and sees if they're
     * valid spots that could be moved in. If they are, the ghost will place a left/right/up/down direction
     * in the directionArray that parallels the direction that was searched.
     */
    public void populateNeighbors(BoardCoordinate current, MazeSquare[][] map,
                          Direction[][] directionArray, LinkedList<BoardCoordinate> Q, GhostColor ghostColor) {
        this.checkNeighbors(0, -1, current, directionArray, map, Q, true,  Direction.LEFT, ghostColor); // LEFT
        this.checkNeighbors(0, 1, current, directionArray, map, Q, true,  Direction.RIGHT, ghostColor); // RIGHT
        this.checkNeighbors(-1, 0, current, directionArray, map, Q, true, Direction.UP, ghostColor); // UP
        this.checkNeighbors(1, 0, current, directionArray, map, Q, true, Direction.DOWN, ghostColor); // DOWN
    }

    /**
     * This method visits the left/right/up/down neighbors of the ghost and adds the future direction
     * depending on if it's a valid direction for the ghost to go in.
     */
    public void visitNeighbors(BoardCoordinate current, MazeSquare[][] map,
                                  Direction[][] directionArray, LinkedList<BoardCoordinate> Q, GhostColor ghostColor) {
        this.checkNeighbors(0, -1, current, directionArray, map, Q, false, Direction.LEFT, ghostColor); // LEFT
        this.checkNeighbors(0, 1, current, directionArray, map, Q, false, Direction.RIGHT, ghostColor); // RIGHT
        this.checkNeighbors(-1, 0, current, directionArray, map, Q, false, Direction.UP, ghostColor); // UP
        this.checkNeighbors(1, 0, current, directionArray, map, Q, false, Direction.DOWN, ghostColor); // DOWN
    }

    /**
     * This method is primarily responsible for checking the validity of the ghost's neighbors. It's split
     * into two halves: if this is the first level of the bfs where the neighbors are first being populated,
     * and then every other level of bfs when the neighbors of the neighbors are being checked. If it's an
     * everything else type of situation, depending on what direction the ghost is moving in, that square
     * will be checked and the direction added to the direction array list.
     */
    public void checkNeighbors(int i, int j, BoardCoordinate current,
                               Direction[][] directionArray, MazeSquare[][] map,
                               LinkedList<BoardCoordinate> Q, boolean first, Direction direction, GhostColor ghostColor) {

        if(current.getColumn()==22 && j==1) {
            j=-22;
        } else if (current.getColumn()==0 && j==-1) {
            j=22;
        }
        if(!first) {
            if (!map[current.getRow()+i][current.getColumn()+j].getIsAWall() &&
                    directionArray[current.getRow()+i][current.getColumn()+j]==null
                    && this.getOpposite(direction)!=this.getDirection(ghostColor)) {

                directionArray[current.getRow()+i][current.getColumn()+j] =
                        directionArray[current.getRow()][current.getColumn()];

                Q.add(new BoardCoordinate(current.getRow() + i,
                        current.getColumn() + j, false));
            }
        } else {
            if(!map[current.getRow()+i][current.getColumn()+j].getIsAWall()
                    && this.getOpposite(direction)!=this.getDirection(ghostColor)) {
                switch(j) {
                    case -1:
                    case 22:
                        directionArray[current.getRow()+i][current.getColumn()+j] =
                                Direction.LEFT;
                        break;
                    case 1:
                    case -22:
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

    /**
     * The distance from the root to the target is calculated here
     */
    public int distanceToTarget(BoardCoordinate current, BoardCoordinate target) {
        double distance = Math.sqrt(Math.pow((current.getRow()-target.getRow()),2) +
                Math.pow((current.getColumn()-target.getColumn()),2));
        return (int)distance;
    }

    /**
     * This method returns the opposite of an input direction
     */
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

    /**
     * This method returns the instance variable direction for each of the ghosts depending on
     * what color is passed in. I made GhostColor enums to code more generally this way.
     */
    public Direction getDirection(GhostColor ghostColor) {
        switch(ghostColor) {
            case PINK:
                return _pinkDirection;
            case BLUE:
                return _blueDirection;
            case ORANGE:
                return _orangeDirection;
            default:
                return _redDirection;
        }
    }

    /**
     * This method also takes in the specific ghost color enum and sets the direction
     * for the proper ghost.
     */
    public void setDirection(GhostColor ghostColor, Direction direction) {
        switch(ghostColor) {
            case RED:
                _redDirection = direction;
                break;
            case PINK:
                _pinkDirection = direction;
                break;
            case BLUE:
                _blueDirection = direction;
                break;
            case ORANGE:
                _orangeDirection = direction;
                break;
            default:
                break;
        }
    }
}