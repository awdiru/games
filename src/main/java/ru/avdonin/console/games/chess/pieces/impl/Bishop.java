package ru.avdonin.console.games.chess.pieces.impl;

import ru.avdonin.console.games.chess.panes.ChessPanel;
import ru.avdonin.console.games.chess.pieces.Piece;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Direction;
import ru.avdonin.console.games.chess.util.NumberCell;

import java.util.HashSet;
import java.util.Set;

public class Bishop extends Piece {
    public Bishop(ChessPanel parent, boolean isWhite, Cell cell) {
        super(parent, isWhite, 3, cell);
    }

    @Override
    public Set<NumberCell> calculatePossibleMoves() {
        Set<NumberCell> possibleMoves = new HashSet<>();
        possibleMoves.addAll(calculatePossibleMoves(Direction.RIGHT_UP));
        possibleMoves.addAll(calculatePossibleMoves(Direction.RIGHT_DOWN));
        possibleMoves.addAll(calculatePossibleMoves(Direction.LEFT_UP));
        possibleMoves.addAll(calculatePossibleMoves(Direction.LEFT_DOWN));
        return possibleMoves;
    }
}
