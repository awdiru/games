package ru.avdonin.console.games.snake;

import ru.avdonin.console.util.Game;

import javax.swing.*;

public class SnakeFrame extends JFrame implements Game {
    public SnakeFrame() {
    }

    @Override
    public void start() {
        setTitle("Змейка");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        SnakePanel gamePanel = new SnakePanel();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
