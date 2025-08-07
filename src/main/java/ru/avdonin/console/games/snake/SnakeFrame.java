package ru.avdonin.console.games.snake;

import javax.swing.*;

public class SnakeFrame extends JFrame {
    public SnakeFrame() {
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
