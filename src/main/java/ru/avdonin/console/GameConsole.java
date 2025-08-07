package ru.avdonin.console;

import javax.swing.*;

public class GameConsole extends JFrame {
    public GameConsole() {
        setTitle("Игры");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        GameConsolePanel gamePanel = new GameConsolePanel(this);
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
