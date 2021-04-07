package pacman;

import cs15.fnl.pacmanSupport.SquareType;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Game {

    private MazeSquare[][] _map;

    public Game(Pane gamePane) {
        SquareType map[][] = cs15.fnl.pacmanSupport.SupportMap.getSupportMap();
        this.setupBoard(map, gamePane);
        _map = new MazeSquare[Constants.ROWS][Constants.COLUMNS];
    }

    public void setupBoard(SquareType[][] map, Pane gamePane) {
        for(int i=0; i<Constants.ROWS; i++) {
            for (int j = 0; j<Constants.COLUMNS; j++) {
                switch(map[i][j]) {
                    case WALL:
                        _map[i][j] = new MazeSquare(gamePane, Color.DARKBLUE, i, j);
                        break;
                    case FREE:
                        _map[i][j] = new MazeSquare(gamePane, Color.BLACK, i, j);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
