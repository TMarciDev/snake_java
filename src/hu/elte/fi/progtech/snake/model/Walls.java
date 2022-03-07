package hu.elte.fi.progtech.snake.model;

import java.util.LinkedList;

public class Walls {

    Walls() {}

    /**
     * Az alábbi metódusok létrehozzák a kívánt falak listáját.
     * @return egy walls lácolt listával.
     */
    public LinkedList<Wall> initDots() {
        LinkedList<Wall> walls;
        walls = new LinkedList<>();

        walls.add(new Wall(2, 4));
        walls.add(new Wall(6, 4));
        walls.add(new Wall(10, 4));
        walls.add(new Wall(14, 4));

        walls.add(new Wall(3, 8));
        walls.add(new Wall(8, 4));
        walls.add(new Wall(9, 7));
        walls.add(new Wall(13, 8));

        walls.add(new Wall(8, 10));
        walls.add(new Wall(1, 5));
        walls.add(new Wall(2, 2));
        walls.add(new Wall(12, 10));

        return walls;
    }

    public LinkedList<Wall> initBasic() {
        LinkedList<Wall> walls;
        walls = new LinkedList<>();

        walls.add(new Wall(2, 4));
        walls.add(new Wall(2, 5));

        return walls;
    }

    public LinkedList<Wall> initGreatWall() {
        LinkedList<Wall> walls;
        walls = new LinkedList<>();


        for(int i = 2; i < 15; ++i) {
            walls.add(new Wall(i, 5));
            walls.add(new Wall(i, 7));
        }

        return walls;
    }
}
