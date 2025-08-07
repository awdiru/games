package ru.avdonin.console.games.snake;

import ru.avdonin.console.games.snake.util.Constants;
import ru.avdonin.console.games.snake.util.Food;
import ru.avdonin.console.games.snake.util.Snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class SnakePanel extends JPanel implements ActionListener {
    private Timer timer;
    private Snake snake;
    private Food food;
    private boolean isRunning;
    private int delay;


    public SnakePanel() {
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        initGame();
    }

    public void initGame() {
        snake = new Snake();
        food = new Food(snake);
        isRunning = true;
        delay = 300;
        timer = new Timer(delay, this);
        timer.start();
        addKeyListener(new SnakeController(snake, this));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (isRunning) {
            snake.draw(g);
            food.draw(g);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Счет: " + (snake.getBody().size() - 1), 10, 25);
        } else {
            gameOver(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isRunning) {
            snake.move();
            checkCollision();
            checkFood();
        }
        repaint();
    }

    private void checkFood() {
        if (Objects.equals(snake.getHead(), food.getPosition())) {
            snake.grow();
            food.spawn();
            delay = delay <= 50 ? 50 : delay - 10;
            timer.setDelay(delay);
        }
    }

    private void checkCollision() {
        Point head = snake.getHead();
        int maxX = Constants.WIDTH / Constants.UNIT_SIZE;
        int maxY = Constants.HEIGHT / Constants.UNIT_SIZE;

        if (head.x < 0) head.x = maxX;
        if (head.y < 0) head.y = maxY;
        if (head.x > maxX) head.x = 0;
        if (head.y > maxY) head.y = 0;

        if (snake.getBody().lastIndexOf(head) != 0) isRunning = false;

        if (!isRunning) timer.stop();
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());

        String text = "Game Over";
        int x = (Constants.WIDTH - metrics.stringWidth(text)) / 2;
        int y = Constants.HEIGHT / 2;
        g.drawString(text, x, y);

        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Нажмите R для перезапуска", Constants.WIDTH / 4, y + 50);
    }

}
