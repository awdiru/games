package ru.avdonin.engine3d.renders.impl;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.buffer.ZBuffer;
import ru.avdonin.engine3d.helpers.BufferHelper;
import ru.avdonin.engine3d.helpers.RenderHelper;
import ru.avdonin.engine3d.helpers.UtilHelper;
import ru.avdonin.engine3d.renders.Render;
import ru.avdonin.engine3d.util.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

@Getter
@Setter
public class SimpleRender extends Render {
    private final Map<Point3D, Color> rendererPoints = new HashMap<>();
    private final Map<Edge3D, Color> rendererLines = new HashMap<>();
    private final ZBuffer zBuffer = new ZBuffer();

    public SimpleRender() {
        this(1280, 720);
    }

    public SimpleRender(int width, int height) {
        super();
        setSize(new Dimension(width, height));
    }

    @Override
    public void renderPoint(Point3D point, Color color) {
        rendererPoints.put(point, color);
    }

    @Override
    public void renderLine(Edge3D edge, Color color) {
        rendererLines.put(edge, color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(87, 87, 87));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        zBuffer.clearZBuffer();

        for (Object3D obj : sceneStorage.getObjects().values()) {
            if (obj instanceof Light3D)
                renderLight(g2d, (Light3D) obj);

            else {
                for (Polygon3D polygon : obj.getPolygons())
                    renderPolygon(g2d, polygon);
            }
        }

        paintLines(g2d);
        paintPoints(g2d);
    }

    private void paintPoints(Graphics2D g2d) {
        for (Map.Entry<Point3D, Color> entry : rendererPoints.entrySet()) {
            Point3D point = entry.getKey();
            Color color = entry.getValue();

            Point2D.Double p = projectPoint(point);
            if (!isVisiblePoint(p)) continue;

            double depth = BufferHelper.getPointDepth(point, this.camera);
            int x = (int) p.x;
            int y = (int) p.y;

            if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight() && depth < zBuffer.getDepth(x, y)) {
                zBuffer.setDepth(x, y, depth);
                g2d.setColor(color);
                g2d.fillOval(x, y, 3, 3);
            }
        }
        rendererPoints.clear();
    }

    private void paintLines(Graphics2D g2d) {
        for (Map.Entry<Edge3D, Color> entry : rendererLines.entrySet()) {
            Edge3D edge = entry.getKey();
            Color color = entry.getValue();

            // Проверяем видимость обеих точек отрезка
            Point2D.Double p1 = projectPoint(edge.getP1());
            Point2D.Double p2 = projectPoint(edge.getP2());

            if (isVisiblePoint(p1) || isVisiblePoint(p2)) {
                drawLine3D(g2d, edge.getP1(), edge.getP2(), color);
            }
        }
        rendererLines.clear();
    }

    private void renderPolygon(Graphics2D g2d, Polygon3D polygon) {
        Point2D.Double p1 = projectPoint(polygon.getP1());
        Point2D.Double p2 = projectPoint(polygon.getP2());
        Point2D.Double p3 = projectPoint(polygon.getP3());

        double angle = getCameraAngle(polygon);

        if ((angle > 90) && isVisiblePolygon(p1, p2, p3)) {
            Color color = RenderHelper.computeColor(polygon, sceneStorage);
            rasterizePolygon(g2d, p1, p2, p3, polygon, color);
        }
    }

    private void rasterizePolygon(Graphics2D g2d, Point2D.Double p1, Point2D.Double p2, Point2D.Double p3,
                                  Polygon3D polygon, Color color) {
        int minX = (int) Math.max(0, Math.min(Math.min(p1.x, p2.x), p3.x));
        int maxX = (int) Math.min(getWidth() - 1, Math.max(Math.max(p1.x, p2.x), p3.x));
        int minY = (int) Math.max(0, Math.min(Math.min(p1.y, p2.y), p3.y));
        int maxY = (int) Math.min(getHeight() - 1, Math.max(Math.max(p1.y, p2.y), p3.y));

        double area = edgeFunction(p1, p2, p3);
        if (Math.abs(area) < 1e-10) return; // Избегаем деления на ноль

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Point2D.Double p = new Point2D.Double(x, y);

                double w1 = edgeFunction(p2, p3, p) / area;
                double w2 = edgeFunction(p3, p1, p) / area;
                double w3 = edgeFunction(p1, p2, p) / area;

                if (w1 >= 0 && w2 >= 0 && w3 >= 0) {
                    double z = BufferHelper.interpolateDepth(polygon, this.camera, w1, w2, w3);

                    if (z < zBuffer.getDepth(x, y)) {
                        zBuffer.setDepth(x, y, z);
                        g2d.setColor(color);
                        g2d.fillRect(x, y, 1, 1);
                    }
                }
            }
        }
    }

    private double edgeFunction(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
        return (c.x - a.x) * (b.y - a.y) - (c.y - a.y) * (b.x - a.x);
    }


    private Point2D.Double projectPoint(Point3D point) {
        Point3D cameraPoint = camera.getPoint();
        Vector3D cameraZ = UtilHelper.getNormalVector(camera.getVectorZ());
        Vector3D cameraX = UtilHelper.getNormalVector(camera.getVectorX());
        Vector3D cameraY = UtilHelper.getNormalVector(camera.getVectorY());

        Vector3D viewVector = new Vector3D(cameraPoint, point);

        double xCam = viewVector.dot(cameraX);
        double yCam = viewVector.dot(cameraY);
        double zCam = viewVector.dot(cameraZ);

        double distance = camera.getProjectDistance(getHeight());
        if (zCam <= 0) return new Point2D.Double(-1, -1);

        double factor = distance / zCam;
        double x = xCam * factor + getWidth() / 2;
        double y = getHeight() / 2 - yCam * factor;

        return new Point2D.Double(x, y);
    }

    private double getCameraAngle(Polygon3D polygon) {
        Vector3D polygonNormal = UtilHelper.getNormalVector(UtilHelper.getNormal(polygon));
        Point3D cameraPoint = camera.getPoint();
        Point3D centerPolygon = UtilHelper.getCenterPolygon(polygon);
        Vector3D toCamera = new Vector3D(centerPolygon, cameraPoint);
        return UtilHelper.getAngle(polygonNormal, toCamera);
    }

    private boolean isVisiblePolygon(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        return isVisiblePoint(p1) || isVisiblePoint(p2) || isVisiblePoint(p3);
    }

    private boolean isVisiblePoint(Point2D.Double p) {
        int height = getHeight();
        int width = getWidth();
        return p.x <= width && p.x >= 0 && p.y <= height && p.y >= 0;
    }



    private void renderLight(Graphics2D g2d, Light3D light) {
        Point3D point = light.getPoint();
        Point2D.Double center = projectPoint(point);
        if (!isVisiblePoint(center)) return;

        double centerDepth = BufferHelper.getPointDepth(point, this.camera);
        int xCenter = (int) center.x;
        int yCenter = (int) center.y;

        double distance = UtilHelper.getLength(camera.getPoint(), point);
        int r = (int) (light.getIntensity() * 30 / distance);
        if (r > 50) r = 50;

        g2d.setColor(new Color(198, 198, 198));

        for (int i = 0; i < 360; i += 30) {
            int x2 = xCenter + (int) (Math.cos(Math.toRadians(i)) * r);
            int y2 = yCenter + (int) (Math.sin(Math.toRadians(i)) * r);

            drawLine2D(g2d, xCenter, yCenter, x2, y2, centerDepth);
        }
        Point3D dottedEnd = new Point3D(point.getX(), 0, point.getZ());
        Color dottedColor = new Color(27, 27, 27);
        drawDottedLine3D(g2d, point, dottedEnd, dottedColor, 20);

        Point3D d1 = new Point3D(dottedEnd.getX() - 10, dottedEnd.getY() - 10, dottedEnd.getZ());
        Point3D d2 = new Point3D(dottedEnd.getX() + 10, dottedEnd.getY() + 10, dottedEnd.getZ());
        Point3D d3 = new Point3D(dottedEnd.getX() - 10, dottedEnd.getY() + 10, dottedEnd.getZ());
        Point3D d4 = new Point3D(dottedEnd.getX() + 10, dottedEnd.getY() - 10, dottedEnd.getZ());

        drawLine3D(g2d, d1, d2, dottedColor);
        drawLine3D(g2d, d3, d4, dottedColor);
    }

    private void drawLine2D(Graphics2D g2d, int x1, int y1, int x2, int y2, double depth) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;
        int e2;

        int currentX = x1;
        int currentY = y1;

        while (true) {
            if (currentX >= 0 && currentX < getWidth() && currentY >= 0 && currentY < getHeight()) {
                if (depth < zBuffer.getDepth(currentX, currentY)) {
                    g2d.drawLine(currentX, currentY, currentX, currentY);
                    zBuffer.setDepth(currentX, currentY, depth);
                }
            }

            if (currentX == x2 && currentY == y2) break;
            e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                currentX += sx;
            }
            if (e2 < dx) {
                err += dx;
                currentY += sy;
            }
        }
    }

    private void drawLine3D(Graphics2D g2d, Point3D point1, Point3D point2, Color color) {
        Point2D.Double p1 = projectPoint(point1);
        Point2D.Double p2 = projectPoint(point2);

        double depthStart = BufferHelper.getPointDepth(point1, this.camera);
        double depthEnd = BufferHelper.getPointDepth(point2, this.camera);

        int x1 = (int) p1.x;
        int y1 = (int) p1.y;
        int x2 = (int) p2.x;
        int y2 = (int) p2.y;

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int steps = Math.max(dx, dy);

        if (steps == 0) {
            if (x1 >= 0 && x1 < getWidth() && y1 >= 0 && y1 < getHeight() && depthStart < zBuffer.getDepth(x1, y1)) {
                g2d.setColor(color);
                g2d.fillRect(x1, y1, 1, 1);
                zBuffer.setDepth(x1, y1, depthStart);
            }
            return;
        }

        double depthStep = (depthEnd - depthStart) / steps;
        double currentDepth = depthStart;

        double x = x1;
        double y = y1;
        double xStep = (double) (x2 - x1) / steps;
        double yStep = (double) (y2 - y1) / steps;

        g2d.setColor(color);
        for (int i = 0; i <= steps; i++) {
            int ix = (int) Math.round(x);
            int iy = (int) Math.round(y);

            if (ix >= 0 && ix < getWidth() && iy >= 0 && iy < getHeight() && currentDepth < zBuffer.getDepth(ix, iy)) {
                g2d.fillRect(ix, iy, 1, 1);
                zBuffer.setDepth(ix, iy, currentDepth);
            }

            x += xStep;
            y += yStep;
            currentDepth += depthStep;
        }
    }

    private void drawDottedLine3D(Graphics2D g2d, Point3D point1, Point3D point2, Color color, double dottedSize) {
        Point3D p1 = new Point3D(point1);
        Point3D p2 = new Point3D(point2);


        Vector3D vector = new Vector3D(p1, p2);
        double length = vector.getLength();
        vector = UtilHelper.getNormalVector(vector);
        vector = UtilHelper.changeLenVector(vector, dottedSize);

        int N = (int) (length / dottedSize);

        for (int i = 0; i < N / 2; i++) {
            Point3D p = new Point3D(p1);
            p1.translate(vector);
            drawLine3D(g2d, p, p1, color);
            p1.translate(vector);
        }
        drawLine3D(g2d, p1, p2, color);
    }
}