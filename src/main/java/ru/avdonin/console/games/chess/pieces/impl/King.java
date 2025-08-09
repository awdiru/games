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
public class King extends Piece {
    private boolean hasMoved = false;

    public King(ChessPanel parent, boolean isWhite, Cell cell) {
        super(parent, isWhite, 0, cell);
    }

    @Override
    public void move(Cell newCell) {
        if (!parent.isFreeMoves() && parent.isWhiteMove() != isWhite) return;
        if (!parent.isFreeAllowedMoves() && !possibleMoves.contains(newCell.getNumberCell())) return;

        Cell oldCell = getCell();
        int currentCol = cell.getNumberCell().col();
        int newCol = newCell.getNumberCell().col();

        if (Math.abs(newCol - currentCol) == 2) {
            int direction = (newCol > currentCol) ? 1 : -1;
            int rookCol = (direction == 1) ? 8 : 1;
            int rookNewCol = newCol - direction;

            Cell rookCell = parent.getCells().get(new NumberCell(cell.getNumberCell().row(), rookCol));
            Cell rookNewCell = parent.getCells().get(new NumberCell(cell.getNumberCell().row(), rookNewCol));

            Rook rook = (Rook) rookCell.getPiece();
            rookCell.setPiece(null);
            rook.setCell(rookNewCell);
            rookNewCell.setPiece(rook);

            rook.setHasMoved(true);
        }

        super.move(newCell);
        Cell moveCell = getCell();

        if (!oldCell.equals(moveCell)) hasMoved = true;
    }

    @Override
    public Set<NumberCell> calculatePossibleMoves() {
        Set<NumberCell> possibleMoves = new HashSet<>();
        for (Direction direction : Direction.values())
            possibleMoves.addAll(calculatePossibleMoves(direction));
        return possibleMoves;
    }

    @Override
    protected Set<NumberCell> calculatePossibleMoves(Direction direction) {
        Set<NumberCell> possibleMoves = new HashSet<>();
        NumberCell current = cell.getNumberCell();
        NumberCell next = new NumberCell(current.row() + direction.getRowOff(), current.col() + direction.getColOff());

        Cell cell = parent.getCells().get(next);
        if (cell != null) {
            if (existEnemy(next))
                possibleMoves.add(next);
            if (notExistPiece(next))
                possibleMoves.add(next);
        }

        if (!hasMoved && (isWhite && !parent.isWhiteCheck()) || (!isWhite && !parent.isBlackCheck())) {
            checkCastling(possibleMoves, 1);
            checkCastling(possibleMoves, -1);
        }
        return possibleMoves;
    }

    private void checkCastling(Set<NumberCell> possibleMoves, int direction) {
        int row = cell.getNumberCell().row();
        int kingCol = cell.getNumberCell().col();
        int rookCol = (direction == 1) ? 8 : 1;

        Cell rookCell = parent.getCells().get(new NumberCell(row, rookCol));
        if (!(rookCell.getPiece() instanceof Rook rook) || rook.isHasMoved()) return;

        for (int col = kingCol + direction; col != rookCol; col += direction) {
            if (parent.getCells().get(new NumberCell(row, col)).getPiece() != null) return;
        }

        Set<NumberCell> attacked = isWhite ? parent.getBlackAttacked() : parent.getWhiteAttacked();
        if (!attacked.contains(new NumberCell(row, kingCol)) &&
                !attacked.contains(new NumberCell(row, kingCol + direction)) &&
                !attacked.contains(new NumberCell(row, kingCol + 2 * direction))) {

            possibleMoves.add(new NumberCell(row, kingCol + 2 * direction));
        }
    }
}
