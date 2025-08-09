package ru.avdonin.console.games.chess.pieces.impl;

import ru.avdonin.console.games.chess.panes.ChessPanel;
import ru.avdonin.console.games.chess.pieces.Piece;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Direction;
import ru.avdonin.console.games.chess.util.NumberCell;

import java.util.HashSet;
import java.util.Set;

public class Queen extends Piece {
    public Queen(ChessPanel parent, boolean isWhite, Cell cell) {
        super(parent, isWhite, 9, cell);
    }

    @Override
    public Set<NumberCell> calculatePossibleMoves() {
        Set<NumberCell> possibleMoves = new HashSet<>();
        for (Direction direction : Direction.values())
            possibleMoves.addAll(calculatePossibleMoves(direction));
        return possibleMoves;
    }

}
