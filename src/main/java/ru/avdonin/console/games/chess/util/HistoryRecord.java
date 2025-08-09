package ru.avdonin.console.games.chess.util;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.console.games.chess.pieces.Piece;

@Getter
@Setter
public class HistoryRecord {
    private MoveRecord moveRecordW;
    private MoveRecord moveRecordB;

    public HistoryRecord(MoveRecord moveRecordW, MoveRecord moveRecordB) {
        this.moveRecordW = moveRecordW;
        this.moveRecordB = moveRecordB;
    }
}
