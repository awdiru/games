package ru.avdonin.console.games.chess;

import ru.avdonin.console.games.chess.panes.MainPanel;
import ru.avdonin.console.util.Game;

import javax.swing.*;

public class ChessFrame extends JFrame implements Game {
    public ChessFrame() {
    }

    @Override
    public void start() {
        setTitle("Шахматы");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        MainPanel gamePanel = new MainPanel();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
