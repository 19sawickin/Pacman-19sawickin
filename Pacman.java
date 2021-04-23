package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * The pacman class constructs pacman and is primarily responsible for graphically
 * updating pacman's location. It also keeps track of pacman's lives.
 */
public class Pacman {

    private Circle _pacman;
    private int _lives;

    /**
     * Pacman's constructor sets pacman's original location and lives at 3
     */
    public Pacman(Pane gamePane, int i, int j) {
        _pacman = new Circle(Constants.PACMAN_RADIUS);
        _pacman.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _pacman.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _pacman.setFill(Color.YELLOW);
        gamePane.getChildren().add(_pacman);
        _lives = 3;
    }

    /**
     * getter that returns the row index of pacman
     */
    public int getX() {
        return (int)(_pacman.getCenterX() - Constants.OFFSET)/Constants.SQUARE_WIDTH;
    }

    /**
     * getter method that returns the column index of the square that pacman is in
     */
    public int getY() {
        return (int)(_pacman.getCenterY() - Constants.OFFSET)/Constants.SQUARE_WIDTH;
    }

    /**
     * setter that sets pacman's x-location
     */
    public void setX(int x) {
        _pacman.setCenterX(x);
    }

    /**
     * setter that sets pacman's y-location
     */
    public void setY(int y) {
        _pacman.setCenterY(y);
    }

    /**
     * This method is an accessor that returns the number of lives that pacman has
     */
    public int getLives() {
        return _lives;
    }

    /**
     * each time this method is called, pacman's lives decrements
     */
    public int subtractLife() {
        return _lives--;
    }

    /**
     * this method takes in the direction that pacman is moving in from the
     * keyhandler in the game class and updates pacman's direction accordingly. It also
     * checks for wrapping when moving left and right.
     */
    public void move(Direction direction, Pacman pacman) {
        switch(direction) {
            case LEFT:
                if(pacman.getX()==0) {
                    _pacman.setCenterX(22*Constants.SQUARE_WIDTH + Constants.OFFSET);
                } else {
                    _pacman.setCenterX(_pacman.getCenterX() + -1*Constants.SQUARE_WIDTH);
                }
                break;
            case RIGHT:
                if(pacman.getX()==22) {
                    _pacman.setCenterX(0);
                } else {
                    _pacman.setCenterX(_pacman.getCenterX() + Constants.SQUARE_WIDTH);
                }
                break;
            case UP:
                _pacman.setCenterY(_pacman.getCenterY() + -1*Constants.SQUARE_WIDTH);
                break;
            case DOWN:
                _pacman.setCenterY(_pacman.getCenterY() + Constants.SQUARE_WIDTH);
                break;
            default:
                break;
        }
    }
}
