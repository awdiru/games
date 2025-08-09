package ru.avdonin.console.games.chess.panels;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.console.games.chess.pieces.Piece;
import ru.avdonin.console.games.chess.pieces.impl.*;
import ru.avdonin.console.games.chess.util.Cell;
import ru.avdonin.console.games.chess.util.Constants;
import ru.avdonin.console.games.chess.util.NumberCell;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class ChessPanel extends JPanel {
    private final MainPanel parent;

    private final Map<NumberCell, Cell> cells = new HashMap<>();
    private final Set<NumberCell> whiteAttacked = new HashSet<>();
    private final Set<NumberCell> blackAttacked = new HashSet<>();

    private King whiteKing;
    private King blackKing;

    private boolean whiteCheck = false;
    private boolean blackCheck = false;

    private boolean patWhite = false;
    private boolean patBlack = false;

    private boolean whiteCastling = true;
    private boolean blackCastling = true;

    private boolean isWhiteMove = true;

    private Piece selectedPiece;

    private boolean freeMoves = false;
    private boolean freeAllowedMoves = false;


    public ChessPanel(MainPanel parent) {
        this.parent = parent;
        initUI();
        start();
    }

    private void initUI() {
        setLayout(new GridLayout(8, 8));
        setPreferredSize(new Dimension(Constants.CELL_SIZE * 10, Constants.CELL_SIZE * 8));
        setBackground(Color.GRAY);
    }

    private void start() {
        isWhiteMove = true;
        patWhite = false;
        patBlack = false;
        freeMoves = false;
        freeAllowedMoves = false;

        generateBoard();
        generatePieces();
        calculatePossibleMoves();

        revalidate();
        repaint();
    }


    public void calculatePossibleMoves() {
        whiteAttacked.clear();
        blackAttacked.clear();

        for (Cell cell : cells.values()) {
            if (cell.getPiece() == null) continue;
            Piece piece = cell.getPiece();
            piece.calculateMoves();
            if (piece.isWhite()) whiteAttacked.addAll(piece.getPossibleMoves());
            else blackAttacked.addAll(piece.getPossibleMoves());
        }

        checkingCheck();
    }

    public void checkingCheck() {
        whiteCheck = checkingCheck(blackAttacked, whiteKing);
        blackCheck = checkingCheck(whiteAttacked, blackKing);
        whiteKing.getCell().repaint();
        blackKing.getCell().repaint();
    }

    public void checkingCheckmate() {
        patWhite = whiteAttacked.isEmpty();
        patBlack = blackAttacked.isEmpty();
        if (patWhite || patBlack) repaint();
    }

    public boolean isNotSaveMove(Piece piece, NumberCell cell) {
        if (freeMoves) return false;
        Cell fromCell = piece.getCell();
        Cell toCell = cells.get(cell);

        fromCell.setPieceNotRepaint(null);
        Piece oldPiece = toCell.getPiece();
        toCell.setPieceNotRepaint(piece);
        piece.setCell(toCell);

        if (piece instanceof King && Math.abs(piece.getCell().getNumberCell().col() - cell.col()) == 2) {
            int direction = (cell.col() > piece.getCell().getNumberCell().col()) ? 1 : -1;
            NumberCell kingStepCell = new NumberCell(cell.row(), piece.getCell().getNumberCell().col() + direction);

            Set<NumberCell> enemyMoves = calculateEnemyMoves(piece.isWhite());
            return enemyMoves.contains(kingStepCell) || enemyMoves.contains(cell);
        }
        King king = piece.isWhite() ? whiteKing : blackKing;

        Set<NumberCell> enemyMoves = calculateEnemyMoves(piece.isWhite());
        boolean notSavedMove = enemyMoves.contains(king.getCell().getNumberCell());

        fromCell.setPieceNotRepaint(piece);
        toCell.setPieceNotRepaint(oldPiece);
        piece.setCell(fromCell);

        return notSavedMove;
    }

    private boolean checkingCheck(Set<NumberCell> enemyAttacked, King king) {
        NumberCell cellKing = king.getCell().getNumberCell();
        return enemyAttacked.contains(cellKing);
    }

    private Set<NumberCell> calculateEnemyMoves(boolean isWhite) {
        Set<NumberCell> enemyAttacked = new HashSet<>();
        for (Cell cell : cells.values()) {
            Piece piece = cell.getPiece();
            if (piece == null || piece.isWhite() == isWhite) {
                continue;
            }
            enemyAttacked.addAll(piece.calculatePossibleMoves());
        }
        return enemyAttacked;
    }

    private void generateBoard() {
        for (int row = 8; row >= 1; row--) {
            for (int col = 1; col <= 8; col++) {
                NumberCell cellNum = new NumberCell(row, col);
                Cell cell = new Cell(this, cellNum);
                cells.put(cellNum, cell);
                add(cell);
            }
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
            Cell blackCell = cells.get(new NumberCell(7, col));
            Cell whiteCell = cells.get(new NumberCell(2, col));

            Pawn blackPawn = new Pawn(this, false, blackCell);
            Pawn whitePawn = new Pawn(this, true, whiteCell);

            blackCell.setPiece(blackPawn);
            whiteCell.setPiece(whitePawn);
        }
    }

    private void generateRooks() {
        for (int col = 1; col <= 8; col += 7) {
            Cell blackCell = cells.get(new NumberCell(8, col));
            Cell whiteCell = cells.get(new NumberCell(1, col));

            Rook blackRook = new Rook(this, false, blackCell);
            Rook whiteRook = new Rook(this, true, whiteCell);

            blackCell.setPiece(blackRook);
            whiteCell.setPiece(whiteRook);
        }
    }

    private void generateKnights() {
        for (int col = 2; col <= 7; col += 5) {
            Cell blackCell = cells.get(new NumberCell(8, col));
            Cell whiteCell = cells.get(new NumberCell(1, col));

            Knight blackKnight = new Knight(this, false, blackCell);
            Knight whiteKnight = new Knight(this, true, whiteCell);

            blackCell.setPiece(blackKnight);
            whiteCell.setPiece(whiteKnight);
        }
    }

    private void generateBishops() {
        for (int col = 3; col <= 6; col += 3) {
            Cell blackCell = cells.get(new NumberCell(8, col));
            Cell whiteCell = cells.get(new NumberCell(1, col));

            Bishop blackBishop = new Bishop(this, false, blackCell);
            Bishop whiteBishop = new Bishop(this, true, whiteCell);

            blackCell.setPiece(blackBishop);
            whiteCell.setPiece(whiteBishop);
        }
    }

    private void generateQueens() {
        Cell blackCell = cells.get(new NumberCell(8, 4));
        Cell whiteCell = cells.get(new NumberCell(1, 4));

        Queen blackQueen = new Queen(this, false, blackCell);
        Queen whiteQueen = new Queen(this, true, whiteCell);

        blackCell.setPiece(blackQueen);
        whiteCell.setPiece(whiteQueen);
    }

    private void generateKings() {
        Cell blackCell = cells.get(new NumberCell(8, 5));
        Cell whiteCell = cells.get(new NumberCell(1, 5));

        blackKing = new King(this, false, blackCell);
        whiteKing = new King(this, true, whiteCell);

        blackCell.setPiece(blackKing);
        whiteCell.setPiece(whiteKing);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if ((patWhite || patBlack) && !freeAllowedMoves) {
            g.setFont(new Font("SansSerif", Font.PLAIN, Constants.CELL_SIZE));
            String label;
            if (whiteCheck || blackCheck) {
                label = "Мат ";
                if (patWhite) label = label + "белым!";
                else if (patBlack) label = label + "черным!";

            } else label = "Пат!";

            g.setColor(Color.BLACK);
            int x = Constants.CELL_SIZE * 2;
            int y = Constants.CELL_SIZE * 5 + 10;
            g.drawString(label, x, y);
        }
    }

    public void clear() {
        removeAll();
        start();
    }
}
