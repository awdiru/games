package ru.avdonin.console.games.chess.panels;


import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    private final MainPanel parent;

    public MenuPanel(MainPanel parent) {
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setLayout(new FlowLayout());
        add(getRestartButton());
        add(getSettingsButton());
    }

    private JButton getRestartButton() {
        JButton button = new JButton("Перезапуск");
        button.addActionListener(e -> parent.restart());
        button.setAlignmentX(CENTER_ALIGNMENT);
        return button;
    }

    private JButton getSettingsButton() {
        JButton button = new JButton("Настройки");
        button.addActionListener(e -> showSettingsContextMenu(button));
        return button;
    }

    private void showSettingsContextMenu(Component parent) {
        ChessPanel chessPanel = this.parent.getChessPanel();
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem freeMoves = new JMenuItem("Свободная очередь хода: " + chessPanel.isFreeMoves());
        freeMoves.addActionListener(e -> chessPanel.setFreeMoves(!chessPanel.isFreeMoves()));
        popupMenu.add(freeMoves);

        JMenuItem freeAllowedMoves = new JMenuItem("Свободное перемещение по полю: " + chessPanel.isFreeAllowedMoves());
        freeAllowedMoves.addActionListener(e -> chessPanel.setFreeAllowedMoves(!chessPanel.isFreeAllowedMoves()));
        popupMenu.add(freeAllowedMoves);

        popupMenu.show(parent, parent.getWidth() / 2, parent.getHeight() / 2);
    }
}