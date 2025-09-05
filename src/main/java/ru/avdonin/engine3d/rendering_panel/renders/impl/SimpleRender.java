package ru.avdonin.engine3d.rendering_panel.renders.impl;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.rendering_panel.buffer.FrameBuffer;
import ru.avdonin.engine3d.rendering_panel.buffer.ZBuffer;
import ru.avdonin.engine3d.menu_panels.left.helpers.BufferHelper;
import ru.avdonin.engine3d.menu_panels.left.helpers.RenderHelper;
import ru.avdonin.engine3d.menu_panels.left.helpers.UtilHelper;
import ru.avdonin.engine3d.rendering_panel.renders.Render;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.rendering_panel.util.Obj;
import ru.avdonin.engine3d.rendering_panel.util.objects.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

@Getter
@Setter
public class SimpleRender extends Render {
    private static final double DEPTH_EPSILON = 1e-5;
    private final ZBuffer zBuffer = new ZBuffer();
    private final FrameBuffer frameBuffer = new FrameBuffer();

    public SimpleRender() {
        this(1280, 720);
    }

    public SimpleRender(int width, int height) {
        setSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        SceneStorage storage = getStorage();
        super.paintComponent(g);

        zBuffer.clearZBuffer();
        frameBuffer.clearBuffer();

        for (Map.Entry<String, Obj<?>> entry : storage.getObjects().entrySet()) {
            String name = entry.getKey();
            Obj<?> obj = entry.getValue();
            if (obj instanceof Light3D o)
                renderLight(o);
            else if (obj instanceof Point3D o)
                renderPoint(o);
            else if (obj instanceof Vector3D o)
                renderVector(o);
            else if (obj instanceof Edge3D o)
                renderLine(o);
            else if (obj instanceof Polygon3D o)
                renderPolygon(o);
            else if (obj instanceof Object3D o)
                for (Polygon3D polygon : o.getPolygons())
                    renderPolygon(polygon);
        }

        drawFrameBuffer((Graphics2D) g);
    }

    private void drawFrameBuffer(Graphics2D g2d) {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                g2d.setColor(frameBuffer.getColor(x, y));
                g2d.fillRect(x, y, 1, 1);
            }
        }
    }

    private void setPixel(int x, int y, double depth, Color color) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return;

        double currentDepth = zBuffer.getDepth(x, y);
        if (depth < currentDepth - DEPTH_EPSILON) {
            zBuffer.setDepth(x, y, depth);
            frameBuffer.setColor(x, y, color);
        } else if (Math.abs(depth - currentDepth) < DEPTH_EPSILON) {
            if (RenderHelper.getBrightness(color) > RenderHelper.getBrightness(frameBuffer.getColor(x, y)))
                frameBuffer.setColor(x, y, color);

        }
    }

    private void renderPoint(Point3D point) {
        Color color = point.getColor();

        Point2D.Double p = projectPoint(point);
        if (!isVisiblePoint(p)) return;

        double depth = BufferHelper.getPointDepth(point, this.camera);
        int x = (int) p.x;
        int y = (int) p.y;

        setPixel(x, y, depth, color);
    }

    private void renderLine(Edge3D edge) {
        renderLine(edge, edge.getColor());
    }

    private void renderLine(Edge3D edge, Color color) {
        renderLine3D(edge.getP1(), edge.getP2(), color);
    }

    private void renderVector(Vector3D vector) {
        renderLine(vector);
        Vector3D s = UtilHelper.changeLenVector(new Vector3D(vector.getEnd(), vector.getStart()), 10);
        s.setColor(vector.getColor());

        Vector3D s1 = new Vector3D(s);
        s1.getEnd().translate(new Vector3D(0, 5, 0));
        Vector3D s2 = new Vector3D(s);
        s2.getEnd().translate(new Vector3D(0, -5, 0));

        renderLine(s1);
        renderLine(s2);
    }

    private void renderPolygon(Polygon3D polygon) {
        Point2D.Double p1 = projectPoint(polygon.getP1());
        Point2D.Double p2 = projectPoint(polygon.getP2());
        Point2D.Double p3 = projectPoint(polygon.getP3());

        double angle = getCameraAngle(polygon);

        Boolean isSkeleton = Context.get(Constants.IS_SKELETON_KEY);
        if (((angle > 90) && isVisiblePolygon(p1, p2, p3)) || isSkeleton) {
            Color color = RenderHelper.computeColor(polygon);
            rasterizePolygon(p1, p2, p3, polygon, color);
        }
    }

    private void rasterizePolygon(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3,
                                  Polygon3D polygon, Color color) {

        Boolean isSkeleton = Context.get(Constants.IS_SKELETON_KEY);
        if (isSkeleton) {
            renderLine(polygon.getEdge1(), color);
            renderLine(polygon.getEdge2(), color);
            renderLine(polygon.getEdge3(), color);
            return;
        }
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
                    double depth = BufferHelper.interpolateDepth(polygon, this.camera, w1, w2, w3);
                    setPixel(x, y, depth, color);
                }
            }
        }
    }

    private double edgeFunction(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
        return (c.x - a.x) * (b.y - a.y) - (c.y - a.y) * (b.x - a.x);
    }


    private Point2D.Double projectPoint(Point3D point) {
        Point3D cameraPoint = camera.getPoint();
        Vector3D cameraZ = camera.getBasis().getVectorZ();
        Vector3D cameraX = camera.getBasis().getVectorX();
        Vector3D cameraY = camera.getBasis().getVectorY();

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


    private void renderLight(Light3D light) {
        Point3D point = light.getPoint();
        Point2D.Double center = projectPoint(point);
        if (!isVisiblePoint(center)) return;

        double centerDepth = BufferHelper.getPointDepth(point, this.camera);
        int xCenter = (int) center.x;
        int yCenter = (int) center.y;

        double distance = UtilHelper.getLength(camera.getPoint(), point);
        int r = (int) (light.getIntensity() * 30 / distance);
        if (r > 50) r = 50;

        for (int i = 0; i < 360; i += 30) {
            int x2 = xCenter + (int) (Math.cos(Math.toRadians(i)) * r);
            int y2 = yCenter + (int) (Math.sin(Math.toRadians(i)) * r);

            renderLine2D(xCenter, yCenter, x2, y2, centerDepth, new Color(198, 198, 198));
        }
        Point3D dottedEnd = new Point3D(point.getX(), 0, point.getZ());
        Color dottedColor = new Color(27, 27, 27);
        renderDottedLine3D(point, dottedEnd, dottedColor, 20);

        Point3D d1 = new Point3D(dottedEnd.getX() - 10, dottedEnd.getY() - 10, dottedEnd.getZ());
        Point3D d2 = new Point3D(dottedEnd.getX() + 10, dottedEnd.getY() + 10, dottedEnd.getZ());
        Point3D d3 = new Point3D(dottedEnd.getX() - 10, dottedEnd.getY() + 10, dottedEnd.getZ());
        Point3D d4 = new Point3D(dottedEnd.getX() + 10, dottedEnd.getY() - 10, dottedEnd.getZ());

        renderLine3D(d1, d2, dottedColor);
        renderLine3D(d3, d4, dottedColor);
    }

    private void renderLine2D(int x1, int y1, int x2, int y2, double depth, Color color) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;
        int e2;

        int currentX = x1;
        int currentY = y1;

        while (true) {
            setPixel(currentX, currentY, depth, color);
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

    private void renderLine3D(Point3D point1, Point3D point2, Color color) {
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
            setPixel(x1, y1, depthStart, color);
            return;
        }

        double depthStep = (depthEnd - depthStart) / steps;
        double currentDepth = depthStart;

        double x = x1;
        double y = y1;
        double xStep = (double) (x2 - x1) / steps;
        double yStep = (double) (y2 - y1) / steps;

        for (int i = 0; i <= steps; i++) {
            int ix = (int) Math.round(x);
            int iy = (int) Math.round(y);
            setPixel(ix, iy, currentDepth, color);
            x += xStep;
            y += yStep;
            currentDepth += depthStep;
        }
    }

    private void renderDottedLine3D(Point3D point1, Point3D point2, Color color, double dottedSize) {
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
            renderLine3D(p, p1, color);
            p1.translate(vector);
        }
        renderLine3D(p1, p2, color);
    }
}