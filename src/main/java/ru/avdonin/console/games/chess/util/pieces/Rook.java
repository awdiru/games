package ru.avdonin.console.games.chess.util.pieces;

import ru.avdonin.console.games.chess.ChessPanel;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Direction;
import ru.avdonin.console.games.chess.util.Piece;
import ru.avdonin.console.games.chess.util.coords.NumberCell;

import java.util.Set;

public class Rook extends Piece {
    public Rook(ChessPanel parent, Cell cell, boolean isWhite) {
        super(parent, cell, isWhite, "rook.png");
    }

    @Override
    public void addAllowedCells(Set<NumberCell> allowedCells) {
        NumberCell current = this.cell.getNumberCell();
        addAllowedCells(allowedCells, current, Direction.UP);
        addAllowedCells(allowedCells, current, Direction.DOWN);
        addAllowedCells(allowedCells, current, Direction.RIGHT);
        addAllowedCells(allowedCells, current, Direction.LEFT);
        removeCheckMove(allowedCells);
    }
}
