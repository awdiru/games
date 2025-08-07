package ru.avdonin.console.games.chess.util;

import lombok.Getter;

import javax.swing.*;

import ru.avdonin.console.games.chess.ChessPanel;
import ru.avdonin.console.games.chess.util.coords.NumberCell;
import ru.avdonin.console.games.chess.util.pieces.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

@Getter
public class Cell extends JPanel {
    private Piece piece;
    private final NumberCell numberCell;
    private final ChessPanel parent;
    private boolean selectedPiece = false;
    private boolean selectedCell = false;

    private boolean whiteCheck = false;
    private boolean blackCheck = false;

    private final Set<Piece> whiteCheckPieces = new HashSet<>();
    private final Set<Piece> blackCheckPieces = new HashSet<>();

    public Cell(NumberCell numberCell, ChessPanel parent) {
        this.numberCell = numberCell;
        this.parent = parent;
        setLayout(new BorderLayout());
        setBackground(getCellColor());
        setPreferredSize(new Dimension(Constants.CELL_SIZE, Constants.CELL_SIZE));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                actionEvent();
            }
        });
    }

    private Color getCellColor() {
        if ((numberCell.row() + numberCell.col()) % 2 == 0)
            return Color.WHITE;
        else return new Color(241, 159, 107);
    }

    private void actionEvent() {
        Piece selectedPiece = parent.getSelectedPiece();
        Set<NumberCell> possibleMoves = parent.getCurrentPossibleMoves();

        if (selectedPiece == null && piece != null) {
            if (piece.isWhite != parent.isWhitesMove()) return;

            parent.setSelectedPiece(piece);
            piece.addAllowedCells(possibleMoves);

            selectedCells(possibleMoves);
            setSelectedPiece(true);

        } else if (selectedPiece != null) {
            parent.setSelectedPiece(null);
            selectedPiece.getCell().setSelectedPiece(false);
            selectedPiece.move(possibleMoves, numberCell);

            selectedCells(possibleMoves);
            possibleMoves.clear();
        }
    }

    private void selectedCells(Set<NumberCell> possibleMoves) {
        for (NumberCell numCell : possibleMoves) {
            Cell cell = parent.getCells().get(numCell);
            if (cell != null)
                cell.setSelectedCell(!cell.selectedCell);
        }
    }

    public void setPiece(Piece piece) {
        removeAll();
        this.piece = piece;
        if (piece != null) {
            add(this.piece, BorderLayout.CENTER);
            turningPawn();
        }
        revalidate();
        repaint();
    }

    private void turningPawn() {
        if (piece instanceof Pawn && ((numberCell.row() == 8 && piece.isWhite)
                || (numberCell.row() == 1 && !piece.isWhite))) {
            getContextMenuTurningPawn();
        }
    }

    private void getContextMenuTurningPawn() {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem queen = new JMenuItem("Ферзь");
        JMenuItem rook = new JMenuItem("Ладья");
        JMenuItem knight = new JMenuItem("Конь");
        JMenuItem bishop = new JMenuItem("Слон");

        queen.addActionListener(e -> turningPawn(Queen.class));
        rook.addActionListener(e -> turningPawn(Rook.class));
        knight.addActionListener(e -> turningPawn(Knight.class));
        bishop.addActionListener(e -> turningPawn(Bishop.class));

        menu.add(queen);
        menu.add(rook);
        menu.add(knight);
        menu.add(bishop);

        menu.show(this, getWidth() / 2, getHeight() / 2);
    }

    private void turningPawn(Class<? extends Piece> aClass) {
        try {
            Piece newPiece = aClass.getConstructor(ChessPanel.class, Cell.class, boolean.class)
                    .newInstance(piece.getParent(), this, piece.isWhite());
            setPiece(newPiece);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSelectedPiece(boolean selectedPiece) {
        this.selectedPiece = selectedPiece;
        repaint();
    }

    public void setSelectedCell(boolean selectedCell) {
        this.selectedCell = selectedCell;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (selectedPiece) {
            g.setColor(new Color(100, 255, 100, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (selectedCell) {
            g.setColor(new Color(0, 51, 255, 134));
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            g.fillOval(centerX - 5, centerY - 5, 10, 10);
        }
    }
}