package ru.avdonin.engine3d.rendering_panel.buffer;

public class ZBuffer {
    private final static int MAX_WIDTH = 3840;
    private final static int MAX_HEIGHT = 2160;
    private double[][] zBuffer;

    public ZBuffer() {
        initZBuffer();
    }

    private void initZBuffer() {
        zBuffer = new double[MAX_WIDTH][MAX_HEIGHT];
        clearZBuffer();
    }

    /**
     * Очистить буфер глубины
     */
    public void clearZBuffer() {
        for (int x = 0; x < zBuffer.length; x++) {
            for (int y = 0; y < zBuffer[0].length; y++) {
                zBuffer[x][y] = Double.MAX_VALUE;
            }
        }
    }

    /**
     * Вернуть глубину точки
     *
     * @param x координата x на экране
     * @param y координата y на экране
     * @return глубина точки
     */
    public double getDepth(int x, int y) {
        return zBuffer[x][y];
    }

    /**
     * Изменить значение глубины в буфере
     * @param x координата x на экране
     * @param y координата y на экране
     * @param depth новая глубина точки
     */
    public void setDepth(int x, int y, double depth) {
        zBuffer[x][y] = depth;
    }
}
