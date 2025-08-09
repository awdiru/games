package ru.avdonin.console.games.chess.pieces.impl;

import ru.avdonin.console.games.chess.panes.ChessPanel;
import ru.avdonin.console.games.chess.pieces.Piece;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.HistoryRecord;
import ru.avdonin.console.games.chess.util.NumberCell;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Pawn extends Piece {
    private NumberCell takingAisle = null;

    public Pawn(ChessPanel parent, boolean isWhite, Cell cell) {
        super(parent, isWhite, 1, cell);
    }

    @Override
    public void move(Cell newCell) {
        super.move(newCell);
        if (Objects.equals(takingAisle, newCell.getNumberCell())) {
            Cell cell = parent.getCells().get(new NumberCell(takingAisle.row() + (isWhite ? -1 : 1), takingAisle.col()));
            cell.setPiece(null);
        }
        takingAisle = null;
    }

    @Override
    public Set<NumberCell> calculatePossibleMoves() {
        Set<NumberCell> possibleMoves = new HashSet<>();
        int startRow = isWhite ? 2 : 7;
        int direction = isWhite ? 1 : -1;

        NumberCell current = cell.getNumberCell();
        NumberCell desired = new NumberCell(current.row() + direction, current.col());

        if (notExistPiece(desired)) {
            possibleMoves.add(desired);
            desired = new NumberCell(startRow + direction * 2, current.col());
            if (current.row() == startRow && notExistPiece(desired))
                possibleMoves.add(desired);
        }

        int[] cols = {current.col() - 1, current.col() + 1};
        for (int col : cols) {
            desired = new NumberCell(current.row() + direction, col);
            if (existEnemy(desired))
                possibleMoves.add(desired);
        }
        addTakingAisle(possibleMoves);
        return possibleMoves;
    }

    private void addTakingAisle(Set<NumberCell> possibleMoves) {
        HistoryRecord record = history.getLastRecord();
        if (record == null) return;
        if (!(record.piece() instanceof Pawn)) return;

        NumberCell fromCell = record.fromCell();
        NumberCell toCell = record.toCell();
        Piece piece = record.piece();

        boolean firstMove = fromCell.row() == (piece.isWhite() ? 2 : 7);
        boolean neighborCol = cell.getNumberCell().col() == fromCell.col() - 1
                || cell.getNumberCell().col() == fromCell.col() + 1;
        boolean requiredRow = cell.getNumberCell().row() == (isWhite ? 5 : 4);
        boolean requiredRowEnemy = toCell.row() == (piece.isWhite() ? 4 : 5);

        if (firstMove && neighborCol && requiredRow && requiredRowEnemy) {
            takingAisle = new NumberCell(toCell.row() + (isWhite ? 1 : -1), toCell.col());
            possibleMoves.add(takingAisle);
        }
    }

}
