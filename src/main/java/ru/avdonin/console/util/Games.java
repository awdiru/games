package ru.avdonin.console.util;

import lombok.Getter;
import ru.avdonin.console.games.chess.ChessGame;
import ru.avdonin.console.games.snake.SnakeGame;

@Getter
public enum Games {
    SNAKE("Змейка", new SnakeGame()),
    CHES("Шахматы", new ChessGame());

    private final String name;
    private final Game gameClass;

    Games(String name, Game gameClass) {
        this.name = name;
        this.gameClass = gameClass;
    }
}
