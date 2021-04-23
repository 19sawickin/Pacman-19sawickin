package pacman;

import cs15.fnl.pacmanSupport.SquareType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The Game class contains most of the logic of pacman. It
 * sets up the board and populates it with all of the dots/
 * energizers/ghosts/pacman in the proper spot then generates
 * the two main timelines -- one for the ghost pen and
 * the other for general game. The main timeline handles switching
 * between the various modes and the ghost timeline handles the rate
 * at which ghosts in the ghost pen are released. The game ends when pacman
 * no longer has any lives or if all of the dots/energizers are cleared.
 */

public class Game {

    private Pane _gamePane;
    private Label _scoreLabel;
    private Label _livesLabel;
    private Label _gameOverLabel;
    private Label _youWinLabel;
    private MazeSquare[][] _map;
    private Timeline _timeline;
    private Timeline _ghostTimeline;
    private LinkedList<Ghost> _ghostPen;
    private Pacman _pacman;
    private Ghost _red;
    private Ghost _blue;
    private Ghost _orange;
    private Ghost _pink;
    private Direction _direction;
    private int _futureX;
    private int _futureY;
    private int _score;
    private int _dotCounter;
    private boolean _frightMode;
    private int _frightModeCounter;
    private int _modeCounter;

    /**
     * The game constructor is in charge of initializing most of the variables that
     * keep track of the game status as well as for calling the methods to set up the board
     * and populate it with the proper pieces. The timelines are also set up here
     */
    public Game(Pane gamePane, HBox bottomPane) {
        SquareType map[][] = cs15.fnl.pacmanSupport.SupportMap.getSupportMap();
        _map = new MazeSquare[Constants.ROWS][Constants.COLUMNS];
        _gamePane = gamePane;
        _frightMode = false;
        _frightModeCounter = 0;
        _modeCounter = 0;
        _ghostPen = new LinkedList<Ghost>();
        _futureX = 1;
        _futureY = 0;
        _dotCounter = 0;
        _direction = Direction.RIGHT;
        this.setupBoard(gamePane, map);
        this.generateMap(map, gamePane);
        this.setupTimeline();
        this.setupScore(bottomPane);
        gamePane.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler());
        gamePane.setFocusTraversable(true);
    }

    /**
     * This method sets up the board by taking in the support map and setting
     * up all of the walls and free squares. These are all MazeSquares which are
     * stored in the _map array.
     */
    public void setupBoard(Pane gamePane, SquareType[][] map) {
        for(int i=0; i<Constants.ROWS; i++) {
            for(int j=0; j<Constants.COLUMNS; j++) {
                if(map[i][j]==SquareType.WALL) {
                    _map[i][j] = new MazeSquare(gamePane, Color.DARKBLUE, i, j);
                    _map[i][j].setIsAWall(true);
                }
                else {
                    _map[i][j] = new MazeSquare(gamePane, Color.BLACK, i, j);
                    _map[i][j].setIsAWall(false);
                }
            }
        }
    }

    /**
     * This method populates the _map with dots, energizers, and ghosts
     * depending on what's contained in the map support code. Every time a
     * dot or an energizer is added to the board, the dot counter increments by
     * 1. Similarly, when a dot or energizer is removed, this variable is decremented
     * so that the game is over when there are no more dots/energizers left on the board.
     * The ghosts are also added to the ghostPen arrayList here.
     */
    public void generateMap(SquareType[][] map, Pane gamePane) {
        for(int i=0; i<Constants.ROWS; i++) {
            for (int j = 0; j<Constants.COLUMNS; j++) {
                switch(map[i][j]) {
                    case DOT:
                        _map[i][j].getArrayList().add(new Dot(gamePane, i, j));
                        _dotCounter++;
                        break;
                    case ENERGIZER:
                        _map[i][j].getArrayList().add(new Energizer(gamePane, i, j));
                        _dotCounter++;
                        break;
                    case PACMAN_START_LOCATION:
                        _pacman = new Pacman(gamePane, i, j);
                        break;
                    case GHOST_START_LOCATION:
                        _red = new Ghost(gamePane, i, j, Color.RED, 0, -2);
                        _map[i][j].getArrayList().add(_red);
                        _pink = new Ghost(gamePane, i, j, Color.PINK, -1, 0);
                        _ghostPen.add(_pink);
                        _blue = new Ghost(gamePane, i, j, Color.LIGHTBLUE, 0, 0);
                        _ghostPen.add(_blue);
                        _orange = new Ghost(gamePane, i, j, Color.ORANGE, 1, 0);
                        _ghostPen.add(_orange);
                    default:
                        break;
                }
            }
        }
    }

    /**
     * This method is in charge of constructing the two timelines and
     * playing them.
     */
    public void setupTimeline() {
        _timeline = new Timeline
                (new KeyFrame(Duration.seconds(Constants.DURATION),
                        new TimeHandler()));
        _ghostTimeline = new Timeline
                (new KeyFrame(Duration.seconds(Constants.GHOST_DURATION),
                        new GhostTimeHandler()));
        _timeline.setCycleCount(Animation.INDEFINITE);
        _ghostTimeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
        _ghostTimeline.play();
    }

    /**
     * This timehandler handles the ghost pen. Each keyframe is 5 seconds, so
     * when there's a ghost in the ghost pen, it gets released after 5 seconds.
     * Ghosts get added to the pen when they're collided with. This happens in the
     * collide method further down and the Ghost class accesses the ghostPen array
     * list from its collide method and adds the ghost that way.
     */
    private class GhostTimeHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent kf) {
            if(!_ghostPen.isEmpty()) {
                _ghostPen.get(0).removeFromPen(_ghostPen.get(0), _map);
                _ghostPen.removeFirst();
            }
        }
    }

    /**
     * This timehandler is in charge of updating every 0.25 seconds to
     * move the ghosts and pacman. If pacman still has lives and the game
     * has not been won, it'll check to see if pacman can move, then move
     * pacman, check to see if there are any collisions, move the ghosts,
     * then check to see if there are any collisions again. But if pacman has
     * 0 lives, the game will display a "you lose" label and the timeline will
     * stop. Alternatively, if all the dots/energizers are eaten, a "you win"
     * label will be displayed.
     */
    private class TimeHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent kf) {
            if(_pacman.getLives()!=0 && _dotCounter!=0) {
                if(checkValidity(_futureX, _futureY)) {
                    _pacman.move(_direction, _pacman);
                }
                Game.this.checkSquare();
                this.moveGhost();
                Game.this.checkSquare();
            } else if(_dotCounter==0) {
                _youWinLabel = new Label("You Win");
                _gamePane.getChildren().add(_youWinLabel);
                _ghostTimeline.stop();
                _timeline.stop();
            } else {
                _gameOverLabel = new Label("You Lose");
                _gameOverLabel.setTextFill(Color.WHITE);
                _gamePane.getChildren().add(_gameOverLabel);
                _ghostTimeline.stop();
                _timeline.stop();
            }
        }

        /**
         * This method is responsible for moving the ghosts. The roots are found for each of the ghosts and
         * this remains constant between the different modes. Each time moveGhost is called, the method will
         * see if the fright mode variable is true, and if it is, it will run fright mode. Chase and scatter
         * are determined by the modeCounter variable where chase mode is run for 20 seconds and scatter
         * is run for the next 7 seconds.
         */
        public void moveGhost() {
            BoardCoordinate redRoot = new BoardCoordinate(_red.getY()/Constants.SQUARE_WIDTH,
                    _red.getX()/Constants.SQUARE_WIDTH, false); // RED'S TARGET
            BoardCoordinate pinkRoot = new BoardCoordinate(_pink.getY()/Constants.SQUARE_WIDTH,
                    _pink.getX()/Constants.SQUARE_WIDTH, false);
            BoardCoordinate blueRoot = new BoardCoordinate(_blue.getY()/Constants.SQUARE_WIDTH,
                    _blue.getX()/Constants.SQUARE_WIDTH, false);
            BoardCoordinate orangeRoot = new BoardCoordinate(_orange.getY()/Constants.SQUARE_WIDTH,
                    _orange.getX()/Constants.SQUARE_WIDTH, false);
            if(_frightMode) {
                //this.frightMode(redRoot, pinkRoot, blueRoot, orangeRoot);
                this.chaseMode(redRoot, pinkRoot, blueRoot, orangeRoot);
                _frightModeCounter++;
                if(_frightModeCounter==7/Constants.DURATION) {
                    Game.this.setFrightMode(false);
                    _modeCounter = 0;
                    _frightModeCounter = 0;
                }
            } else if(_modeCounter < 20/Constants.DURATION) {
                this.chaseMode(redRoot, pinkRoot, blueRoot, orangeRoot);
                _modeCounter++;
            } else if(_modeCounter < 35/Constants.DURATION) {
                this.scatterMode(redRoot, pinkRoot, blueRoot, orangeRoot);
                _modeCounter++;
            } else if(_modeCounter==27) {
                _modeCounter = 0;
            }
        }

        /**
         * The chase method generates targets for each of the different ghosts, which all target pacman or
         * a square around pacman. These targets are all passed into the "move all" method called at the end
         * of the chaseMode method which actually calls on the ghosts to move in the ghost class.
         */
        public void chaseMode(BoardCoordinate redRoot, BoardCoordinate pinkRoot, BoardCoordinate blueRoot, BoardCoordinate orangeRoot) {
            BoardCoordinate redTarget = new BoardCoordinate(_pacman.getY(), _pacman.getX(),
                    true); // TARGET IS PACMAN
            BoardCoordinate pinkTarget = new BoardCoordinate(_pacman.getY() + 1, _pacman.getX() - 3,
                    true); // PINK TARGETS 3 SPACES LEFT 1 SPACE DOWN FROM PACMAN
            BoardCoordinate blueTarget = new BoardCoordinate(_pacman.getY(), _pacman.getX() + 2,
                    true); // BLUE TARGETS 2 SPACES RIGHT OF PACMAN
            BoardCoordinate orangeTarget = new BoardCoordinate(_pacman.getY() - 4, _pacman.getX(),
                    true); // ORANGE TARGETS 4 SPACES DOWN FROM PACMAN
            this.moveAll(redRoot, pinkRoot, blueRoot, orangeRoot, redTarget, pinkTarget, blueTarget, orangeTarget);
        }

        /**
         * The scatter mode sets different targets for each of the ghosts. The targets are simply different corners
         * of the board, one for each of the four ghosts. Like in chase mode, the "move all" method is called again
         * and these targets are passed through to bfs in the ghost class.
         */
        public void scatterMode(BoardCoordinate redRoot, BoardCoordinate pinkRoot, BoardCoordinate blueRoot, BoardCoordinate orangeRoot) {
            BoardCoordinate redTarget = new BoardCoordinate(Constants.RED_CORNER_Y, Constants.RED_CORNER_X,
                    true);
            BoardCoordinate pinkTarget = new BoardCoordinate(Constants.PINK_CORNER_Y, Constants.PINK_CORNER_X,
                    true);
            BoardCoordinate blueTarget = new BoardCoordinate(Constants.BLUE_CORNER_Y, Constants.BLUE_CORNER_X,
                    true);
            BoardCoordinate orangeTarget = new BoardCoordinate(Constants.ORANGE_CORNER_Y, Constants.ORANGE_CORNER_X,
                    true);
            this.moveAll(redRoot, pinkRoot, blueRoot, orangeRoot, redTarget, pinkTarget, blueTarget, orangeTarget);
        }

        /**
         * In frightMode, a random direction is generated, and instead of passing through a bfs call for the
         * direction, this random direction is passed through into the move method. The random direction is
         * generated further down in the game class.
         */
        public void frightMode(BoardCoordinate redRoot, BoardCoordinate pinkRoot, BoardCoordinate blueRoot, BoardCoordinate orangeRoot) {
            _red.move(Game.this.generateDirection(_red, GhostColor.RED), _red, _map, GhostColor.RED);
            _pink.move(Game.this.generateDirection(_pink, GhostColor.PINK), _pink, _map, GhostColor.PINK);
            _blue.move(Game.this.generateDirection(_blue, GhostColor.BLUE), _blue, _map, GhostColor.BLUE);
            _orange.move(Game.this.generateDirection(_orange, GhostColor.ORANGE), _orange, _map, GhostColor.ORANGE);
        }

        /**
         * The moveAll() method is where the ghosts actually call the move method that's inside of the ghost
         * class. This is only used by scatter and chase, as bfs is used in these instances.
         */
        public void moveAll(BoardCoordinate redRoot, BoardCoordinate pinkRoot, BoardCoordinate blueRoot, BoardCoordinate orangeRoot,
                            BoardCoordinate redTarget, BoardCoordinate pinkTarget, BoardCoordinate blueTarget, BoardCoordinate orangeTarget) {
            _red.move(_red.bfs(redTarget, redRoot, _map, GhostColor.RED), _red, _map, GhostColor.RED);
            _pink.move(_pink.bfs(pinkTarget, pinkRoot, _map, GhostColor.PINK), _pink, _map, GhostColor.PINK);
            _blue.move(_blue.bfs(blueTarget, blueRoot, _map, GhostColor.BLUE), _blue, _map, GhostColor.BLUE);
            _orange.move(_orange.bfs(orangeTarget, orangeRoot, _map, GhostColor.ORANGE), _orange, _map, GhostColor.ORANGE);
        }
    }

    /**
     * The KeyHandler sees what direction is pressed, checks to see if that move would be
     * valid for pacman, then sets pacman's direction as the keyEvent if it is valid.
     * Pacman would then move in that direction if the move is valid.
     */
    private class KeyHandler implements EventHandler<KeyEvent> {

        public void handle(KeyEvent e) {
            switch(e.getCode()) {
                case LEFT:
                    if(checkValidity(-1, 0)) {
                        _direction = Direction.LEFT;
                    }
                    break;
                case RIGHT:
                    if(checkValidity(1, 0)) {
                        _direction = Direction.RIGHT;
                    }
                    break;
                case UP:
                    if(checkValidity(0 ,-1)) {
                        _direction = Direction.UP;
                    }
                    break;
                case DOWN:
                    if(checkValidity(0, 1)) {
                        _direction = Direction.DOWN;
                    }
                    break;
                default:
                    break;
            }
            e.consume();
        }
    }

    /**
     * This method checks to see if the space that pacman is about to move into is a wall or not.
     * The first if statement is basically for wrapping purposes, because any time move is called
     * on pacman, it has to pass the validity check first. So if it's going through the tunnels
     * and is at square 0 and moving left, it will check at index -1 which is out of bounds. This
     * first if condition overrides that.
     */
    public boolean checkValidity(int futureX, int futureY) {
        if(_pacman.getX()==0 || _pacman.getX()==22) {
            return true;
        }
        if(_map[_pacman.getY() + futureY][_pacman.getX() + futureX].getIsAWall()) {
            return false;
        } else {
            _futureX = futureX;
            _futureY = futureY;
            return true;
        }
    }

    /**
     * This method loops through the array list at a specific square that pacman moves into
     * and calls collide on any of the collidable objects that are inside of array list of
     * that square. When collide is called, an integer is also returned so that the score can
     * be incremented.
     */
    public void checkSquare() {
        int j = _pacman.getX();
        int i = _pacman.getY();
        if(!_map[i][j].getArrayList().isEmpty()) {
            for(int k=0; k<_map[i][j].getArrayList().size(); k++) {
                Collidable object = _map[i][j].getArrayList().get(k);
                _score = _score + object.collide(_pacman, _gamePane, this);
                _map[i][j].getArrayList().remove(object);
            }
        }
        this.updateScore();
    }

    /**
     * This method is responsible for setting up the score and lives labels and placing
     * them into the gamePane. While this is something typically done in the paneOrganizer,
     * putting this method in the game class made life so much easier.
     */
    public void setupScore(HBox bottomPane) {
        _score = 0;
        _scoreLabel = new Label("Score: " + _score);
        _livesLabel = new Label("Lives " + _pacman.getLives());
        _scoreLabel.setTextFill(Color.WHITE);
        _livesLabel.setTextFill(Color.WHITE);
        _scoreLabel.setTranslateX(100);
        _livesLabel.setTranslateX(200);
        bottomPane.getChildren().addAll(_scoreLabel, _livesLabel);
    }

    /**
     * This method updates the text of the score and lives
     */
    public void updateScore() {
        _scoreLabel.setText("Score: " + _score);
        _livesLabel.setText("Lives: " + _pacman.getLives());
    }

    /**
     * This getter method allows the ghost class to access the status of the frightMode
     * variable. This is important because when pacman collides with a ghost, the ghost
     * collide method needs to know what mode the game is currently in in order to act
     * accordingly.
     */
    public boolean getFrightMode() {
        return _frightMode;
    }

    /**
     * This setter method allows the energizer class to be able to access
     * and mutate the frightMode boolean. If pacman collides with an energizer,
     * the boolean is set to true. Then, all of the ghosts change their color to blue.
     * This method is called again after the frightModeCounter reaches 7 seconds, and
     * fright mode is set back to false, changing all of the ghosts back to their
     * original colors.
     */
    public void setFrightMode(Boolean fright) {
        _frightMode = fright;
        if(_frightMode) {
            _red.getGhost().setFill(Color.BLUE);
            _pink.getGhost().setFill(Color.BLUE);
            _blue.getGhost().setFill(Color.BLUE);
            _orange.getGhost().setFill(Color.BLUE);
        } else {
            _red.getGhost().setFill(Color.RED);
            _pink.getGhost().setFill(Color.PINK);
            _blue.getGhost().setFill(Color.LIGHTBLUE);
            _orange.getGhost().setFill(Color.ORANGE);
        }
    }

    /**
     * This method is responsible for generating a random direction used in fright mode. Basically,
     * an array List of directions is created locally and the current ghost checks all of its neighbors.
     * If a neighbor is valid, it is added to the arrayList and a random generator returns a random direction
     * from the arrayList. This gets returned to the "direction" parameter of the ghost's move() method instead
     * of bfs.
     */
    public Direction generateDirection(Ghost ghost, GhostColor ghostColor) {
        ArrayList<Direction> directionArray = new ArrayList();
        this.checkNeighbors(0,-1, ghost, ghostColor, directionArray);
        this.checkNeighbors(0, 1, ghost, ghostColor, directionArray);
        this.checkNeighbors(-1, 0, ghost, ghostColor, directionArray);
        this.checkNeighbors(1, 0, ghost, ghostColor, directionArray);
        int i = (int)(Math.random()*directionArray.size());
        return directionArray.get(i);
    }

    /**
     * This method takes in what direction the ghost is checking (determined by the integer being passed
     * in) and then adds that direction to the directionArray if it's a valid move -- if there's no
     * wall on the map in that direction or if it's not a 180 from the previous direction.
     */
    public void checkNeighbors(int row, int col, Ghost ghost, GhostColor ghostColor, ArrayList<Direction> directionArray) {
        if(!_map[ghost.getY()/Constants.SQUARE_WIDTH + row]
                [ghost.getX()/Constants.SQUARE_WIDTH + col].getIsAWall()) {
            if(col==-1) {
                if(Direction.LEFT != ghost.getOpposite(ghost.getDirection(ghostColor))) {
                    directionArray.add(Direction.LEFT);
                }
            } else if(col==1) {
                if(Direction.RIGHT != ghost.getOpposite(ghost.getDirection(ghostColor))) {
                    directionArray.add(Direction.RIGHT);
                }
            } else if(row==-1) {
                if(Direction.LEFT != ghost.getOpposite(ghost.getDirection(ghostColor))) {
                    directionArray.add(Direction.DOWN);
                }
            } else if(row==1) {
                if(Direction.UP != ghost.getOpposite(ghost.getDirection(ghostColor))) {
                    directionArray.add(Direction.UP);
                }
            }
        }
    }

    /**
     * This method resets the game when pacman collides with a ghost in scatter or
     * chase mode. The ghosts are all sent back to their starting positions and the
     * modeCounter is reset to 0.
     */
    public void resetGame() {
        for(int i=_ghostPen.size(); i>0; i--) {
            _ghostPen.remove();
        }
        _red.setX(Constants.RED_START_X);
        _red.setY(Constants.RED_START_Y);
        _pink.setX(Constants.PINK_START_X);
        _pink.setY(Constants.PINK_START_Y);
        _ghostPen.add(_pink);
        _blue.setX(Constants.BLUE_START_X);
        _blue.setY(Constants.BLUE_START_Y);
        _ghostPen.add(_blue);
        _orange.setX(Constants.ORANGE_START_X);
        _orange.setY(Constants.ORANGE_START_Y);
        _ghostPen.add(_orange);
        _pacman.setX(Constants.PACMAN_START_X);
        _pacman.setY(Constants.PACMAN_START_Y);
        _modeCounter = 0;
        System.out.println("Game reset");
    }

    /**
     * This method is responsible for actually adding the ghosts to the ghost pen
     * both logically and graphically.
     */
    public void addToPen(Ghost ghost) {
        _ghostPen.add(ghost);
        ghost.setX(11*Constants.SQUARE_WIDTH);
        ghost.setY(10*Constants.SQUARE_WIDTH);
    }

    /**
     * This method decrements the dotCounter every time a dot or an energizer is collided
     * with. Once this variable reaches zero, the game ends and you win.
     */
    public void subtractDotCounter() {
        _dotCounter--;
    }
}