package ru.avdonin.console.games.chess.util;

import ru.avdonin.console.games.chess.pieces.Piece;

public record MoveRecord(Piece piece, NumberCell fromCell, NumberCell toCell) {
}
