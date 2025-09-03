package ru.avdonin.engine3d.buffer;

public class ZBuffer {
    protected double[][] zBuffer;
    protected int maxWidth = 3840;
    protected int maxHeight = 2160;

    public ZBuffer() {
        initZBuffer();
    }

    private void initZBuffer() {
        zBuffer = new double[maxWidth][maxHeight];
        clearZBuffer();
    }

    public void clearZBuffer() {
        for (int x = 0; x < zBuffer.length; x++) {
            for (int y = 0; y < zBuffer[0].length; y++) {
                zBuffer[x][y] = Double.MAX_VALUE;
            }
        }
    }

    public double getDepth(int x, int y) {
        return zBuffer[x][y];
    }

    public void setDepth(int x, int y, double depth) {
        zBuffer[x][y] = depth;
    }
}
