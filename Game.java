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
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {

    private Pane _gamePane;
    private Label _scoreLabel;
    private Label _livesLabel;
    private Label _gameOverLabel;
    private Label _youWinLabel;
    private ArrayList<Circle> _dotArrayList;
    private ArrayList<Circle> _energizerArrayList;
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
    private int _frightModeCounter;
    private int _modeCounter;

    public Game(Pane gamePane, HBox bottomPane) {
        SquareType map[][] = cs15.fnl.pacmanSupport.SupportMap.getSupportMap();
        _map = new MazeSquare[Constants.ROWS][Constants.COLUMNS];
        _gamePane = gamePane;
        _dotArrayList = new ArrayList();
        _energizerArrayList = new ArrayList();
        _frightMode = false;
        _frightModeCounter = 0;
        _modeCounter = 0;
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
                        _map[i][j].getArrayList().add(new Dot(gamePane, i, j, this));
                        break;
                    case ENERGIZER:
                        _map[i][j].getArrayList().add(new Energizer(gamePane, i, j, this));
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
                _ghostPen.get(0).removeFromPen(_ghostPen.get(0), _map);
                _ghostPen.removeFirst();
            }
        }
    }

    private class TimeHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent kf) {
            if(_pacman.getLives()!=0 && !_dotArrayList.isEmpty() && !_energizerArrayList.isEmpty()) {
                if(checkValidity(_futureX, _futureY)) {
                    _pacman.move(_direction, _pacman);
                }
                Game.this.checkSquare();
                this.moveGhost();
                Game.this.checkSquare();
            } else if(_dotArrayList.isEmpty() && _energizerArrayList.isEmpty()) {
                _youWinLabel = new Label("You Win");
                _gamePane.getChildren().add(_youWinLabel);
                _ghostTimeline.stop();
            } else {
                _gameOverLabel = new Label("You Lose");
                _gameOverLabel.setTextFill(Color.WHITE);
                _gamePane.getChildren().add(_gameOverLabel);
                _ghostTimeline.stop();
            }
        }

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
                this.frightMode(redRoot, pinkRoot, blueRoot, orangeRoot);
                //this.chaseMode(redRoot, pinkRoot, blueRoot, orangeRoot);
                _frightModeCounter++;
                if(_frightModeCounter==7/Constants.DURATION) {
                    Game.this.setFrightMode(false);
                    _modeCounter = 0;
                    _frightModeCounter = 0;
                }
            } else if(_modeCounter < 20/Constants.DURATION) {
                this.chaseMode(redRoot, pinkRoot, blueRoot, orangeRoot);
                _modeCounter++;
            } else if(_modeCounter < 27/Constants.DURATION) {
                this.scatterMode(redRoot, pinkRoot, blueRoot, orangeRoot);
                //this.chaseMode(redRoot, pinkRoot, blueRoot, orangeRoot);
                _modeCounter++;
            } else if(_modeCounter==27) {
                _modeCounter = 0;
            }
        }

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

        public void frightMode(BoardCoordinate redRoot, BoardCoordinate pinkRoot, BoardCoordinate blueRoot, BoardCoordinate orangeRoot) {
            _red.move(Game.this.generateDirection(_red, GhostColor.RED), _red, _map, GhostColor.RED);
            _pink.move(Game.this.generateDirection(_pink, GhostColor.PINK), _pink, _map, GhostColor.PINK);
            _blue.move(Game.this.generateDirection(_blue, GhostColor.BLUE), _blue, _map, GhostColor.BLUE);
            _orange.move(Game.this.generateDirection(_orange, GhostColor.ORANGE), _orange, _map, GhostColor.ORANGE);
        }

        public void moveAll(BoardCoordinate redRoot, BoardCoordinate pinkRoot, BoardCoordinate blueRoot, BoardCoordinate orangeRoot,
                            BoardCoordinate redTarget, BoardCoordinate pinkTarget, BoardCoordinate blueTarget, BoardCoordinate orangeTarget) {
            _red.move(_red.bfs(redTarget, redRoot, _map, GhostColor.RED), _red, _map, GhostColor.RED);
            _pink.move(_pink.bfs(pinkTarget, pinkRoot, _map, GhostColor.PINK), _pink, _map, GhostColor.PINK);
            _blue.move(_blue.bfs(blueTarget, blueRoot, _map, GhostColor.BLUE), _blue, _map, GhostColor.BLUE);
            _orange.move(_orange.bfs(orangeTarget, orangeRoot, _map, GhostColor.ORANGE), _orange, _map, GhostColor.ORANGE);
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

    public void checkSquare() {
        int j = _pacman.getX();
        int i = _pacman.getY();
        if(!_map[i][j].getArrayList().isEmpty()) {
            for(int k=0; k<_map[i][j].getArrayList().size(); k++) {
                Collidable object = _map[i][j].getArrayList().get(k);
                _score = _score + object.collide(_pacman, _gamePane, this);; // _score = _score + object.collide(_red, _pacman, _ghostPen, _gamePane);
                //_gamePane.getChildren().remove(object.getNode());
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
        _scoreLabel.setTranslateX(100);
        _livesLabel.setTranslateX(200);
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
        } else {
            _red.getGhost().setFill(Color.RED);
            _pink.getGhost().setFill(Color.PINK);
            _blue.getGhost().setFill(Color.LIGHTBLUE);
            _orange.getGhost().setFill(Color.ORANGE);
        }
    }

    public Direction generateDirection(Ghost ghost, GhostColor ghostColor) {
        ArrayList<Direction> directionArray = new ArrayList();
        this.checkNeighbors(0,-1, ghost, ghostColor, directionArray);
        this.checkNeighbors(0, 1, ghost, ghostColor, directionArray);
        this.checkNeighbors(-1, 0, ghost, ghostColor, directionArray);
        this.checkNeighbors(1, 0, ghost, ghostColor, directionArray);

        int i = (int)(Math.random()*directionArray.size());
        Direction direction = directionArray.get(i);
        return direction;
    }

    public void checkNeighbors(int row, int col, Ghost ghost, GhostColor ghostColor, ArrayList<Direction> directionArray) {
        Direction direction = ghost.getDirection(ghostColor);
        if(!_map[ghost.getY()/Constants.SQUARE_WIDTH + row]
                [ghost.getX()/Constants.SQUARE_WIDTH + col].getIsAWall()) {
            if(col==-1) {
                direction = Direction.LEFT;
            } else if(col==1) {
                direction = Direction.RIGHT;
            } else if(row==-1) {
                direction = Direction.DOWN;
            } else if(row==1) {
                direction = Direction.UP;
            }
            if(direction != ghost.getOpposite(ghost.getDirection(ghostColor))) {
                directionArray.add(direction);
            }
        }
    }

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

    public void addToPen(Ghost ghost, Pane gamePane) {
        _ghostPen.add(ghost);
        ghost.setX(11*Constants.SQUARE_WIDTH);
        ghost.setY(10*Constants.SQUARE_WIDTH);
    }

    public ArrayList<Circle> getDotArrayList() {
        return _dotArrayList;
    }

    public ArrayList<Circle> getEnergizerArrayList() {
        return _energizerArrayList;
    }
}