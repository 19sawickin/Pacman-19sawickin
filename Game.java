package pacman;

import cs15.fnl.pacmanSupport.SquareType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;

public class Game {

    private MazeSquare[][] _map;
    private MazeSquare _mazeSquare;
    private Timeline _timeline;
    private Pacman _pacman;
    private Direction _direction;

    public Game(Pane gamePane) {
        SquareType map[][] = cs15.fnl.pacmanSupport.SupportMap.getSupportMap();
        _map = new MazeSquare[Constants.ROWS][Constants.COLUMNS];
        _pacman = null;
        _mazeSquare = null;
        _direction = Direction.LEFT;
        this.setupBoard(gamePane, map);
        this.generateMap(map, gamePane);
        this.setupTimeline();
        gamePane.addEventHandler(KeyEvent.KEY_PRESSED, new KeyHandler());
        gamePane.setFocusTraversable(true);
    }

    public void setupBoard(Pane gamePane, SquareType[][] map) {
        for(int i=0; i<Constants.ROWS; i++) {
            for(int j=0; j<Constants.COLUMNS; j++) {
                if(map[i][j]==SquareType.WALL) {
                    _mazeSquare = new MazeSquare(gamePane, Color.DARKBLUE, i, j);
                    _mazeSquare.setIsAWall(true);
                }
                else {
                    _mazeSquare = new MazeSquare(gamePane, Color.BLACK, i, j);
                    _mazeSquare.setIsAWall(false);
                }
                _map[i][j] = _mazeSquare;
            }
        }
    }

    public void generateMap(SquareType[][] map, Pane gamePane) {
        for(int i=0; i<Constants.ROWS; i++) {
            for (int j = 0; j<Constants.COLUMNS; j++) {
                switch(map[i][j]) {
                    case DOT:
                        new Dot(gamePane, i, j);
                        break;
                    case ENERGIZER:
                        new Energizer(gamePane, i, j);
                        break;
                    case PACMAN_START_LOCATION:
                        _pacman = new Pacman(gamePane, i, j);
                        break;
                    case GHOST_START_LOCATION:
                        new Ghost(gamePane, i, j, Color.RED, 0, -2);
                        new Ghost(gamePane, i, j, Color.PINK, -1, 0);
                        new Ghost(gamePane, i, j, Color.LIGHTBLUE, 0, 0);
                        new Ghost(gamePane, i, j, Color.ORANGE, 1, 0);
                    default:
                        break;
                }
            }
        }
    }

    public void setupTimeline() {
        _timeline = new Timeline
                (new KeyFrame(Duration.seconds(Constants.DURATION),
                        new TimeHandler()));
        _timeline.setCycleCount(Animation.INDEFINITE);
        _timeline.play();
    }

    private class TimeHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent kf) {
            _pacman.move(_direction);
        }
    }

    private class KeyHandler implements EventHandler<KeyEvent> {

        public void handle(KeyEvent e) {
            switch(e.getCode()) {
                case LEFT:
                    _direction = Direction.LEFT;
                    //_pacman.move(Direction.LEFT);
                    break;
                case RIGHT:
                    _direction = Direction.RIGHT;
                    //_pacman.move(Direction.RIGHT);
                    break;
                case UP:
                    _direction = Direction.UP;
                    //_pacman.move(Direction.UP);
                    break;
                case DOWN:
                    _direction = Direction.DOWN;
                    //_pacman.move(Direction.DOWN);
                    break;
                default:
                    break;
            }
            e.consume();
        }
    }
}
