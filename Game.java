package pacman;

import cs15.fnl.pacmanSupport.SquareType;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;

import java.util.LinkedList;

public class Game {

    private Pane _gamePane;
    private Label _scoreLabel;
    private Label _livesLabel;
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

    private boolean _frightMode;

    public Game(Pane gamePane, HBox bottomPane) {
        SquareType map[][] = cs15.fnl.pacmanSupport.SupportMap.getSupportMap();
        _map = new MazeSquare[Constants.ROWS][Constants.COLUMNS];
        _gamePane = gamePane;
        _frightMode = false;
        _ghostPen = new LinkedList<Ghost>();
        _futureX = 1;
        _futureY = 0;
        _direction = Direction.RIGHT;
        this.setupBoard(gamePane, map);
        this.generateMap(map, gamePane);
        this.setupTimeline();
        this.setupScore(bottomPane);
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

    private class GhostTimeHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent kf) {
            if(!_ghostPen.isEmpty()) {
                _ghostPen.get(0).removeFromPen(_ghostPen.get(0));
                _ghostPen.removeFirst();
                //_red.removeFromPen(_red);
            }
        }
    }

    private class TimeHandler implements EventHandler<ActionEvent> {

        private int _modeCounter = 0;
        private int _scatterCounter = 0;
        private int _chaseCounter = 0;
        private int _frightModeCounter = 0;

        public void handle(ActionEvent kf) {
            if(_pacman.getLives()!=0) {
                if(checkValidity(_futureX, _futureY)) {
                    _pacman.move(_direction, _pacman);
                }
                Game.this.checkSquare();
                this.moveGhost();
                Game.this.checkSquare();
            } else {
                _ghostTimeline.stop();
            }
        }

        public void moveGhost() {
            if(_frightMode) {
                //this.frightMode();
                _frightModeCounter++;
                if(_frightModeCounter==7) {
                    _frightMode = false;
                    _modeCounter = 0;
                    _frightModeCounter = 0;
                }
            } else if(_modeCounter < 20) {
                this.chaseMode();
                _modeCounter++;
            } else if(_modeCounter < 27) {
                //this.scatterMode();
                _modeCounter++;
            } else if(_modeCounter==27) {
                _modeCounter = 0;
            }
        }

        public void chaseMode() {
            BoardCoordinate redTarget = new BoardCoordinate(_pacman.getY(), _pacman.getX(),
                    true); // TARGET IS PACMAN
            BoardCoordinate root = new BoardCoordinate(_red.getY()/Constants.SQUARE_WIDTH,
                    _red.getX()/Constants.SQUARE_WIDTH, false); // RED'S TARGET

            BoardCoordinate pinkTarget = new BoardCoordinate(_pacman.getY() + 1, _pacman.getX() - 3,
                    true); // PINK TARGETS 3 SPACES LEFT 1 SPACE DOWN FROM PACMAN
            BoardCoordinate pinkRoot = new BoardCoordinate(_pink.getY()/Constants.SQUARE_WIDTH,
                    _pink.getX()/Constants.SQUARE_WIDTH, false);

            BoardCoordinate blueTarget = new BoardCoordinate(_pacman.getY(), _pacman.getX() + 2,
                    true); // PINK TARGETS 3 SPACES LEFT 1 SPACE DOWN FROM PACMAN
            BoardCoordinate blueRoot = new BoardCoordinate(_blue.getY()/Constants.SQUARE_WIDTH,
                    _blue.getX()/Constants.SQUARE_WIDTH, false);

            BoardCoordinate orangeTarget = new BoardCoordinate(_pacman.getY() - 4, _pacman.getX(),
                    true); // PINK TARGETS 3 SPACES LEFT 1 SPACE DOWN FROM PACMAN
            BoardCoordinate orangeRoot = new BoardCoordinate(_orange.getY()/Constants.SQUARE_WIDTH,
                    _orange.getX()/Constants.SQUARE_WIDTH, false);

            _red.move(_red.bfs(redTarget, root, _map, GhostColor.RED), _red, _map, GhostColor.RED);
            _pink.move(_pink.bfs(pinkTarget, pinkRoot, _map, GhostColor.PINK), _pink, _map, GhostColor.PINK);
            _blue.move(_blue.bfs(blueTarget, blueRoot, _map, GhostColor.BLUE), _blue, _map, GhostColor.BLUE);
            _orange.move(_orange.bfs(blueTarget, orangeRoot, _map, GhostColor.ORANGE), _orange, _map, GhostColor.ORANGE);
            _chaseCounter++;
        }

        public void scatterMode() {
            BoardCoordinate target = new BoardCoordinate(1, 1,
                    true);
            BoardCoordinate root = new BoardCoordinate(_red.getY()/Constants.SQUARE_WIDTH,
                    _red.getX()/Constants.SQUARE_WIDTH, false);
            _red.move(_red.bfs(target, root, _map, GhostColor.RED), _red, _map, GhostColor.RED);
            _scatterCounter++;
        }

        public void frightMode() {
            //_red.changeColor(_red, _frightMode);
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
        int collideReturn = 0;
        if(!_map[i][j].getArrayList().isEmpty()) {
            for(int k=0; k<_map[i][j].getArrayList().size(); k++) {
                Collidable object = _map[i][j].getArrayList().get(k);
                collideReturn = object.collide(_red, _pacman, _gamePane, this);
                _score = _score + collideReturn; // _score = _score + object.collide(_red, _pacman, _ghostPen, _gamePane);
                if(collideReturn==0 || collideReturn==200) { // basically if a ghost was collided with

                }
                _gamePane.getChildren().remove(object.getNode());
                _map[i][j].getArrayList().remove(object);
            }
        }
        this.updateScore();
    }

    public void setupScore(HBox bottomPane) {
        _score = 0;
        _scoreLabel = new Label("Score: " + _score);
        _livesLabel = new Label("Lives " + _pacman.getLives());
        _scoreLabel.setTextFill(Color.WHITE);
        _livesLabel.setTextFill(Color.WHITE);
        _livesLabel.setAlignment(Pos.BASELINE_CENTER);
        bottomPane.getChildren().addAll(_scoreLabel, _livesLabel);
    }

    public void updateScore() {
        _scoreLabel.setText("Score: " + _score);
        _livesLabel.setText("Lives: " + _pacman.getLives());
    }

    public boolean getFrightMode() {
        return _frightMode;
    }

    public void setFrightMode(Boolean fright) {
        _frightMode = fright;
        if(_frightMode) {
            _red.getGhost().setFill(Color.BLUE);
            _pink.getGhost().setFill(Color.BLUE);
            _blue.getGhost().setFill(Color.BLUE);
            _orange.getGhost().setFill(Color.BLUE);
        }
    }
}