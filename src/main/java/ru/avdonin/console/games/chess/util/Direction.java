package ru.avdonin.console.games.chess.util;

import lombok.Getter;

@Getter
public enum Direction {
    UP(1, 0),
    DOWN(-1, 0),
    RIGHT(0, 1),
    LEFT(0, -1),
    RIGHT_UP(1, 1),
    RIGHT_DOWN(-1, 1),
    LEFT_UP(1, -1),
    LEFT_DOWN(-1, -1);

    private final int rowOff;
    private final int colOff;

    Direction(int rowOff, int colOff) {
        this.rowOff = rowOff;
        this.colOff = colOff;
    }
}
