package ru.avdonin.console.games.chess.pieces.impl;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.console.games.chess.panes.ChessPanel;
import ru.avdonin.console.games.chess.pieces.Piece;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Direction;
import ru.avdonin.console.games.chess.util.NumberCell;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Rook extends Piece {
    private boolean hasMoved = false;

    public Rook(ChessPanel parent, boolean isWhite, Cell cell) {
        super(parent, isWhite, 5, cell);
    }

    @Override
    public void move(Cell newCell) {
        Cell oldCell = getCell();
        super.move(newCell);
        Cell moveCell = getCell();
        if (!oldCell.equals(moveCell)) hasMoved = true;
    }

    @Override
    public Set<NumberCell> calculatePossibleMoves() {
        Set<NumberCell> possibleMoves = new HashSet<>();
        possibleMoves.addAll(calculatePossibleMoves(Direction.RIGHT));
        possibleMoves.addAll(calculatePossibleMoves(Direction.LEFT));
        possibleMoves.addAll(calculatePossibleMoves(Direction.UP));
        possibleMoves.addAll(calculatePossibleMoves(Direction.DOWN));
        return possibleMoves;
    }
}
