package ru.avdonin.console.games.chess.panels;

import lombok.Getter;
import ru.avdonin.console.games.chess.pieces.Piece;
import ru.avdonin.console.games.chess.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Getter
public class HistoryPanel extends JPanel {
    private final JPanel moveMarker;
    private final JPanel history;
    private final List<MoveRecord> records;
    private final List<HistoryRecord> historyRecords;
    private int moveCount = 0;

    public HistoryPanel() {
        history = new JPanel();
        moveMarker = new JPanel();
        records = new LinkedList<>();
        historyRecords = new LinkedList<>();
        initUI();
    }

    private void initUI() {
        history.setLayout(new BoxLayout(history, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(history);
        moveMarker.setBackground(Cell.WHITE);
        moveMarker.setPreferredSize(new Dimension((int) (Constants.CELL_SIZE * 2.6), Constants.CELL_SIZE));

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(moveMarker, BorderLayout.SOUTH);
        setPreferredSize(new Dimension((int) (Constants.CELL_SIZE * 2.5), Constants.CELL_SIZE * 2));
        setBackground(Color.GRAY);
    }

    public void add(Piece piece, NumberCell fromCell, NumberCell toCell) {
        if (Objects.equals(fromCell, toCell)) return;
        moveCount++;

        MoveRecord moveRecord = new MoveRecord(piece, fromCell, toCell);
        records.add(moveRecord);

        HistoryRecord historyRecord;
        if (moveCount % 2 == 1) {
            historyRecord = new HistoryRecord(moveRecord, null);
            historyRecords.add(historyRecord);
        } else {
            historyRecord = historyRecords.getLast();
            historyRecord.setMoveRecordB(moveRecord);

            String recordW = getMoveRecord(
                    historyRecord.getMoveRecordW().piece(),
                    historyRecord.getMoveRecordW().fromCell(),
                    historyRecord.getMoveRecordW().toCell()
            );

            String recordB = getMoveRecord(
                    historyRecord.getMoveRecordB().piece(),
                    historyRecord.getMoveRecordB().fromCell(),
                    historyRecord.getMoveRecordB().toCell()
            );


            String htmlRecord = "<html><table><tr>" +
                    "<td width='100'>" + moveCount / 2 + ") " + recordW + "</td>" +
                    "<td width='100'>" + recordB + "</td>" +
                    "</tr></table></html>";

            history.add(new JLabel(htmlRecord));
            history.revalidate();
            history.repaint();
        }
    }

    private String getMoveRecord(Piece piece, NumberCell fromCell, NumberCell toCell) {
        String pieceName = getPieceName(piece);
        String fromCellName = Cell.getColLetter(fromCell.col()) + "" + fromCell.row();
        String toCellName = Cell.getColLetter(toCell.col()) + "" + toCell.row();
        return pieceName + ": " + fromCellName + " > " + toCellName;
    }

    public MoveRecord getLastRecord() {
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

    public void setMoveMarker() {
        if (moveCount % 2 == 1)
           moveMarker.setBackground(new Color(117, 114, 114));
        else moveMarker.setBackground(new Color(255, 174, 64));
        moveMarker.revalidate();
        moveMarker.repaint();
    }
}
