package ru.avdonin.console;

import ru.avdonin.console.util.Games;

import javax.swing.*;
import java.awt.*;

public class GameConsolePanel extends JPanel {
    private final JFrame parent;
    public GameConsolePanel(JFrame parent) {
        this.parent = parent;
        initGames();
    }

    private void initGames() {
        setLayout(new BorderLayout());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

        for (Games game : Games.values()) {
            JButton gameButton = new JButton();
            gameButton.setPreferredSize(new Dimension(200, 30));
            gameButton.setText(game.getName());
            gameButton.addActionListener(e -> {
                game.getGameClass().start();
                parent.dispose();
            });
            buttonsPanel.add(gameButton);
        }
        add(buttonsPanel, BorderLayout.CENTER);
    }
}
