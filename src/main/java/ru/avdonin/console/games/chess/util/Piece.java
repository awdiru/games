package ru.avdonin.console.games.chess.util;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.console.games.chess.ChessPanel;
import ru.avdonin.console.games.chess.util.coords.NumberCell;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

@Getter
@Setter
public abstract class Piece extends JPanel {
    protected final ChessPanel parent;
    protected Cell cell;
    protected final boolean isWhite;
    protected final String iconName;
    private final JLabel pieceIcon;

    protected Piece(ChessPanel parent, Cell cell, boolean isWhite, String iconName) {
        this.parent = parent;
        this.cell = cell;
        this.isWhite = isWhite;
        this.iconName = "icons/chess/pieces/" + (isWhite ? "white/" : "black/") + iconName;
        this.pieceIcon = new JLabel(new ImageIcon(this.iconName));

        setLayout(new BorderLayout());
        add(pieceIcon, BorderLayout.CENTER);
        setOpaque(false);
    }

    public void move(Set<NumberCell> allowedCells, NumberCell numberCell) {
        if (!allowedCells.contains(numberCell)) return;
        cell.setPiece(null);
        cell = parent.getCells().get(numberCell);
        cell.setPiece(this);
        parent.setWhitesMove(!parent.isWhitesMove());
    }

    public abstract void addAllowedCells(Set<NumberCell> allowedCells);

    public void addAttackedCells(Set<NumberCell> attackedCells) {
        addAllowedCells(attackedCells);
    }

    protected void addAllowedCells(Set<NumberCell> allowedCells, NumberCell desired, Direction direction) {
        if (existEnemy(desired))
            allowedCells.add(desired);

        if (notExistPiece(desired)) {
            allowedCells.add(desired);
            addAllowedCells(allowedCells,
                    new NumberCell(desired.row() + direction.getRowOff(), desired.col() + direction.getColOff()),
                    direction);
        }
    }

    protected void removeCheckMove(Set<NumberCell> allowedCells) {
        for (NumberCell cellNum : allowedCells) {
            if (parent.isMoveCausingCheck(this, cellNum)) {
                allowedCells.remove(cellNum);
            }
        }
    }

    protected boolean existEnemy(NumberCell numberCell) {
        Cell cell = parent.getCells().get(numberCell);
        if (cell == null) return false;
        if (cell.getPiece() == null) return false;
        return cell.getPiece().isWhite() != isWhite;
    }

    protected boolean notExistPiece(NumberCell numberCell) {
        Cell cell = parent.getCells().get(numberCell);
        if (cell == null) return false;
        return cell.getPiece() == null;
    }
}
