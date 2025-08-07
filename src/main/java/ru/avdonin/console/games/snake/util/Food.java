package ru.avdonin.console.games.snake.util;

import lombok.Getter;

import java.awt.*;
import java.util.Random;

public class Food {
    @Getter
    private Point position;
    private final Snake snake;
    private final int maxX;
    private final int maxY;

    public Food(Snake snake) {
        this.snake = snake;
        this.maxX = Constants.WIDTH / Constants.UNIT_SIZE;
        this.maxY = Constants.HEIGHT / Constants.UNIT_SIZE;
        spawn();
    }

    public void spawn() {
        Random rand = new Random();
        Point newPosition;
        do {
            newPosition = new Point(rand.nextInt(maxX), rand.nextInt(maxY));
        } while (snake.getBody().contains(newPosition));
        position = newPosition;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(position.x * Constants.UNIT_SIZE,
                position.y * Constants.UNIT_SIZE,
                Constants.UNIT_SIZE,
                Constants.UNIT_SIZE);
    }
}
