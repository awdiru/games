package ru.avdonin.console.games.chess.util;

import lombok.Getter;

@Getter
public enum Direction {
    UP(0, 1),
    DOWN(0, -1),
    RIGHT(1, 0),
    LEFT(-1, 0),
    RIGHT_UP(1, 1),
    RIGHT_DOWN(1, -1),
    LEFT_UP(-1, 1),
    LEFT_DOWN(-1, -1);

    private final int colOff;
    private final int rowOff;

    Direction(int colOff, int rowOff) {
        this.colOff = colOff;
        this.rowOff = rowOff;
    }
}
