package ru.avdonin.console.games.chess.util.pieces;

import ru.avdonin.console.games.chess.ChessPanel;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Piece;
import ru.avdonin.console.games.chess.util.coords.NumberCell;

import java.util.Set;

public class Pawn extends Piece {

    public Pawn(ChessPanel parent, Cell cell, boolean isWhite) {
        super(parent, cell, isWhite, "pawn.png");
    }

    @Override
    public void addAllowedCells(Set<NumberCell> allowedCells) {
        int direction = isWhite ? 1 : -1;
        int startRow = isWhite ? 2 : 7;
        NumberCell current = this.cell.getNumberCell();

        NumberCell desired = new NumberCell(current.row() + direction, current.col());
        if (notExistPiece(desired)) {
            allowedCells.add(desired);
            desired = new NumberCell(direction * 2 + startRow, current.col());
            if (current.row() == startRow && notExistPiece(desired))
                allowedCells.add(desired);
        }

        int[] cols = {current.col() - 1, current.col() + 1};
        for (int col : cols) {
            desired = new NumberCell(current.row() + direction, col);
            if(existEnemy(desired))
                allowedCells.add(desired);
        }
        removeCheckMove(allowedCells);
    }

    @Override
    public void addAttackedCells(Set<NumberCell> attackedCells) {
        int direction = isWhite ? 1 : -1;
        NumberCell current = cell.getNumberCell();
        int[] cols = {current.col() - 1, current.col() + 1};

        for (int col : cols) {
            NumberCell desired = new NumberCell(current.row() + direction, col);
            attackedCells.add(desired);
        }
    }
}
