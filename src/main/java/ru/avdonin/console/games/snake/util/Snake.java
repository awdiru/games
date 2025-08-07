package ru.avdonin.console.games.snake.util;


import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

@Getter
public class Snake {
    private final List<Point> body = new LinkedList<>();
    @Setter
    private Direction direction;

    public Snake() {
        this.body.add(new Point(5, 5));
        this.direction = Direction.RIGHT;
    }

    public void move() {
        Point head = body.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case RIGHT -> newHead.x++;
            case LEFT -> newHead.x--;
            case UP -> newHead.y--;
            case DOWN -> newHead.y++;
        }

        body.addFirst(newHead);
        body.removeLast();
    }

    public void grow() {
        Point newTail = new Point(body.getLast());
        body.addLast(newTail);
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        for (Point segment : body)
            g.fillRect(segment.x * Constants.UNIT_SIZE + 1,
                    segment.y * Constants.UNIT_SIZE + 1,
                    Constants.UNIT_SIZE - 2,
                    Constants.UNIT_SIZE - 2);
    }

    public Point getHead() {
        return body.getFirst();
    }
}
