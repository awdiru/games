package ru.avdonin.console.games.chess.util.pieces;

import ru.avdonin.console.games.chess.ChessPanel;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Piece;
import ru.avdonin.console.games.chess.util.coords.NumberCell;

import java.util.Set;

public class Knight extends Piece {
    public Knight(ChessPanel parent, Cell cell, boolean isWhite) {
        super(parent, cell, isWhite, "knight.png");
    }

    @Override
    public void addAllowedCells(Set<NumberCell> allowedCells) {
        NumberCell current = this.cell.getNumberCell();
        int[] rows = {2, 1, -1, -2, -2, -1, 1, 2};
        int[] cols = {1, 2, 2, 1, -1, -2, -2, -1};

        for (int i = 0; i < 8; i++) {
            NumberCell desired = new NumberCell(current.row() + rows[i], current.col() + cols[i]);
            if (desired.col() > 8 || desired.row() > 8
                    || desired.col() < 0 || desired.row() < 0)
                return;
            if (existEnemy(desired) || notExistPiece(desired))
                allowedCells.add(desired);
        }
        removeCheckMove(allowedCells);
    }
}
