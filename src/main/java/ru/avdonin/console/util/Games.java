package ru.avdonin.console.util;

import lombok.Getter;
import ru.avdonin.console.games.chess.ChessFrame;
import ru.avdonin.console.games.snake.SnakeFrame;

@Getter
public enum Games {
    SNAKE("Змейка", new SnakeFrame()),
    CHES("Шахматы", new ChessFrame());

    private final String name;
    private final Game gameClass;

    Games(String name, Game gameClass) {
        this.name = name;
        this.gameClass = gameClass;
    }
}
