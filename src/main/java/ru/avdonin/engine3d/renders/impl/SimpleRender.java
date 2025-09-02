package ru.avdonin.engine3d.renders.impl;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.BufferHelper;
import ru.avdonin.engine3d.helpers.UtilHelper;
import ru.avdonin.engine3d.renders.Render;
import ru.avdonin.engine3d.renders.zBuffer.ShadowMap;
import ru.avdonin.engine3d.renders.zBuffer.ZBuffer;
import ru.avdonin.engine3d.util.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

@Getter
@Setter
public class SimpleRender extends Render {
    private final Map<Point3D, Color> rendererPoints = new HashMap<>();
    private final Map<Edge3D, Color> rendererLines = new HashMap<>();
    private boolean shadowsEnabled = true;

    private final ZBuffer zBuffer = new ZBuffer();

    public SimpleRender() {
        this(1280, 720);
    }

    public SimpleRender(int width, int height) {
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
        if (shadowsEnabled) {
            for (Light3D light : getLights().values()) {
                if (light.isCastsShadows()) {
                    renderShadowMap(light);
                }
            }
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(87, 87, 87));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        zBuffer.clearZBuffer();

        for (Object3D obj : getObjects().values()) {
            if (!(obj instanceof Light3D)) {
                for (Polygon3D polygon : obj.getPolygons())
                    renderPolygon(g2d, polygon);
            }
        }
        for (Light3D obj : getLights().values()) {
            renderLight(g2d, obj);
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

            if (isVisiblePoint(x, y, depth)) {
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
            drawLine3D(g2d, edge.getP1(), edge.getP2(), color);
        }
        rendererLines.clear();
    }

    private void renderPolygon(Graphics2D g2d, Polygon3D polygon) {
        Point2D.Double p1 = projectPoint(polygon.getP1());
        Point2D.Double p2 = projectPoint(polygon.getP2());
        Point2D.Double p3 = projectPoint(polygon.getP3());

        double angle = UtilHelper.getCameraAngle(polygon, camera);

        if ((angle > 90) && isVisiblePolygon(p1, p2, p3)) {
            Color color = computeColor(polygon);
            rasterizePolygon(g2d, p1, p2, p3, polygon, color);
        }
    }

    private void rasterizePolygon(Graphics2D g2d, Point2D.Double p1, Point2D.Double p2, Point2D.Double p3,
                                  Polygon3D polygon, Color color) {

        int minX = (int) Math.max(0, Math.min(Math.min(p1.x, p2.x), p3.x));
        int maxX = (int) Math.min(getWidth() - 1, Math.max(Math.max(p1.x, p2.x), p3.x));
        int minY = (int) Math.max(0, Math.min(Math.min(p1.y, p2.y), p3.y));
        int maxY = (int) Math.min(getHeight() - 1, Math.max(Math.max(p1.y, p2.y), p3.y));

        double area = BufferHelper.edgeFunction(p1, p2, p3);
        if (Math.abs(area) < 1e-10) return;

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Point2D.Double p = new Point2D.Double(x, y);

                double w1 = BufferHelper.edgeFunction(p2, p3, p) / area;
                double w2 = BufferHelper.edgeFunction(p3, p1, p) / area;
                double w3 = BufferHelper.edgeFunction(p1, p2, p) / area;

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

    private Point2D.Double projectPoint(Point3D point) {
        return projectPoint(point, this.camera, getWidth(), getHeight());
    }

    private Point2D.Double projectPoint(Point3D point, Camera3D camera, int width, int height) {
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
        double x = xCam * factor + width / 2;
        double y = height / 2 - yCam * factor;

        return new Point2D.Double(x, y);
    }


    private boolean isVisiblePolygon(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        return isVisiblePoint(p1) || isVisiblePoint(p2) || isVisiblePoint(p3);
    }

    private boolean isVisiblePoint(Point2D.Double p) {
        int height = getHeight();
        int width = getWidth();
        return p.x <= width && p.x >= 0 && p.y <= height && p.y >= 0;
    }

    private boolean isVisiblePoint(int x, int y, double depth) {
        return x > 0 && x < getWidth() && y > 0 && y < getHeight() && depth < zBuffer.getDepth(x, y);
    }

    private Color computeColor(Polygon3D polygon) {
        double intensity = 0;
        Point3D center = UtilHelper.getCenterPolygon(polygon);

        for (Light3D l : getLights().values()) {
            double distance = UtilHelper.getLength(center, l.getPoint()) / 100;
            double lightIntensity = computeIntensityLight(polygon, l);

            // Проверка нахождения в тени
            if (shadowsEnabled && l.isCastsShadows() && isInShadow(center, l)) {
                lightIntensity *= 0.2; // Ослабление света в тени
            }

            intensity += lightIntensity / distance;
        }
        if (intensity > 255) intensity = 255;
        if (intensity < 20) intensity = 20;

        Color color = polygon.getColor();
        double red = (double) color.getRed() / 255;
        double green = (double) color.getGreen() / 255;
        double blue = (double) color.getBlue() / 255;

        return new Color((int) (red * intensity), (int) (green * intensity), (int) (blue * intensity), color.getAlpha());
    }

    private boolean isInShadow(Point3D point, Light3D light) {
        ShadowMap shadowMap = light.getShadowMap();
        if (shadowMap == null) return false;

        Camera3D lightCamera = createLightCamera(light);
        int resolution = shadowMap.getResolution();
        Point2D.Double shadowCoords = projectPoint(point, lightCamera, resolution, resolution);

        int x = (int) shadowCoords.x;
        int y = (int) shadowCoords.y;

        if (x < 0 || x >= shadowMap.getResolution() ||
                y < 0 || y >= shadowMap.getResolution()) {
            return true;
        }

        double pointDepth = BufferHelper.getPointDepth(point, lightCamera);
        double shadowDepth = shadowMap.getDepth(x, y);

        // Bias для борьбы с самозатенением
        double bias = 0.1;
        return pointDepth > shadowDepth + bias;
    }

    private double computeIntensityLight(Polygon3D polygon, Light3D light) {
        Point3D pCenter = UtilHelper.getCenterPolygon(polygon);
        Vector3D pn = UtilHelper.getNormalVector(UtilHelper.getNormal(polygon));
        Vector3D l = new Vector3D(light.getPoint(), pCenter);
        double angle = UtilHelper.getAngleRad(pn, l);
        return Math.max(getLights().size() * 10, Math.cos(angle) * light.getIntensity());
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

            drawLine2D(g2d, Color.WHITE, xCenter, yCenter, x2, y2, centerDepth);
        }

        Point3D dottedEnd = new Point3D(point.getX(), 0, point.getZ());
        Color dottedColor = new Color(27, 27, 27);
        drawDottedLine3D(g2d, point, dottedEnd, dottedColor, 20);
        double crossSize = (double) getHeight() / 100;
        drawCross2D(g2d, dottedEnd, camera.getVectorZ(), dottedColor, crossSize);
    }

    private void drawLine2D(Graphics2D g2d, Color color, int x1, int y1, int x2, int y2, double depth) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;
        int e2;

        int currentX = x1;
        int currentY = y1;

        g2d.setColor(color);
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

    private void drawCross2D(Graphics2D g2d, Point3D p, Vector3D z, Color color, double size) {
        Point2D.Double p0 = projectPoint(p);
        double depth = BufferHelper.getPointDepth(p, this.camera);

        drawLine2D(g2d, color,
                (int) (p0.x - size), (int) (p0.y - size),
                (int) (p0.x + size), (int) (p0.y + size),
                depth);

        drawLine2D(g2d, color,
                (int) (p0.x - size), (int) (p0.y + size),
                (int) (p0.x + size), (int) (p0.y - size),
                depth);
    }

    private void drawCross3D(Graphics2D g2d, Point3D p, Vector3D z, Color color, double size) {
        Vector3D x = new Vector3D(0, 0, 0);
        UtilHelper.computeVectorX(x, z);
        Vector3D y = new Vector3D(0, 0, 0);
        UtilHelper.computeVectorY(x, y, z);

        x = UtilHelper.changeLenVector(x, size);
        Vector3D x1 = new Vector3D(-x.getEnd().getX(), x.getEnd().getY(), x.getEnd().getZ());

        y = UtilHelper.changeLenVector(y, size);
        Vector3D y1 = new Vector3D(y.getEnd().getX(), -y.getEnd().getY(), y.getEnd().getZ());

        Point3D p1 = new Point3D(p);
        p1.translate(x);
        p1.translate(y);

        Point3D p2 = new Point3D(p);
        p2.translate(x1);
        p2.translate(y1);

        Point3D p3 = new Point3D(p);
        p3.translate(x);
        p3.translate(y1);

        Point3D p4 = new Point3D(p);
        p4.translate(x1);
        p4.translate(y);

        drawLine3D(g2d, p1, p2, color);
        drawLine3D(g2d, p3, p4, color);
    }

    private void renderShadowMap(Light3D light) {
        ShadowMap shadowMap = light.getShadowMap();
        if (shadowMap == null) return;

        shadowMap.clearZBuffer();
        Camera3D lightCamera = createLightCamera(light);

        for (Object3D obj : getObjects().values()) {
            if (obj instanceof Light3D) continue;
            for (Polygon3D polygon : obj.getPolygons()) {
                renderPolygonToShadowMap(polygon, lightCamera, shadowMap);
            }
        }
    }

    private Camera3D createLightCamera(Light3D light) {
        Point3D lightPos = light.getPoint();
        Vector3D lightDir = light.getVector();
        Camera3D lightCamera = new Camera3D(lightPos, lightDir);
        lightCamera.setViewingAngleRad(Math.toRadians(90));
        return lightCamera;
    }

    private void renderPolygonToShadowMap(Polygon3D polygon, Camera3D lightCamera, ShadowMap shadowMap) {
        int resolution = shadowMap.getResolution();
        Point2D.Double p1 = projectPoint(polygon.getP1(), lightCamera, resolution, resolution);
        Point2D.Double p2 = projectPoint(polygon.getP2(), lightCamera, resolution, resolution);
        Point2D.Double p3 = projectPoint(polygon.getP3(), lightCamera, resolution, resolution);

        if (!isVisiblePolygon(p1, p2, p3)) return;

        int minX = (int) Math.max(0, Math.min(Math.min(p1.x, p2.x), p3.x));
        int maxX = (int) Math.min(shadowMap.getResolution() - 1, Math.max(Math.max(p1.x, p2.x), p3.x));
        int minY = (int) Math.max(0, Math.min(Math.min(p1.y, p2.y), p3.y));
        int maxY = (int) Math.min(shadowMap.getResolution() - 1, Math.max(Math.max(p1.y, p2.y), p3.y));

        double area = BufferHelper.edgeFunction(p1, p2, p3);
        if (Math.abs(area) < 1e-10) return;

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                Point2D.Double p = new Point2D.Double(x, y);

                double w1 = BufferHelper.edgeFunction(p2, p3, p) / area;
                double w2 = BufferHelper.edgeFunction(p3, p1, p) / area;
                double w3 = BufferHelper.edgeFunction(p1, p2, p) / area;

                if (w1 >= 0 && w2 >= 0 && w3 >= 0) {
                    double depth = BufferHelper.interpolateDepth(polygon, lightCamera, w1, w2, w3);
                    if (depth < shadowMap.getDepth(x, y)) {
                        shadowMap.setDepth(x, y, depth);
                    }
                }
            }
        }
    }
}