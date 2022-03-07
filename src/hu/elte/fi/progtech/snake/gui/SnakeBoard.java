package hu.elte.fi.progtech.snake.gui;

import hu.elte.fi.progtech.snake.model.*;
import hu.elte.fi.progtech.snake.model.score.ScoreBoard;
import hu.elte.fi.progtech.snake.resources.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SnakeBoard extends JPanel {

    private final SnakeLogic snakeLogic;

    private Timer timer;

    private double gameOverCounter;

    private boolean savedScore;
    private boolean canTurn;

    private String name;
    private String map;
    private int speed;

    private double clock;
    /**
     * Snake board konstruktor, létrehozza a listenereket és elindítja az új játékot.
     */
    public SnakeBoard(SnakeLogic snakeLogic) {
        this.snakeLogic = snakeLogic;
        setPreferredSize(new Dimension(SnakeUIConstants.GAME_WIDTH, SnakeUIConstants.GAME_HEIGHT));
        setBackground(SnakeUIConstants.BACKGROUND_COLOR);
        setFocusable(true);

        startNewGame();

        /**
         * Ha elveszítettük a játékot majd rákattintunk az ablakra új játékott indíthatunk egy kattintással
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ScoreBoard scoreBoard = snakeLogic.getScoreBoard();
                if (scoreBoard.isGameOver()) {
                    startNewGame();
                    repaint();
                }
            }
        });


        /**
         * Figyeli az éppen leütött billentyűt és a kígyó fejének irányát adja meg, merre menjen tovább.
         * Ha az adott ciklusban aftunk már meg értéket akor abban a ciklusban mégegyet nem tudunk.
         */
        KeyListener snakeKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!canTurn) return;
                canTurn = false;
                final ScoreBoard scoreBoard = snakeLogic.getScoreBoard();
                if (scoreBoard.isGameOver()) {
                    return;
                }
                Direction currentDirection = snakeLogic.getDirection();

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_UP:
                        if (currentDirection != Direction.DOWN)
                            snakeLogic.setDirection(Direction.UP);
                        break;

                    case KeyEvent.VK_LEFT:
                        if (currentDirection != Direction.RIGHT) snakeLogic.setDirection(Direction.LEFT);
                        break;

                    case KeyEvent.VK_RIGHT:
                        if (currentDirection != Direction.LEFT)
                            snakeLogic.setDirection(Direction.RIGHT);
                        break;

                    case KeyEvent.VK_DOWN:
                        if (currentDirection != Direction.UP)
                            snakeLogic.setDirection(Direction.DOWN);
                        break;
                }
                repaint();
            }
        };
        addKeyListener(snakeKeyListener);
    }

    /**
     * A játék megnyitásakor vagy új játék indításakor megjelenő ablakot hozza létre.
     * benne a játékos nevét, játékteret és játéksebességet adhjatuk meg.
     */
    private void createInputDialog() {
        JTextField nameField = new JTextField(5);

        JComboBox<String> mapList = new JComboBox<>(SnakeRules.MAPS);
        mapList.setSelectedIndex(0);

        int maxSpeed = SnakeRules.MAX_TIMER;
        int minSpeed = SnakeRules.MIN_TIMER;

        JSlider speedSlider = new JSlider(minSpeed, maxSpeed, maxSpeed - minSpeed);

        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Name: "));
        myPanel.add(nameField);

        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Map:"));
        myPanel.add(mapList);

        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(new JLabel("Speed:      hard"));
        myPanel.add(speedSlider);
        myPanel.add(new JLabel("easy"));

        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please enter your game settings:", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            name = nameField.getText().trim();
            if(name.equals("")) name = "Anonymous";
            map = (String) Objects.requireNonNull(mapList.getSelectedItem());
            speed = speedSlider.getValue();
        }
    }

    /**
     * Új játék indítása esetén használjuk. Alaphelyzetbe állítja az összes értéket
     * És meghívja a dialog ablakot
     */
    private void startNewGame() {
        stop();

        createInputDialog();

        SnakeRules.TIMER = speed;

        snakeLogic.initWall(map);

        snakeLogic.newGame(GameConstants.NORMAL_ROW_COUNT, GameConstants.NORMAL_COL_COUNT, name);

        int time = SnakeRules.TIMER;

        timer = new Timer(time, oneGameCycleAction);
        timer.start();

        gameOverCounter = SnakeUIConstants.GAME_OVER_SHOW_DEAD * 1000 / time;

        savedScore = false;
        canTurn = true;

        clock = 0;
    }

    private void stop() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }

    /**
     * A kezdőképernyő megrajzoló algoritmus
     */
    private void drawStartScreen(Graphics2D graphics2D) {
        graphics2D.setFont(SnakeUIConstants.MAIN_FONT);

        graphics2D.setColor(SnakeUIConstants.TITLE_BG_COLOR);
        graphics2D.fill(SnakeUIConstants.TITLE_RECTANGLE);
        graphics2D.fill(SnakeUIConstants.CLICK_RECTANGLE);

        graphics2D.setColor(SnakeUIConstants.TEXT_COLOR);
        graphics2D.drawString("Snake", SnakeUIConstants.TITLE_POS_X, SnakeUIConstants.TITLE_POS_Y);

        graphics2D.setFont(SnakeUIConstants.SMALL_FONT);
        graphics2D.drawString("Click to start", SnakeUIConstants.CLICK_POS_X, SnakeUIConstants.CLICK_POS_Y);
    }

    /**
     * A megadott kígyó test elemet rajzolja ki a képernyőre. Az images-ben található képek segítségévgel.
     * @param type a kígyó teste milyen típusú, merre áll
     * @param r elhelyezkedés: row
     * @param c elhelyezkedés: column
     */
    private void drawSnakePart(Graphics2D g, SnakeType type, int r, int c) {
        int x = SnakeUIConstants.LEFT_MARGIN + c * SnakeUIConstants.BLOCK_SIZE;
        int y = SnakeUIConstants.TOP_MARGIN + r * SnakeUIConstants.BLOCK_SIZE;

        try {
            String path;

            switch(type) {
                case BODY_BOTTOM_LEFT -> path = "images/body_bottomleft.png";
                case BODY_BOTTOM_RIGHT -> path = "images/body_bottomright.png";
                case BODY_HORIZONTAL -> path = "images/body_horizontal.png";
                case BODY_TOP_LEFT -> path = "images/body_topleft.png";
                case BODY_TOP_RIGHT -> path = "images/body_topright.png";
                case BODY_VERTICAL -> path = "images/body_vertical.png";

                case HEAD_UP -> path = "images/head_up.png";
                case HEAD_LEFT -> path = "images/head_left.png";
                case HEAD_RIGHT -> path = "images/head_right.png";
                case HEAD_DOWN -> path = "images/head_down.png";

                case TAIL_DOWN -> path = "images/tail_down.png";
                case TAIL_LEFT -> path = "images/tail_left.png";
                case TAIL_RIGHT -> path = "images/tail_right.png";
                case TAIL_UP -> path = "images/tail_up.png";
                default -> path = "PATH FAILED!";
            }

            BufferedImage image = ImageLoader.readImage(path);
            g.drawImage(image , x, y, SnakeUIConstants.BLOCK_SIZE, SnakeUIConstants.BLOCK_SIZE, null);

        } catch (IOException ex) {
            System.err.println("Failed to set body image! __ " + ex.getMessage());

        }
    }

    /**
     * Az alap játéktér megrajzolása
     * A scoreboard megrajzolása
     */
    private void drawUI(Graphics2D graphics2D) {
        graphics2D.setColor(SnakeUIConstants.GRID_COLOR);
        graphics2D.fill(SnakeUIConstants.GRID_RECTANGLE);

        graphics2D.setStroke(SnakeUIConstants.LARGE_STROKE);
        graphics2D.setColor(SnakeUIConstants.GRID_BORDER_COLOR);
        graphics2D.draw(SnakeUIConstants.GRID_RECTANGLE);

        drawScoreBoard(graphics2D);
    }

    /**
     * A scoreboard megrajzolásáért felel.
     * Az első 10 eredményt rajzolja ki névvel ha az nagyobb volt mint 10.
     * A jelenlegi pontszámunkat is megadja.
     */
    private void drawScoreBoard(Graphics2D graphics2D) {
        ScoreBoard scoreBoard = snakeLogic.getScoreBoard();
        int x = SnakeUIConstants.SCORE_POS_X;
        int y = SnakeUIConstants.SCORE_POS_Y;
        graphics2D.setColor(SnakeUIConstants.TEXT_COLOR);
        graphics2D.setFont(SnakeUIConstants.SMALL_FONT);


        graphics2D.drawString("HIGH SCORES", x, y);

        int i = 0;

        for(; i < 10; ++i) {
            int score = scoreBoard.getTopScore()[i];

            if(score <= 0) break;

            String user = scoreBoard.getTopPlayer()[i];
            graphics2D.drawString(String.format("%d:    %d points:    %s",i + 1, score, user), x, y + 30 * (i + 1));
        }
        graphics2D.drawString(String.format("Your score:      %d points", scoreBoard.getScore()), x, y + (i + 3) * 30);
        graphics2D.drawString(String.format("Your time:      %d s", (int)clock / 1000), x, y + (i + 4) * 30);
    }

    /**
     * A kígyó összes szegmensének megrajzolásáért felel.
     * Végigiterál a snake részein és meghívja a szükséges drawSnake függvényt.
     */
    private void drawSnake(Graphics2D g) {
        int headX = snakeLogic.getHeadX();
        int headY = snakeLogic.getHeadY();

        int tailX = snakeLogic.getTailX();
        int tailY = snakeLogic.getTailY();

        drawSnakePart(g ,snakeLogic.getHeadType() ,headX ,headY);
        drawSnakePart(g ,snakeLogic.getTailType() ,tailX , tailY);

        for(int i = 0; i < snakeLogic.getSize(); ++i) {
            BodySegment part = snakeLogic.getBodySegmentById(i);
            drawSnakePart(g, part.getType(), part.getX(), part.getY());
        }
    }

    /**
     * A egy fal elemet rajzol ki az images mappában megadott kép segítségével.
     * @param r row ahova rajzolja
     * @param c column ahova rajzolja
     */
    private void drawWallPart(Graphics2D g, int r, int c) {
        int x = SnakeUIConstants.LEFT_MARGIN + c * SnakeUIConstants.BLOCK_SIZE;
        int y = SnakeUIConstants.TOP_MARGIN + r * SnakeUIConstants.BLOCK_SIZE;

        try {

            String path = "images/wall.png";

            BufferedImage image = ImageLoader.readImage(path);
            g.drawImage(image , x, y, SnakeUIConstants.BLOCK_SIZE, SnakeUIConstants.BLOCK_SIZE, null);

        } catch (IOException ex) {
            System.err.println("Failed to set body image! __ " + ex.getMessage());

        }
    }

    /**
     * Kirajzolja a gyümolcsöt a képernyőre.
     */
    private void drawFruit(Graphics2D g) {
        Fruit fruit = snakeLogic.getFruit();
        int x = SnakeUIConstants.LEFT_MARGIN + fruit.getY() * SnakeUIConstants.BLOCK_SIZE;
        int y = SnakeUIConstants.TOP_MARGIN + fruit.getX() * SnakeUIConstants.BLOCK_SIZE;

        try {
            String path = "images/apple.png";

            BufferedImage image = ImageLoader.readImage(path);
            g.drawImage(image , x, y, SnakeUIConstants.BLOCK_SIZE, SnakeUIConstants.BLOCK_SIZE, null);

        } catch (IOException ex) {
            System.err.println("Failed to set body image! __ " + ex.getMessage());

        }
    }

    /**
     * A falak láncolt listáján végigiterálva meghívja a drawWall metódust a megfelelő paraméterekkel
     */
    private void drawWalls(Graphics2D g) {
        for(Wall i : snakeLogic.getWalls()) {
            drawWallPart(g, i.getX(), i.getY());
        }
    }

    /**
     * Kirajzolja az összes játékbeli elemet:
     * Snake
     * Walls
     * Fruit
     */
    private void drawAll(Graphics2D graphics2D) {
        drawSnake(graphics2D);
        drawWalls(graphics2D);
        drawFruit(graphics2D);
    }

    /**
     * A paintComponent-et származtatja.
     * A játék állapotától függően meghívja a rajzoló metódusokat.
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        drawUI(graphics2D);

        ScoreBoard scoreBoard = snakeLogic.getScoreBoard();
        if (scoreBoard.isGameOver()) {
            if(gameOverCounter <= 0) {
                if(!savedScore) {
                    snakeLogic.saveScoreIfGreaterThan0();
                    scoreBoard.setTopScore();
                    savedScore = true;
                }
                drawStartScreen(graphics2D);
            } else {
                --gameOverCounter;
                drawAll(graphics2D);
            }
        } else {
            drawAll(graphics2D);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * A játékban lévő léptetést hajtja végre.
     * A korábban megkapott legutolsó snake irányt használva abba az irányba meghívja
     * a snake move metódusát.
     * Újrafesti a képernyőt.
     */
    private final Action oneGameCycleAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ScoreBoard scoreBoard = snakeLogic.getScoreBoard();
            if (!scoreBoard.isGameOver()) {
                switch (snakeLogic.getDirection()) {
                    case DOWN -> snakeLogic.move(Direction.DOWN);
                    case UP -> snakeLogic.move(Direction.UP);
                    case LEFT -> snakeLogic.move(Direction.LEFT);
                    case RIGHT -> snakeLogic.move(Direction.RIGHT);
                }
                canTurn = true;
                clock += SnakeRules.TIMER;
            }
            repaint();

        }
    };

}
