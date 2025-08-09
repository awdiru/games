package ru.avdonin.console.games.chess.panes;

import ru.avdonin.console.games.chess.pieces.Piece;
import ru.avdonin.console.games.chess.util.Constants;

import javax.swing.*;
import java.awt.*;

public class MaterialPanel extends JPanel {
    private final JPanel whitePanel = new JPanel();
    private final JPanel blackPanel = new JPanel();
    private final JPanel whiteMaterialPanel = new JPanel();
    private final JPanel blackMaterialPanel = new JPanel();
    private final JPanel whiteMaterialCountPanel = new JPanel();
    private final JPanel blackMaterialCountPanel = new JPanel();
    private int materialCount = 0;

    public MaterialPanel() {
        initUI();
    }

    private void initUI() {
        setPreferredSize(new Dimension(Constants.CELL_SIZE * 2, Constants.CELL_SIZE * 10));
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                whitePanel,
                blackPanel
        );
        add(splitPane);

        initPanel(whitePanel, whiteMaterialCountPanel, whiteMaterialPanel);
        initPanel(blackPanel, blackMaterialCountPanel, blackMaterialPanel);
    }

    public void addCount(Piece piece) {
        if (piece == null) return;
        if (piece.isWhite()) {
            whiteMaterialPanel.add(new JLabel(piece.getScaledImage()));
            whiteMaterialPanel.revalidate();
            whiteMaterialPanel.repaint();
            materialCount += piece.getValue();
        } else {
            blackMaterialPanel.add(new JLabel(piece.getScaledImage()));
            blackMaterialPanel.revalidate();
            blackMaterialPanel.repaint();
            materialCount -= piece.getValue();
        }
        updateMaterialCount();
    }

    public void addCount(int count, boolean isWhite) {
        if (isWhite) materialCount += count;
        else materialCount -= count;
        updateMaterialCount();
    }

    private void updateMaterialCount() {
        blackMaterialCountPanel.removeAll();
        whiteMaterialCountPanel.removeAll();

        if (materialCount > 0) whiteMaterialCountPanel.add(new JLabel("Перевес: +" + materialCount));
        else if (materialCount < 0) blackMaterialCountPanel.add(new JLabel("Перевес: +" + (-materialCount)));

        blackMaterialCountPanel.revalidate();
        whiteMaterialCountPanel.revalidate();
        blackMaterialCountPanel.repaint();
        whiteMaterialCountPanel.repaint();
    }

    private void initPanel(JPanel panel, JPanel countPanel, JPanel materialPanel) {
        countPanel.setPreferredSize(new Dimension(Constants.CELL_SIZE * 2, Constants.CELL_SIZE));
        materialPanel.setPreferredSize(new Dimension(Constants.CELL_SIZE * 2, Constants.CELL_SIZE * 4));
        materialPanel.setLayout(new FlowLayout());

        panel.setLayout(new BorderLayout());
        panel.add(countPanel, BorderLayout.NORTH);
        panel.add(materialPanel, BorderLayout.CENTER);
    }

    public void clear() {
        whiteMaterialPanel.removeAll();
        blackMaterialPanel.removeAll();
        materialCount = 0;

        whiteMaterialPanel.revalidate();
        blackMaterialPanel.revalidate();
        whiteMaterialPanel.repaint();
        blackMaterialPanel.repaint();
        updateMaterialCount();
    }
}