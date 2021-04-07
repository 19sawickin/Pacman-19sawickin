package pacman;

public class Game {

    private MazeSquare[][] _map;

    public Game() {
        this.setupBoard();
        _map = new MazeSquare[Constants.ROWS][Constants.COLUMNS];

    }

    public void setupBoard() {

    }
}
