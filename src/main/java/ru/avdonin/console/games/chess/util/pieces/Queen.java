package ru.avdonin.console.games.chess.util.pieces;

import ru.avdonin.console.games.chess.ChessPanel;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Direction;
import ru.avdonin.console.games.chess.util.Piece;
import ru.avdonin.console.games.chess.util.coords.NumberCell;

import java.util.Set;

public class Queen extends Piece {
    public Queen(ChessPanel parent, Cell cell, boolean isWhite) {
        super(parent, cell, isWhite, "queen.png");
    }

    @Override
    public void addAllowedCells(Set<NumberCell> allowedCells) {
        NumberCell current = this.cell.getNumberCell();
        for (Direction direction : Direction.values())
            addAllowedCells(allowedCells, current, direction);

        removeCheckMove(allowedCells);
    }
}
