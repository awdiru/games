package ru.avdonin.console.games.chess.pieces;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.console.games.chess.panels.ChessPanel;
import ru.avdonin.console.games.chess.panels.HistoryPanel;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Constants;
import ru.avdonin.console.games.chess.util.Direction;
import ru.avdonin.console.games.chess.util.NumberCell;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public abstract class Piece extends JPanel {
    protected final ChessPanel parent;
    protected final HistoryPanel history;
    protected final Set<NumberCell> possibleMoves = new HashSet<>();
    protected final boolean isWhite;
    protected final int value;
    protected Cell cell;

    public Piece(ChessPanel parent, boolean isWhite, int value, Cell cell) {
        this.parent = parent;
        this.isWhite = isWhite;
        this.value = value;
        this.cell = cell;

        this.history = parent.getParent().getHistoryPanel();

        setLayout(new BorderLayout());
        setOpaque(false);
        add(new JLabel(new ImageIcon(getIconPath())), BorderLayout.CENTER);
    }

    public void move(Cell newCell) {
        if (!parent.isFreeMoves() && parent.isWhiteMove() != isWhite) return;
        if (!parent.isFreeAllowedMoves() && !possibleMoves.contains(newCell.getNumberCell())) return;

        Cell oldCell = cell;
        parent.getParent().getHistoryPanel().add(this, oldCell.getNumberCell(), newCell.getNumberCell());

        if (newCell.getPiece() != null && newCell.getPiece().isWhite != isWhite)
            parent.getParent().getMaterialPanel().addCount(newCell.getPiece());

        oldCell.setPiece(null);
        cell = newCell;
        newCell.setPiece(this);

        parent.calculatePossibleMoves();
        parent.setWhiteMove(!isWhite);
        parent.checkingCheck();
        parent.checkingCheckmate();
    }

    public void calculateMoves() {
        possibleMoves.clear();
        possibleMoves.addAll(calculatePossibleMoves());
        removeCheckMoves();
    }

    public ImageIcon getScaledImage() {
        ImageIcon icon = new ImageIcon(getIconPath());
        int height = (int) (icon.getIconHeight() / (Constants.CELL_SIZE / 60.0));
        int width = (int) (icon.getIconWidth() * ((double) height / icon.getIconHeight()));
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public abstract Set<NumberCell> calculatePossibleMoves();

    protected Set<NumberCell> calculatePossibleMoves(Direction direction) {
        Set<NumberCell> possibleMoves = new HashSet<>();
        NumberCell next = cell.getNumberCell();
        while (true) {
            next = new NumberCell(next.row() + direction.getRowOff(), next.col() + direction.getColOff());

            Cell cell = parent.getCells().get(next);
            if (cell == null) break;
            if (existFriend(next)) break;

            if (existEnemy(next)) {
                possibleMoves.add(next);
                break;
            }
            if (notExistPiece(next)) {
                possibleMoves.add(next);
            }
        }
        return possibleMoves;
    }

    protected void removeCheckMoves() {
        Set<NumberCell> movedSet = new HashSet<>(possibleMoves);
        for (NumberCell cellNum : movedSet) {
            if (parent.isNotSaveMove(this, cellNum))
                possibleMoves.remove(cellNum);
        }
    }

    protected boolean existEnemy(NumberCell cellNum) {
        Cell cell = parent.getCells().get(cellNum);
        if (cell == null) return false;
        if (cell.getPiece() == null) return false;
        return cell.getPiece().isWhite() != isWhite;
    }

    protected boolean existFriend(NumberCell cellNum) {
        Cell cell = parent.getCells().get(cellNum);
        if (cell == null) return false;
        if (cell.getPiece() == null) return false;
        return cell.getPiece().isWhite() == isWhite;
    }

    protected boolean notExistPiece(NumberCell cellNum) {
        Cell cell = parent.getCells().get(cellNum);
        if (cell == null) return false;
        return cell.getPiece() == null;
    }

    private String getIconPath() {
        String className = this.getClass().getName();
        String pieceName = className.substring(className.lastIndexOf('.') + 1);
        return "icons/chess/pieces/" + (isWhite ? "white/" : "black/") + pieceName + ".png";
    }
}
