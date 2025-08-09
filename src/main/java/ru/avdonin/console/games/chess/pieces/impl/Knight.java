package ru.avdonin.console.games.chess.pieces.impl;

import lombok.Getter;
import ru.avdonin.console.games.chess.panes.ChessPanel;
import ru.avdonin.console.games.chess.pieces.Piece;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.NumberCell;

import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {
    public Knight(ChessPanel parent, boolean isWhite, Cell cell) {
        super(parent, isWhite, 3, cell);
    }

    @Override
    public Set<NumberCell> calculatePossibleMoves() {
        Set<NumberCell> possibleMoves = new HashSet<>();
        NumberCell current = cell.getNumberCell();
        for (KnightDirection direction : KnightDirection.values()) {
            NumberCell next = new NumberCell(current.row() + direction.rowOff, current.col() + direction.colOff);
            if (existEnemy(next))
                possibleMoves.add(next);
            if (notExistPiece(next))
                possibleMoves.add(next);
        }
        return possibleMoves;
    }


    @Getter
    private enum KnightDirection {
        UP1_LEFT2(1, -2),
        UP2_LEFT1(2, -1),
        UP2_RIGHT1(2, 1),
        UP1_RIGHT2(1, 2),
        DOWN1_RIGHT2(-1, 2),
        DOWN2_RIGHT1(-2, 1),
        DOWN2_LEFT1(-2, -1),
        DOWN1_LEFT2(-1, -2);

        private final int rowOff;
        private final int colOff;

        KnightDirection(int rowOff, int colOff) {
            this.rowOff = rowOff;
            this.colOff = colOff;
        }
    }
}
