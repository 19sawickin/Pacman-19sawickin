package pacman;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Pacman {

    private Circle _pacman;

    public Pacman(Pane gamePane, int i, int j) {
        _pacman = new Circle(Constants.PACMAN_RADIUS);
        _pacman.setCenterX(j*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _pacman.setCenterY(i*Constants.SQUARE_WIDTH + Constants.OFFSET);
        _pacman.setFill(Color.YELLOW);
        gamePane.getChildren().add(_pacman);
    }

    public int getX() {
        return (int)(_pacman.getCenterX() - Constants.OFFSET)/Constants.SQUARE_WIDTH;
    }

    public int getY() {
        return (int)(_pacman.getCenterY() - Constants.OFFSET)/Constants.SQUARE_WIDTH;
    }

    public void move(Direction direction, Pacman pacman) {
        switch(direction) {
            case LEFT:
                if(pacman.getX()==1) {
                    _pacman.setCenterX(21*Constants.SQUARE_WIDTH + Constants.OFFSET);
                } else {
                    _pacman.setCenterX(_pacman.getCenterX() + -1*Constants.SQUARE_WIDTH);
                }
                break;
            case RIGHT:
                if(pacman.getX()==21) {
                    _pacman.setCenterX(Constants.OFFSET);
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
