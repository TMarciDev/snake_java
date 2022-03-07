package hu.elte.fi.progtech.snake.model;

import hu.elte.fi.progtech.snake.model.score.ScoreBoard;
import hu.elte.fi.progtech.snake.persistence.dao.HighScoreDao;
import hu.elte.fi.progtech.snake.persistence.entity.HighScore;

import java.sql.SQLException;
import java.util.LinkedList;


public final class SnakeLogic {
    private int row;
    private int column;

    private GridType[][] grids;

    private ScoreBoard scoreBoard;

    private final HighScoreDao highScoreDao;
    private String userName;

    private Direction direction;

    private BodyParts bodyParts;

    private LinkedList<Wall> walls;

    private Fruit fruit;

    public SnakeLogic() {
        this.highScoreDao = new HighScoreDao();
    }


    /**
     * új játék létrehozásának logikája
     * Alaphelyzetbe állítja az értékeket, és feltölti a játéktér mátrixot az alap értékekkel
     * @param row a játéktér row-ja
     * @param column a játéktér column-ja
     * @param userName a felugró ablakban megadott játékosnév
     */
    public void newGame(int row, int column, String userName){
        this.row = row;
        this.column = column;
        this.userName = userName;

        grids = new GridType[row][column];
        initGrid();

        initScoreBoard();

        int headX = GameConstants.NORMAL_ROW_COUNT / 2;
        int headY = GameConstants.NORMAL_COL_COUNT / 2;

        int[] startPartsX = new int[]{headX, headX + 1};
        int[] startPartsY = new int[]{headY, headY};

        bodyParts = new BodyParts(startPartsX, startPartsY);

        grids[headX][headY].setType(SegmentType.SNAKE);

        grids[headX + 1][headY].setType(SegmentType.SNAKE);

        scoreBoard.reset(userName);

        direction = Direction.UP;

        for(Wall i : walls) {
            grids[i.getX()][i.getY()].setType(SegmentType.WALL);
        }

        fruit = new Fruit();
        fruit.createNextFruit(grids);
    }

    /**
     * Az új játék indításánál megadott map segítségével meghívja a megfelelő map létrehozó metódust.
     * @param map a kapott játéktér név
     */
    public void initWall(String map) {
        switch (map) {
            case "Basic" -> walls = new Walls().initBasic();
            case "Dots" -> walls = new Walls().initDots();
            default -> walls = new Walls().initGreatWall();
        }
    }

    /**
     * A scoreboard értékeinek lekérdezése és beállítása a scoreboard segítségével.
     */
    private void initScoreBoard() {
        scoreBoard = new ScoreBoard();
        try {
            int[] topScore = highScoreDao.getTopScore();
            String[] topPlayer =  highScoreDao.getTopPlayer();
            scoreBoard.setTopScore(topScore, topPlayer);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Elmenti az adatbázisba a pontszámunkat ha nagyobb mint 0
     */
    public void saveScoreIfGreaterThan0() {
        int scoreValue = scoreBoard.getScore();
        if(scoreValue > 0) {
            try {
                HighScore score = new HighScore();
                score.setName(userName);
                score.setScore(scoreValue);
                highScoreDao.add(score);
            } catch (SQLException ex) {
                throw new IllegalStateException("Failed to save high_score to database!", ex);
            }
        }
    }

    public BodySegment getBodySegmentById(int i) {
        return bodyParts.getBodySegmentById(i);
    }

    public int getSize() {
        return bodyParts.getSize();
    }


    /**
     * Ellenőrzi, hogy a kígyó felye illegális elembe ütközött e
     * @param x vizsgálandó x hely
     * @param y vizsgálandó y érték
     * @return halott e
     */
    private boolean isDead(int x, int y) {
        if(x < 0 || y <=0 || x >= row - 1 || y >= column - 1) {
            scoreBoard.setGameOver();
            return true;
        }
        SegmentType type = grids[x][y].getSegment();
        if(type == SegmentType.SNAKE || type == SegmentType.WALL) {
            scoreBoard.setGameOver();
            return true;
        }
        return false;
    }

    /**
     * Megvizsgálja, hogy a kapott koordináták a gyümölcs koordinátáival egyenlők e.
     * @return gyümölcsön van e
     */
    private boolean isOnFruit(int x, int y) {
        if(fruit.getX() == x && fruit.getY() == y) {

            scoreBoard.addScore(10);
            fruit.createNextFruit(grids);

            return true;
        }
        return false;
    }

    /**
     * A megfelelő irányba áthelyezi a kígyó felyét
     * Meghívja az isDead és isOnFruit függvényt
     * @param direction az irány amibe mozdítja a felyet
     */
    public void move(Direction direction) {
        this.direction = direction;

        grids[bodyParts.getTailX()][bodyParts.getTailY()].setType(SegmentType.EMPTY);

        bodyParts.moveDirection(direction);

        int x = bodyParts.getHeadX();
        int y = bodyParts.getHeadY();

        if(isOnFruit(x, y)) {
            grids[bodyParts.getTailX()][bodyParts.getTailY()].setType(SegmentType.SNAKE);
            bodyParts.moveHead(true);
        } else {
            bodyParts.moveHead(false);

        }

        if(!isDead(x, y)) grids[x][y].setType(SegmentType.SNAKE);
    }

    /**
     * Az alap négyzetháló mátrix inicializálása
     */
    private void initGrid() {
        for (int i = 0; i < row; ++i) {
            for (int j = 0; j < column; ++j) {
                grids[i][j] = new GridType();
            }
        }
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public int getHeadX() {
        return bodyParts.getHeadX();
    }

    public int getHeadY() {
        return bodyParts.getHeadY();
    }

    public SnakeType getHeadType() {
        return bodyParts.getHeadType();
    }

    public SnakeType getTailType() {
        return bodyParts.getTailType();
    }

    public int getTailX() {
        return bodyParts.getTailX();
    }

    public int getTailY() {
        return bodyParts.getTailY();
    }

    public LinkedList<Wall> getWalls() {
        return walls;
    }

    public Fruit getFruit() {
        return fruit;
    }
}
