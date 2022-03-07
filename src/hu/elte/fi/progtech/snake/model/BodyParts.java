package hu.elte.fi.progtech.snake.model;

import java.util.LinkedList;

public class BodyParts {
    private int headX;
    private int headY;
    SnakeType headType;

    private int tailX;
    private int tailY;
    SnakeType tailType;

    private int oldHeadX;
    private int oldHeadY;

    private final LinkedList<BodySegment> body;

    /**
     * Alapértelmezett helyzetbe állítja a kígyót.
     * @param startPartsX a kapott fej és farok x koordinátái
     * @param startPartsY a kapott fej és farok y koordinátái
     */
    BodyParts(int[] startPartsX, int[] startPartsY) {

        headType = SnakeType.HEAD_UP;
        tailType = SnakeType.TAIL_DOWN;

        body = new LinkedList<>();

        headX = startPartsX[0];
        headY = startPartsY[0];

        tailX = startPartsX[1];
        tailY = startPartsY[1];
    }

    /**
     * Beállítja a kígyóban, melyik irányba szeretne elmozdulni, és a
     * metódus beállítja a saját headX és headY koordinátáit a megfelelő irányba.
     * @param direction a kapott irány
     */
    public void moveDirection(Direction direction) {
        oldHeadX = headX;
        oldHeadY = headY;

        headX += direction.getY();
        headY += direction.getX();

        switch (direction) {
            case DOWN -> headType = SnakeType.HEAD_DOWN;
            case UP -> headType = SnakeType.HEAD_UP;
            case LEFT -> headType = SnakeType.HEAD_LEFT;
            case RIGHT -> headType = SnakeType.HEAD_RIGHT;
        }
    }

    /**
     * Fizikailag elmozdítja a kígyot a megfelelő irányba.
     * Létrehozunk egy új testrészt és ha kell eltöröljük az utolsót és a helyére a farkat rakjuk.
     * @param isEaten ha éppen evett a kígyó akkor nem megy előrébb a farka.
     */
    public void moveHead(boolean isEaten) {

        if(isEaten || body.size() > 0) createPart(oldHeadX, oldHeadY);
        if(!isEaten) moveTail();

    }

    /**
     * A farkat a kígyó testének utolsó elemének helyére rakjuk
     * És kiszámoljuk majd beállítjuk merre álljon.
     */
    private void moveTail() {
        if(body.size() == 0) {
            tailX = oldHeadX;
            tailY = oldHeadY;
            switch (headType) {
                case HEAD_UP -> tailType = SnakeType.TAIL_DOWN;
                case HEAD_DOWN -> tailType = SnakeType.TAIL_UP;
                case HEAD_LEFT -> tailType = SnakeType.TAIL_RIGHT;
                case HEAD_RIGHT -> tailType = SnakeType.TAIL_LEFT;
            }
        } else {
            BodySegment part = body.removeLast();

            tailX = part.getX();
            tailY = part.getY();

            int lastX;
            int lastY;

            lastX = body.getLast().getX();
            lastY = body.getLast().getY();

            SnakeType typeToChange;

            if(lastX == tailX && lastY > tailY) typeToChange = SnakeType.TAIL_LEFT;
            else if(lastX == tailX && lastY < tailY) typeToChange = SnakeType.TAIL_RIGHT;
            else if(lastX > tailX && lastY == tailY) typeToChange = SnakeType.TAIL_UP;
            else {
                typeToChange = SnakeType.TAIL_DOWN;
            }
            tailType = typeToChange;
        }
    }

    /**
     * Minden mozdulat után létrejön egy testrész a fej után.
     * Kiszámoljuk, hogy milyen az új testrész orientációja majd beállítjuk.
     * @param x az új testrész x koordinátája
     * @param y az új testrész y koordinátája
     */
    public void createPart(int x, int y) {

        int bodyX;
        int bodyY;
        if(body.size() == 0) {
            bodyX = tailX;
            bodyY = tailY;
        }
        else {
            bodyX = body.getFirst().getX();
            bodyY = body.getFirst().getY();
        }

        SnakeType typeToAdd;

        switch (headType) {
            case HEAD_RIGHT -> {
                if(bodyX == headX) {
                    typeToAdd = SnakeType.BODY_HORIZONTAL;
                } else if(bodyX < headX) {
                    typeToAdd = SnakeType.BODY_TOP_RIGHT;
                } else {
                    typeToAdd = SnakeType.BODY_BOTTOM_RIGHT;
                }
            }
            case HEAD_LEFT -> {
                if(bodyX == headX) {
                    typeToAdd = SnakeType.BODY_HORIZONTAL;
                } else if(bodyX > headX) {
                    typeToAdd = SnakeType.BODY_BOTTOM_LEFT;
                } else {
                    typeToAdd = SnakeType.BODY_TOP_LEFT;
                }
            }
            case HEAD_UP-> {
                if(bodyY == headY) {
                    typeToAdd = SnakeType.BODY_VERTICAL;
                } else if(bodyY < headY) {
                    typeToAdd = SnakeType.BODY_TOP_LEFT;
                } else {
                    typeToAdd = SnakeType.BODY_TOP_RIGHT;
                }
            }
            default -> { // head down
                if(bodyY == headY) {
                    typeToAdd = SnakeType.BODY_VERTICAL;
                } else if(bodyY > headY) {
                    typeToAdd = SnakeType.BODY_BOTTOM_RIGHT;
                } else {
                    typeToAdd = SnakeType.BODY_BOTTOM_LEFT;
                }
            }
        }
        body.add(0, new BodySegment(x, y, typeToAdd));
    }

    public int getSize() {
        return body.size();
    }

    public int getHeadX() {
        return headX;
    }

    public int getHeadY() {
        return headY;
    }

    public SnakeType getHeadType(){
        return headType;
    }

    public int getTailX() {
        return tailX;
    }

    public int getTailY() {
        return tailY;
    }

    public SnakeType getTailType() {
        return tailType;
    }

    public BodySegment getBodySegmentById(int i) {
        return body.get(i);
    }
}

