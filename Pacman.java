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

    public void move(Direction direction) {
        switch(direction) {
            case LEFT:
                _pacman.setCenterX(_pacman.getCenterX() + -1*Constants.SQUARE_WIDTH);
                break;
            case RIGHT:
                _pacman.setCenterX(_pacman.getCenterX() + Constants.SQUARE_WIDTH);
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

//    public void move(int futureX, int futureY) {
//        _pacman.setCenterX(_pacman.getCenterX() + futureX*Constants.SQUARE_WIDTH);
//        _pacman.setCenterY(_pacman.getCenterY() + futureY*Constants.SQUARE_WIDTH);
//    }
}
