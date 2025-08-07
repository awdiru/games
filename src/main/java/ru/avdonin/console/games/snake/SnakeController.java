package ru.avdonin.console.games.snake;

import ru.avdonin.console.games.snake.util.Direction;
import ru.avdonin.console.games.snake.util.Snake;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SnakeController extends KeyAdapter {
    private final Snake snake;
    private final SnakePanel gamePanel;

    public SnakeController(Snake snake, SnakePanel gamePanel) {
        this.snake = snake;
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> {
                if (snake.getDirection() != Direction.DOWN)
                    snake.setDirection(Direction.UP);
            }
            case KeyEvent.VK_RIGHT -> {
                if (snake.getDirection() != Direction.LEFT)
                    snake.setDirection(Direction.RIGHT);
            }
            case KeyEvent.VK_DOWN -> {
                if (snake.getDirection() != Direction.UP)
                    snake.setDirection(Direction.DOWN);
            }
            case KeyEvent.VK_LEFT -> {
                if (snake.getDirection() != Direction.RIGHT)
                    snake.setDirection(Direction.LEFT);
            }
            case KeyEvent.VK_R -> gamePanel.initGame();
        }
    }
}
