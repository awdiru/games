package ru.avdonin.engine3d.rendering_panel.buffer;

import java.awt.*;

public class FrameBuffer {
    private final static int MAX_WIDTH = 3840;
    private final static int MAX_HEIGHT = 2160;
    private Color[][] buffer;

    public FrameBuffer() {
        initBuffer();
    }

    private void initBuffer() {
        buffer = new Color[MAX_WIDTH][MAX_HEIGHT];
        clearBuffer();
    }

    /**
     * Очистить буфер цветов
     */
    public void clearBuffer() {
        for (int x = 0; x < buffer.length; x++) {
            for (int y = 0; y < buffer[0].length; y++) {
                buffer[x][y] = new Color(87, 87, 87);
            }
        }
    }

    /**
     * Вернуть цвет точки
     *
     * @param x координата x на экране
     * @param y координата y на экране
     * @return цвет точки
     */
    public Color getColor(int x, int y) {
        return buffer[x][y];
    }

    /**
     * Изменить значение цвета в буфере
     *
     * @param x     координата x на экране
     * @param y     координата y на экране
     * @param color новый цвет точки
     */
    public void setColor(int x, int y, Color color) {
        buffer[x][y] = color;
    }
}
