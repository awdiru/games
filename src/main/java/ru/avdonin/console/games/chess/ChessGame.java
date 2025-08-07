package ru.avdonin.console.games.chess;

import ru.avdonin.console.util.Game;

public class ChessGame implements Game {
    @Override
    public void start() {
        new ChessFrame();
    }
}
