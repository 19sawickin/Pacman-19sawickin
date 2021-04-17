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

    private Pane _gamePane;
    private MazeSquare[][] _map;
    private Timeline _timeline;
    private Pacman _pacman;
    private Direction _direction;
    private int _futureX;
    private int _futureY;
    private int _score;

    public Game(Pane gamePane) {
        SquareType map[][] = cs15.fnl.pacmanSupport.SupportMap.getSupportMap();
        _map = new MazeSquare[Constants.ROWS][Constants.COLUMNS];
        _gamePane = gamePane;
        _score = 0;
        _pacman = null;
        _futureX = 1;
        _futureY = 0;
        _direction = Direction.RIGHT;
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

    public void generateMap(SquareType[][] map, Pane gamePane) {
        for(int i=0; i<Constants.ROWS; i++) {
            for (int j = 0; j<Constants.COLUMNS; j++) {
                switch(map[i][j]) {
                    case DOT:
                        _map[i][j].getArrayList().add(new Dot(gamePane, i, j));
                        break;
                    case ENERGIZER:
                        _map[i][j].getArrayList().add(new Energizer(gamePane, i, j));
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
            Game.this.checkSquare();
            if(checkValidity(_futureX, _futureY)) {
                _pacman.move(_direction);
            }
        }
    }

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

    public boolean checkValidity(int futureX, int futureY) {
        if(_map[_pacman.getY() + futureY][_pacman.getX() + futureX].getIsAWall()) {
            return false;
        } else {
            _futureX = futureX;
            _futureY = futureY;
            return true;
        }
    }

    public void checkSquare() {
        int j = _pacman.getX();
        int i = _pacman.getY();
        if(!_map[i][j].getArrayList().isEmpty()) {
            for(int k=0; k<_map[i][j].getArrayList().size(); k++) {
                Collidable object = _map[i][j].getArrayList().get(k);
                object.collide(_score);
                _gamePane.getChildren().remove(object.getNode());
                _map[i][j].getArrayList().remove(object);
            }
        }
    }
}