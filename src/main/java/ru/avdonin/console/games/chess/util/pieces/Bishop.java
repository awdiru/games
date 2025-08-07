package ru.avdonin.console.games.chess.util.pieces;

import ru.avdonin.console.games.chess.ChessPanel;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Direction;
import ru.avdonin.console.games.chess.util.Piece;
import ru.avdonin.console.games.chess.util.coords.NumberCell;

import java.util.Set;

public class Bishop extends Piece {
    public Bishop(ChessPanel parent, Cell cell, boolean isWhite) {
        super(parent, cell, isWhite, "bishop.png");
    }

    @Override
    public void addAllowedCells(Set<NumberCell> allowedCells) {
        NumberCell current = this.cell.getNumberCell();
        addAllowedCells(allowedCells, current, Direction.LEFT_UP);
        addAllowedCells(allowedCells, current, Direction.LEFT_DOWN);
        addAllowedCells(allowedCells, current, Direction.RIGHT_UP);
        addAllowedCells(allowedCells, current, Direction.RIGHT_DOWN);
        removeCheckMove(allowedCells);
    }
}
