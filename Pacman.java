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

    public void move(Direction direction) {
        switch(direction) {
            case LEFT:
                _pacman.setCenterX(_pacman.getCenterX() + -10);
                break;
            case RIGHT:
                _pacman.setCenterX(_pacman.getCenterX() + 10);
                break;
            case UP:
                _pacman.setCenterY(_pacman.getCenterY() + -10);
                break;
            case DOWN:
                _pacman.setCenterY(_pacman.getCenterY() + 10);
                break;
            default:
                break;
        }
    }
}
