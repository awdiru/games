package ru.avdonin.console.games.chess;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Constants;
import ru.avdonin.console.games.chess.util.Piece;
import ru.avdonin.console.games.chess.util.coords.Column;
import ru.avdonin.console.games.chess.util.coords.Empty;
import ru.avdonin.console.games.chess.util.coords.NumberCell;
import ru.avdonin.console.games.chess.util.coords.Row;
import ru.avdonin.console.games.chess.util.pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

@Getter
@Setter
public class ChessPanel extends JPanel {
    private final Map<NumberCell, Cell> cells = new HashMap<>();
    private final Set<NumberCell> currentPossibleMoves = new HashSet<>();
    private boolean whitesMove = true;

    private King whiteKing;
    private King blackKing;

    private Piece selectedPiece;

    public ChessPanel() {
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(Constants.CELL_SIZE * 8 + 80, Constants.CELL_SIZE * 8 + 80));
        setBackground(Color.DARK_GRAY);
        generateBoard();
        generatePieces();
    }

    public boolean isCheck(boolean isWhite) {
        King king = isWhite ? whiteKing : blackKing;
        NumberCell kingCell = king.getCell().getNumberCell();
        Set<NumberCell> attacked = new HashSet<>();

        for (Cell cell : cells.values()) {
            Piece piece = cell.getPiece();
            if (piece != null && piece.isWhite() != isWhite) {
                piece.addAttackedCells(attacked);
                if (attacked.contains(kingCell)) return true;
            }
        }
        return false;
    }

    public boolean isCheckmate(boolean isWhite) {
        if (!isCheck(isWhite)) return false;
        Set<NumberCell> allowedMoves = new HashSet<>();

        for (Cell cell : cells.values()) {
            Piece piece = cell.getPiece();
            if (piece != null && piece.isWhite() == isWhite)
                piece.addAllowedCells(allowedMoves);
        }
        return allowedMoves.isEmpty();
    }

    public boolean isMoveCausingCheck(Piece piece, NumberCell destination) {
        Cell fromCell = piece.getCell();
        Cell toCell = cells.get(destination);
        Piece originalToPiece = toCell.getPiece();

        fromCell.setPiece(null);
        toCell.setPiece(piece);
        piece.setCell(toCell);

        boolean isCheck = isCheck(piece.isWhite());

        fromCell.setPiece(piece);
        toCell.setPiece(originalToPiece);
        piece.setCell(fromCell);

        return isCheck;
    }

    private void generateBoard() {
        List<Character> chars = List.of('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');
        for (int row = 8; row >= 1; row--) {
            add(new Row(row));
            for (int col = 1; col <= 8; col++) {
                NumberCell numberCell = new NumberCell(row, col);
                Cell cell = new Cell(numberCell, this);
                cells.put(numberCell, cell);
                add(cell);
            }
        }
        add(new Empty());
        for (Character col : chars) {
            add(new Column(col));
        }
    }

    private void generatePieces() {
        generatePawns();
        generateRooks();
        generateKnights();
        generateBishops();
        generateQueens();
        generateKings();
    }

    private void generatePawns() {
        for (int col = 1; col <= 8; col++) {
            Cell white = cells.get(new NumberCell(2, col));
            Cell black = cells.get(new NumberCell(7, col));

            white.setPiece(new Pawn(this, white, true));
            black.setPiece(new Pawn(this, black, false));
        }
    }

    private void generateRooks() {
        for (int col = 1; col <= 8; col += 7) {
            Cell white = cells.get(new NumberCell(1, col));
            Cell black = cells.get(new NumberCell(8, col));

            white.setPiece(new Rook(this, white, true));
            black.setPiece(new Rook(this, black, false));
        }
    }

    private void generateKnights() {
        for (int col = 2; col <= 7; col += 5) {
            Cell white = cells.get(new NumberCell(1, col));
            Cell black = cells.get(new NumberCell(8, col));

            white.setPiece(new Knight(this, white, true));
            black.setPiece(new Knight(this, black, false));
        }
    }

    private void generateBishops() {
        for (int col = 3; col <= 6; col +=3) {
            Cell white = cells.get(new NumberCell(1, col));
            Cell black = cells.get(new NumberCell(8, col));

            white.setPiece(new Bishop(this, white, true));
            black.setPiece(new Bishop(this, black, false));
        }
    }

    private void generateQueens() {
        Cell white = cells.get(new NumberCell(1, 5));
        Cell black = cells.get(new NumberCell(8, 5));

        white.setPiece(new Queen(this, white, true));
        black.setPiece(new Queen(this, black, false));
    }

    private void generateKings() {
        Cell white = cells.get(new NumberCell(1, 4));
        Cell black = cells.get(new NumberCell(8, 4));

        whiteKing = new King(this, white, true);
        blackKing = new King(this, black, false);

        white.setPiece(whiteKing);
        black.setPiece(blackKing);
    }
}
