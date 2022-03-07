package hu.elte.fi.progtech.snake.model;


import java.util.Random;

public class Fruit {
    private int x;
    private int y;

    Fruit() {}

    /**
     * Létrehozza a következő gyümölcsöt.
     * @param plane ellenőrzésével hoz létre, hogy csak üres részre helyezzen gyümölcsöt.
     */
    public void createNextFruit(GridType[][] plane) {
        int randMinX = 1;
        int randMinY = 1;
        int randMaxX = GameConstants.NORMAL_ROW_COUNT - 2;
        int randMaxY = GameConstants.NORMAL_COL_COUNT - 2;
        boolean found = false;
        Random rand = new Random();

        int randX, randY;

        while(!found) {
            randX = rand.nextInt(randMaxX) + randMinX;
            randY = rand.nextInt(randMaxY) + randMinY;
            if(randX == x && randY == y) continue;
            if(plane[randX][randY].getSegment() == SegmentType.EMPTY) {
                found = true;
                x = randX;
                y = randY;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
