package pacman;

public class Constants {

    public static final int SQUARE_WIDTH = 25;
    public static final int COLUMNS = 23;
    public static final int ROWS = 23;
    public static final int SCENE_WIDTH = SQUARE_WIDTH*COLUMNS;
    public static final int SCENE_HEIGHT = SQUARE_WIDTH*ROWS;
    public static final double DURATION = 0.25;
    public static final double GHOST_DURATION = 5;

    public static final int DOT_RADIUS = SQUARE_WIDTH/8;
    public static final int ENERGIZER_RADIUS = SQUARE_WIDTH/5;
    public static final int OFFSET = SQUARE_WIDTH/2;

    public static final int PACMAN_RADIUS = SQUARE_WIDTH/2;
    public static final int PACMAN_START_X = 11*Constants.SQUARE_WIDTH + OFFSET;
    public static final int PACMAN_START_Y = 20*Constants.SQUARE_WIDTH + OFFSET;

    public static final int RED_CORNER_X = Constants.SQUARE_WIDTH;
    public static final int RED_CORNER_Y = Constants.SQUARE_WIDTH;
    public static final int PINK_CORNER_X = 22*Constants.SQUARE_WIDTH;
    public static final int PINK_CORNER_Y = 22*Constants.SQUARE_WIDTH;
    public static final int BLUE_CORNER_X = Constants.SQUARE_WIDTH;
    public static final int BLUE_CORNER_Y = 22*Constants.SQUARE_WIDTH;
    public static final int ORANGE_CORNER_X = 22*Constants.SQUARE_WIDTH;
    public static final int ORANGE_CORNER_Y = Constants.SQUARE_WIDTH;

    public static final int RED_START_X = 10*Constants.SQUARE_WIDTH;
    public static final int RED_START_Y = 8*Constants.SQUARE_WIDTH;

    public static final int PINK_START_X = 9*Constants.SQUARE_WIDTH;
    public static final int PINK_START_Y = 10*Constants.SQUARE_WIDTH;

    public static final int BLUE_START_X = 10*Constants.SQUARE_WIDTH;
    public static final int BLUE_START_Y = 10*Constants.SQUARE_WIDTH;

    public static final int ORANGE_START_X = 11*Constants.SQUARE_WIDTH;
    public static final int ORANGE_START_Y = 10*Constants.SQUARE_WIDTH;
}
