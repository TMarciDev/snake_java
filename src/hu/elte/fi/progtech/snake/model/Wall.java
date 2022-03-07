package hu.elte.fi.progtech.snake.model;

public class Wall {
    private final int x;
    private final int y;

    Wall(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
