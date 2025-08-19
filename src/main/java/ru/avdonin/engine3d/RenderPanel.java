package ru.avdonin.engine3d;

import lombok.Getter;
import ru.avdonin.engine3d.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

@Getter
public class RenderPanel extends JPanel {
    private final Map<String, Object3D> space = new HashMap<>();
    private final Camera3D camera = new Camera3D(new Vector3D(new Point3D(), new Point3D(0, 0, 1)));

    private Point3D cameraXAxis;
    private Point3D cameraYAxis;
    private Point3D cameraZAxis;

    public RenderPanel() {
    }

    public void addObject(String name, Object3D o) {
        space.put(name, o);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        updateCameraAxes();

        for (Map.Entry<String, Object3D> entry : space.entrySet()) {
            Object3D obj = entry.getValue();
            for (Polygon3D polygon : obj.getPolygons())
                renderPolygon(g2d, polygon);
        }
    }

    private void updateCameraAxes() {
        cameraZAxis = camera.getVector().getDelta().normalized();

        Point3D up = new Point3D(0, 1, 0);
        if (Math.abs(cameraZAxis.dot(up)) > 0.9) {
            up = new Point3D(0, 0, 1);
        }
        cameraXAxis = up.cross(cameraZAxis).normalized();
        cameraYAxis = cameraZAxis.cross(cameraXAxis).normalized();
    }

    private void renderPolygon(Graphics2D g2d, Polygon3D polygon) {
        // Преобразуем точки в систему координат камеры
        Point3D p1 = toCameraSpace(polygon.getP1());
        Point3D p2 = toCameraSpace(polygon.getP2());
        Point3D p3 = toCameraSpace(polygon.getP3());

        // Проверяем, что полигон перед камерой
        if (p1.getZ() <= 0 || p2.getZ() <= 0 || p3.getZ() <= 0) {
            return;
        }

        // Проецируем точки на экран
        Point2D.Double screenPoint1 = projectPoint(p1);
        Point2D.Double screenPoint2 = projectPoint(p2);
        Point2D.Double screenPoint3 = projectPoint(p3);

        // Проверка на видимость полигона (в системе камеры)
        if (isPolygonVisible(p1, p2, p3)) {
            g2d.setColor(Color.WHITE);
            int[] xPoints = {(int) screenPoint1.x, (int) screenPoint2.x, (int) screenPoint3.x};
            int[] yPoints = {(int) screenPoint1.y, (int) screenPoint2.y, (int) screenPoint3.y};
            g2d.drawPolygon(xPoints, yPoints, 3);
        }
    }


    private Point3D toCameraSpace(Point3D worldPoint) {
        Point3D relative = worldPoint.subtract(camera.getVector().getStart());
        return new Point3D(
                relative.dot(cameraXAxis),
                relative.dot(cameraYAxis),
                relative.dot(cameraZAxis)
        );
    }

    private Point2D.Double projectPoint(Point3D point3D) {
        double distance = camera.getProjectDistance(getHeight()); // Расстояние до плоскости проекции

        double factor = distance / (distance + point3D.getZ());
        double x = point3D.getX() * factor + (double) getWidth() / 2;
        double y = point3D.getY() * factor + (double) getHeight() / 2;
        return new Point2D.Double(x, y);
    }

    private boolean isPolygonVisible(Point3D p1, Point3D p2, Point3D p3) {
        if (true) return true;
        // Вычисляем нормаль в системе координат камеры
        double ux = p2.getX() - p1.getX();
        double uy = p2.getY() - p1.getY();
        double uz = p2.getZ() - p1.getZ();

        double vx = p3.getX() - p1.getX();
        double vy = p3.getY() - p1.getY();
        double vz = p3.getZ() - p1.getZ();

        double nx = uy * vz - uz * vy;
        double ny = uz * vx - ux * vz;
        double nz = ux * vy - uy * vx;

        // Вектор от камеры к полигону (камера в начале координат)
        double dot = nx * p1.getX() + ny * p1.getY() + nz * p1.getZ();
        return dot >= 0;
    }
}
