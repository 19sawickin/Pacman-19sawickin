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
    private Timeline _timeline;
    private Pacman _pacman;
    private Direction _direction;
    private int _futureX;
    private int _futureY;

    public Game(Pane gamePane) {
        SquareType map[][] = cs15.fnl.pacmanSupport.SupportMap.getSupportMap();
        _map = new MazeSquare[Constants.ROWS][Constants.COLUMNS];
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
            _pacman.move(_direction);
        }
    }

    private class KeyHandler implements EventHandler<KeyEvent> {

        public void handle(KeyEvent e) {
            switch(e.getCode()) {
                case LEFT:
                    if(checkValidity(Direction.LEFT)) {
                        _direction = Direction.LEFT;
                    }
                    Game.this.checkValidity();
                    break;
                case RIGHT:
                    Game.this.checkValidity();
                    break;
                case UP:
                    Game.this.checkValidity();
                    break;
                case DOWN:
                    Game.this.checkValidity();
                    break;
                default:
                    break;
            }
            e.consume();
        }
    }

    public void checkValidity(Direction direction) {
        if(_map[_pacman.getX()][_pacman.getY()].getIsAWall()) {
        }
    }
}
