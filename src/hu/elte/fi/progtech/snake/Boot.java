package hu.elte.fi.progtech.snake;

import hu.elte.fi.progtech.snake.gui.SnakeFrame;
import hu.elte.fi.progtech.snake.model.SnakeLogic;

import javax.swing.*;

public class Boot {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeFrame(new SnakeLogic()).setVisible(true));
    }

}
