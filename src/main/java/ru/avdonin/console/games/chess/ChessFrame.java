package ru.avdonin.console.games.chess;

import javax.swing.*;

public class ChessFrame extends JFrame {
    public ChessFrame() {
        setTitle("Шахматы");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        ChessPanel gamePanel = new ChessPanel();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
