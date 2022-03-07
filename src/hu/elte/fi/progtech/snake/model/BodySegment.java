package hu.elte.fi.progtech.snake.model;

public class BodySegment {
    private final int x;
    private final int y;

    private final SnakeType type;

    BodySegment(int x, int y, SnakeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public SnakeType getType() {
        return type;
    }
}
