package hu.elte.fi.progtech.snake.gui;

import java.awt.*;

public final class SnakeUIConstants  {

    private SnakeUIConstants(){}

    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 640;

    public static final Color BACKGROUND_COLOR = new Color(0xDDEEFF);

    public static final int SCORE_POS_X = 400;
    public static final int SCORE_POS_Y = 60;
    public static final int TITLE_POS_X = 130;
    public static final int TITLE_POS_Y = 150;
    public static final int CLICK_POS_X = 120;
    public static final int CLICK_POS_Y = 400;

    public static final int BLOCK_SIZE = 30;
    public static final double GAME_OVER_SHOW_DEAD = 1.1;

    public static final Color TEXT_COLOR = Color.BLACK;
    public static final Font MAIN_FONT = new Font("Monospaced", Font.BOLD, 48);
    public static final Font SMALL_FONT = MAIN_FONT.deriveFont(Font.BOLD, 18);

    public static final Stroke LARGE_STROKE = new BasicStroke(5);

    public static final Color TITLE_BG_COLOR = Color.WHITE;
    public static final Color GRID_COLOR = new Color(0xBECFEA);
    public static final Color GRID_BORDER_COLOR = new Color(0x7788AA);

    public static final Rectangle GRID_RECTANGLE = new Rectangle(46, 47, 308, 517);
    public static final Rectangle TITLE_RECTANGLE = new Rectangle(100, 85, 252, 100);
    public static final Rectangle CLICK_RECTANGLE = new Rectangle(50, 375, 252, 40);

    public static final int TOP_MARGIN = 50;
    public static final int LEFT_MARGIN = 20;
}
