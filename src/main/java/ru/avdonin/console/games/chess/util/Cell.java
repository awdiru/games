package ru.avdonin.console.games.chess.util;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.console.games.chess.panels.ChessPanel;
import ru.avdonin.console.games.chess.pieces.Piece;
import ru.avdonin.console.games.chess.pieces.impl.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Getter
public class Cell extends JPanel {
    public static final Color WHITE = Color.WHITE;
    public static final Color BLACK = new Color(142, 144, 255);

    private final NumberCell numberCell;
    private final ChessPanel parent;

    private Piece piece;
    @Setter
    private boolean selected = false;
    @Setter
    private boolean allowedMove = false;

    public Cell(ChessPanel parent, NumberCell numberCell) {
        this.numberCell = numberCell;
        this.parent = parent;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Constants.CELL_SIZE, Constants.CELL_SIZE));
        setBackground(getColor());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                actionEvent();
            }
        });
    }

    private Color getColor() {
        return (numberCell.col() + numberCell.row()) % 2 == 0
                ? BLACK : WHITE;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        removeAll();
        if (this.piece != null)
            add(this.piece);
        repaint();
    }

    public void setPieceNotRepaint(Piece piece) {
        this.piece = piece;
    }

    private void actionEvent() {
        if (parent.isPatWhite() || parent.isPatBlack()) return;

        Piece selectedPiece = parent.getSelectedPiece();
        if (selectedPiece == null && piece != null) {
            parent.setSelectedPiece(this.getPiece());
            selected = true;
            setAllowedMoves(piece);

        } else if (selectedPiece != null) {
            selectedPiece.getCell().setSelected(false);
            selectedPiece.getCell().repaint();
            setAllowedMoves(selectedPiece);
            selectedPiece.move(this);
            parent.setSelectedPiece(null);
        }
        checkTurningPawn();
        repaint();
    }

    private void checkTurningPawn() {
        if (piece == null) return;
        if (!(piece instanceof Pawn)) return;

        boolean isWhiteTurning = piece.isWhite() && numberCell.row() == 8;
        boolean isBlackTurning = !piece.isWhite() && numberCell.row() == 1;

        if (isWhiteTurning || isBlackTurning)
            showContextMenuTurningPawn();
    }

    private void showContextMenuTurningPawn() {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem queenItem = new JMenuItem("Ферзь");
        JMenuItem rookItem = new JMenuItem("Ладья");
        JMenuItem knightItem = new JMenuItem("Конь");
        JMenuItem bishopItem = new JMenuItem("Слон");

        queenItem.addActionListener(e ->
                replacePawnWith(new Queen(piece.getParent(), piece.isWhite(), this)));
        rookItem.addActionListener(e ->
                replacePawnWith(new Rook(piece.getParent(), piece.isWhite(), this)));
        knightItem.addActionListener(e ->
                replacePawnWith(new Knight(piece.getParent(), piece.isWhite(), this)));
        bishopItem.addActionListener(e ->
                replacePawnWith(new Bishop(piece.getParent(), piece.isWhite(), this)));

        contextMenu.add(queenItem);
        contextMenu.add(rookItem);
        contextMenu.add(knightItem);
        contextMenu.add(bishopItem);

        contextMenu.show(this, 0, 0);
    }

    private void replacePawnWith(Piece newPiece) {
        setPiece(newPiece);
        parent.calculatePossibleMoves();
        parent.checkingCheck();
        parent.checkingCheckmate();
        revalidate();
        repaint();
        parent.getParent().getMaterialPanel().addCount(piece.getValue() - 1, !piece.isWhite());
    }


    private void setAllowedMoves(Piece piece) {
        for (NumberCell cellNum : piece.getPossibleMoves()) {
            Cell cell = parent.getCells().get(cellNum);
            cell.setAllowedMove(!cell.allowedMove);
            cell.repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        addCoordinates(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (selected) {
            g.setColor(new Color(74, 207, 22, 140));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(getColor());
            g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
        }
        if (isCheck()) {
            g.setColor(new Color(253, 0, 0, 169));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(getColor());
            g.fillRect(10, 10, getWidth() - 20, getHeight() - 20);
        }
    }

    private boolean isCheck() {
        if (piece == null) return false;
        boolean isWhiteKing = piece.equals(parent.getWhiteKing());
        boolean isBlackKing = piece.equals(parent.getBlackKing());

        boolean isWhiteCheck = parent.isWhiteCheck();
        boolean isBlackCheck = parent.isBlackCheck();

        return (isWhiteKing && isWhiteCheck) || (isBlackKing && isBlackCheck);
    }

    @Override
    protected void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (allowedMove) {
            g.setColor(new Color(255, 0, 231, 194));
            g.fillOval(getWidth() / 2 - 5, getHeight() / 2 - 5, 15, 15);
        }

    }

    private void addCoordinates(Graphics g) {
        g.setFont(new Font("SansSerif", Font.PLAIN, Constants.CELL_SIZE / 5));

        if (numberCell.col() == 1) {
            String rowLabel = String.valueOf(numberCell.row());
            g.setColor(getTextColor());
            int x = Constants.CELL_SIZE / 12;
            int y = Constants.CELL_SIZE / 4;
            g.drawString(rowLabel, x, y);
        }

        if (numberCell.row() == 1) {
            String colLabel = String.valueOf(getColLetter(numberCell.col()));
            g.setColor(getTextColor());
            int textWidth = g.getFontMetrics().stringWidth(colLabel);
            int x = Constants.CELL_SIZE - textWidth + 10;
            int y = Constants.CELL_SIZE + 10;
            g.drawString(colLabel, x, y);
        }
    }

    private Color getTextColor() {
        return getBackground().equals(WHITE) ? BLACK : WHITE;
    }

    public static char getColLetter(int col) {
        return switch (col) {
            case 1 -> 'a';
            case 2 -> 'b';
            case 3 -> 'c';
            case 4 -> 'd';
            case 5 -> 'e';
            case 6 -> 'f';
            case 7 -> 'g';
            case 8 -> 'h';
            default -> throw new IllegalStateException("Unexpected value: " + col);
        };
    }

}