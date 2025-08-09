package ru.avdonin.console.games.chess.panes;

import lombok.Getter;
import ru.avdonin.console.games.chess.pieces.Piece;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Constants;
import ru.avdonin.console.games.chess.util.HistoryRecord;
import ru.avdonin.console.games.chess.util.NumberCell;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Getter
public class HistoryPanel extends JPanel {
    private final JPanel history;
    private final List<HistoryRecord> records;

    public HistoryPanel() {
        history = new JPanel();
        records = new LinkedList<>();
        initUI();
    }

    private void initUI() {
        history.setLayout(new BoxLayout(history, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(history);
        setLayout(new BorderLayout());

        add(scrollPane);
        setPreferredSize(new Dimension(Constants.CELL_SIZE * 2, Constants.CELL_SIZE * 2));
        setBackground(Color.GRAY);
    }

    public void add(Piece piece, NumberCell fromCell, NumberCell toCell) {
        if (Objects.equals(fromCell, toCell)) return;
        HistoryRecord historyRecord = new HistoryRecord(piece, fromCell, toCell);
        records.add(historyRecord);

        String pieceName = getPieceName(piece);
        String fromCellName = Cell.getColLetter(fromCell.col()) + "" + fromCell.row();
        String toCellName = Cell.getColLetter(toCell.col()) + "" + toCell.row();

        String record = pieceName + (piece.isWhite() ? "W" : "B") + ": " + fromCellName + " > " + toCellName;

        history.add(new JLabel(record));
        history.revalidate();
        history.repaint();
    }

    public HistoryRecord getLastRecord() {
        if (records.isEmpty()) return null;
        return records.getLast();
    }

    public void clear() {
        records.clear();
        history.removeAll();

        history.revalidate();
        history.repaint();
    }

    private String getPieceName(Piece piece) {
        String className = piece.getClass().getName();
        int index = className.lastIndexOf('.') + 1;
        return className.substring(index, index + 2);
    }
}
