package ru.avdonin.console.games.snake;

import ru.avdonin.console.util.Game;

public class SnakeGame implements Game {
    @Override
    public void start() {
        new SnakeFrame();
    }
}
