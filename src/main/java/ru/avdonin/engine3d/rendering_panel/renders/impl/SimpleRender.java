package ru.avdonin.engine3d.rendering_panel.renders.impl;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.rendering_panel.buffer.FrameBuffer;
import ru.avdonin.engine3d.rendering_panel.buffer.ZBuffer;
import ru.avdonin.engine3d.helpers.BufferHelper;
import ru.avdonin.engine3d.helpers.RenderHelper;
import ru.avdonin.engine3d.helpers.VectorHelper;
import ru.avdonin.engine3d.rendering_panel.renders.Render;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
@Setter
public class SimpleRender extends Render {
    private static final double DEPTH_EPSILON = 1e-5;
    private final ZBuffer zBuffer = new ZBuffer();
    private final FrameBuffer frameBuffer = new FrameBuffer();
    private ExecutorService executorService;

    public SimpleRender() {
        this(1280, 720);
    }

    public SimpleRender(int width, int height) {
        setSize(new Dimension(width, height));
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    protected void paintComponent(Graphics g) {
        SceneStorage storage = getStorage();
        super.paintComponent(g);
        Boolean noiseFilter = Context.get(Constants.NOISE_FILTER);

        zBuffer.clearZBuffer();
        if (noiseFilter) frameBuffer.clearBuffer();
        List<Callable<Void>> tasks = new ArrayList<>();

        Graphics2D g2d = (Graphics2D) g;
        if (!noiseFilter){
            g2d.setColor(Constants.BACKGROUND);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        for (Map.Entry<String, AbstractObject3D<?>> entry : storage.getObjects().entrySet()) {
            String name = entry.getKey();
            AbstractObject3D<?> obj = entry.getValue();
            tasks.add(() -> {
                if (obj instanceof Light3D o)
                    renderLight(g2d, o);
                else if (obj instanceof Point3D o)
                    renderPoint(g2d, o);
                else if (obj instanceof Vector3D o)
                    renderVector(g2d, o);
                else if (obj instanceof Edge3D o)
                    renderLine(g2d, o);
                else if (obj instanceof Polygon3D o)
                    renderPolygon(g2d, o);
                else if (obj instanceof Object3D o)
                    for (Polygon3D polygon : o.getPolygons())
                        renderPolygon(g2d, polygon);
                return null;
            });
        }

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (noiseFilter) drawFrameBuffer((Graphics2D) g);
    }

    private void drawFrameBuffer(Graphics2D g2d) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                image.setRGB(x, y, frameBuffer.getColor(x, y).getRGB());
            }
        }
        g2d.drawImage(image, 0, 0, null);
    }


    private void setPixel(Graphics2D g2d, int x, int y, double depth, Color color) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) return;

        Boolean noiseFilter = Context.get(Constants.NOISE_FILTER);
        double currentDepth = zBuffer.getDepth(x, y);

        if (!noiseFilter) {
            if (depth < currentDepth) {
                g2d.setColor(color);
                g2d.fillRect(x, y, 1, 1);
            }
            return;
        }

        if (depth < currentDepth - DEPTH_EPSILON) {
            zBuffer.setDepth(x, y, depth);
            frameBuffer.setColor(x, y, color);
        } else if (Math.abs(depth - currentDepth) < DEPTH_EPSILON) {
            if (RenderHelper.getBrightness(color) > RenderHelper.getBrightness(frameBuffer.getColor(x, y)))
                frameBuffer.setColor(x, y, color);

        }
    }

    private void renderPoint(Graphics2D g2d, Point3D point) {
        Color color = point.getColor();

        Point2D.Double p = projectPoint(point);
        if (!isVisiblePoint(p)) return;

        double depth = BufferHelper.getPointDepth(point, this.camera);
        int x = (int) p.x;
        int y = (int) p.y;

        setPixel(g2d, x, y, depth, color);
    }

    private void renderLine(Graphics2D g2d, Edge3D edge) {
        renderLine(g2d, edge, edge.getColor());
    }

    private void renderLine(Graphics2D g2d, Edge3D edge, Color color) {
        renderLine3D(g2d, edge.getP1(), edge.getP2(), color);
    }

    private void renderVector(Graphics2D g2d, Vector3D vector) {
        renderLine(g2d, vector);
        Vector3D s = VectorHelper.changeLenVector(new Vector3D(vector.getEnd(), vector.getStart()), 10);
        s.setColor(vector.getColor());

        Vector3D s1 = new Vector3D(s);
        Vector3D s2 = new Vector3D(s);

        Vector3D normalize = VectorHelper.normalizeVector(vector);

        if (normalize.equals(new Vector3D(1, 0, 0))) {
            s1.getEnd().translate(new Vector3D(0, 5, 0));
            s2.getEnd().translate(new Vector3D(0, -5, 0));
        } else {
            Vector3D off = new Vector3D(1, 0, 0).cross(normalize);

            off = VectorHelper.changeLenVector(off, 5);
            s1.getEnd().translate(off);

            off = VectorHelper.changeLenVector(off, -5);
            s2.getEnd().translate(off);
        }
        renderLine(g2d, s1);
        renderLine(g2d, s2);
    }

    private void renderPolygon(Graphics2D g2d, Polygon3D polygon) {
        Point2D.Double p1 = projectPoint(polygon.getP1());
        Point2D.Double p2 = projectPoint(polygon.getP2());
        Point2D.Double p3 = projectPoint(polygon.getP3());

        double angle = getCameraAngle(polygon);

        Boolean isSkeleton = Context.get(Constants.IS_SKELETON_KEY);
        if (((angle > 90) && isVisiblePolygon(p1, p2, p3)) || isSkeleton) {
            Color color = RenderHelper.computeColor(polygon);
            rasterizePolygon(g2d, p1, p2, p3, polygon, color);
        }
    }

    private void rasterizePolygon(Graphics2D g2d, Point2D.Double p1, Point2D.Double p2, Point2D.Double p3,
                                  Polygon3D polygon, Color color) {

        Boolean isSkeleton = Context.get(Constants.IS_SKELETON_KEY);
        if (isSkeleton) {
            renderLine(g2d, polygon.getEdge1(), color);
            renderLine(g2d, polygon.getEdge2(), color);
            renderLine(g2d, polygon.getEdge3(), color);
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
                    setPixel(g2d, x, y, depth, color);
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
        Vector3D polygonNormal = polygon.getNormal();
        Point3D cameraPoint = camera.getPoint();
        Point3D centerPolygon = VectorHelper.getCenterPolygon(polygon);
        Vector3D toCamera = new Vector3D(centerPolygon, cameraPoint);
        return VectorHelper.getAngle(polygonNormal, toCamera);
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
        Boolean renderingLights = Context.get(Constants.RENDERING_LIGHTS_OBJ);
        if (!renderingLights) return;
        Point3D point = light.getPoint();
        Point2D.Double center = projectPoint(point);
        if (!isVisiblePoint(center)) return;

        double centerDepth = BufferHelper.getPointDepth(point, this.camera);
        int xCenter = (int) center.x;
        int yCenter = (int) center.y;

        double distance = VectorHelper.getLength(camera.getPoint(), point);
        int r = (int) (light.getIntensity() * 30 / distance);
        if (r > 50) r = 50;

        for (int i = 0; i < 360; i += 30) {
            int x2 = xCenter + (int) (Math.cos(Math.toRadians(i)) * r);
            int y2 = yCenter + (int) (Math.sin(Math.toRadians(i)) * r);

            renderLine2D(g2d, xCenter, yCenter, x2, y2, centerDepth, new Color(198, 198, 198));
        }
        Point3D dottedEnd = new Point3D(point.getX(), 0, point.getZ());
        Color dottedColor = new Color(27, 27, 27);
        renderDottedLine3D(g2d, point, dottedEnd, dottedColor, 20);
        Vector3D left = VectorHelper.changeLenVector(camera.getBasis().getVectorX(), 10);
        Vector3D right = VectorHelper.changeLenVector(camera.getBasis().getVectorX(), 10);
        right = new Vector3D(right.getEnd(), right.getStart());

        Point3D d1 = new Point3D(dottedEnd);
        d1.translate(new Vector3D(0, 10, 0));
        d1.translate(left);

        Point3D d2 = new Point3D(dottedEnd);
        d2.translate(new Vector3D(0, -10, 0));
        d2.translate(right);

        Point3D d3 = new Point3D(dottedEnd);
        d3.translate(new Vector3D(0, -10, 0));
        d3.translate(left);

        Point3D d4 = new Point3D(dottedEnd);
        d4.translate(new Vector3D(0, 10, 0));
        d4.translate(right);

        renderLine3D(g2d, d1, d2, dottedColor);
        renderLine3D(g2d, d3, d4, dottedColor);
    }

    private void renderLine2D(Graphics2D g2d, int x1, int y1, int x2, int y2, double depth, Color color) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;
        int e2;

        int currentX = x1;
        int currentY = y1;

        while (true) {
            setPixel(g2d, currentX, currentY, depth, color);
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

    private void renderLine3D(Graphics2D g2d, Point3D point1, Point3D point2, Color color) {
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
            setPixel(g2d, x1, y1, depthStart, color);
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
            setPixel(g2d, ix, iy, currentDepth, color);
            x += xStep;
            y += yStep;
            currentDepth += depthStep;
        }
    }

    private void renderDottedLine3D(Graphics2D g2d, Point3D point1, Point3D point2, Color color, double dottedSize) {
        Point3D p1 = new Point3D(point1);
        Point3D p2 = new Point3D(point2);


        Vector3D vector = new Vector3D(p1, p2);
        double length = vector.getLength();
        vector = VectorHelper.normalizeVector(vector);
        vector = VectorHelper.changeLenVector(vector, dottedSize);

        int N = (int) (length / dottedSize);

        for (int i = 0; i < N / 2; i++) {
            Point3D p = new Point3D(p1);
            p1.translate(vector);
            renderLine3D(g2d, p, p1, color);
            p1.translate(vector);
        }
        renderLine3D(g2d, p1, p2, color);
    }
}