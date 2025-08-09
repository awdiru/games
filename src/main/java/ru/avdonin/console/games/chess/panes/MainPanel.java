package ru.avdonin.console.games.chess.panes;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class MainPanel extends JPanel {
    private final ChessPanel chessPanel;
    private final HistoryPanel historyPanel;
    private final MenuPanel menuPanel;
    private final MaterialPanel materialPanel;

    public MainPanel() {
        historyPanel = new HistoryPanel();
        materialPanel = new MaterialPanel();
        chessPanel = new ChessPanel(this);
        menuPanel = new MenuPanel(this);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        add(chessPanel, BorderLayout.CENTER);
        add(materialPanel, BorderLayout.WEST);
        add(historyPanel, BorderLayout.EAST);
        add(menuPanel, BorderLayout.NORTH);

        setBackground(Color.GRAY);
    }

    public void restart() {
        if (historyPanel != null)
            historyPanel.clear();
        if (chessPanel != null)
            materialPanel.clear();
        if (chessPanel != null)
            chessPanel.clear();

        revalidate();
        repaint();
    }
}
