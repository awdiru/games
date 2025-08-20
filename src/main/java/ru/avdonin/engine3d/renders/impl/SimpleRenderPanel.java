package ru.avdonin.engine3d.renders.impl;

import ru.avdonin.engine3d.helpers.UtilHelper;
import ru.avdonin.engine3d.renders.Render;
import ru.avdonin.engine3d.util.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Map;

public class SimpleRenderPanel extends Render {

    public SimpleRenderPanel() {
    }

    @Override
    public void renderPoint(Point3D point, Color color) {
        Graphics2D g2d = (Graphics2D) getGraphics();
        Point2D.Double p = projectPoint(point);
        if (!isVisiblePoint(p)) return;
        g2d.setColor(color);
        g2d.fillOval((int) p.x, (int) p.y, 5, 5);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(87, 87, 87));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        for (Map.Entry<String, Object3D> entry : space.entrySet()) {
            Object3D obj = entry.getValue();
            for (Polygon3D polygon : obj.getPolygons())
                renderPolygon(g2d, polygon);
        }
        renderLights(g2d);
    }

    private void renderPolygon(Graphics2D g2d, Polygon3D polygon) {
        Point2D.Double p1 = projectPoint(polygon.getP1());
        Point2D.Double p2 = projectPoint(polygon.getP2());
        Point2D.Double p3 = projectPoint(polygon.getP3());

        double angle = getCameraAngle(polygon);

        if (angle > 70 && isVisiblePolygon(p1, p2, p3)) {
            g2d.setColor(computeColor(polygon, Color.WHITE));
            int[] xPoints = {(int) p1.x, (int) p2.x, (int) p3.x};
            int[] yPoints = {(int) p1.y, (int) p2.y, (int) p3.y};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }

    private Point2D.Double projectPoint(Point3D point) {
        // Преобразуем точку в координаты камеры
        Point3D cameraPoint = camera.getPoint();
        Vector3D cameraZ = camera.getVectorZ();
        Vector3D cameraX = camera.getVectorX();
        Vector3D cameraY = camera.getVectorY();

        // Вектор от камеры к точке
        Vector3D viewVector = new Vector3D(cameraPoint, point);

        // Проецируем на оси камеры
        double xCam = viewVector.getDelta().getX() * cameraX.getEnd().getX() +
                viewVector.getDelta().getY() * cameraX.getEnd().getY() +
                viewVector.getDelta().getZ() * cameraX.getEnd().getZ();

        double yCam = viewVector.getDelta().getX() * cameraY.getEnd().getX() +
                viewVector.getDelta().getY() * cameraY.getEnd().getY() +
                viewVector.getDelta().getZ() * cameraY.getEnd().getZ();

        double zCam = viewVector.getDelta().getX() * cameraZ.getEnd().getX() +
                viewVector.getDelta().getY() * cameraZ.getEnd().getY() +
                viewVector.getDelta().getZ() * cameraZ.getEnd().getZ();

        // Применяем перспективную проекцию
        double distance = camera.getProjectDistance(getHeight());
        double factor = distance / (distance + zCam);
        double x = factor < 0 ? -1 : xCam * factor + (double) getWidth() / 2;
        double y = factor < 0 ? -1 : getHeight() - (yCam * factor + (double) getHeight() / 2);
        return new Point2D.Double(x, y);
    }

    private double getCameraAngle(Polygon3D polygon) {
        Vector3D polygonNormal = UtilHelper.getNormal(polygon);
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

    private Color computeColor(Polygon3D polygon, Color color) {
        double intensity = 0;
        for (Light3D l : lights.values()) {
            double distance = UtilHelper.getLength(UtilHelper.getCenterPolygon(polygon), l.getPoint()) / 100;
            intensity += computeIntensityLight(polygon, l) / distance;
        }
        if (intensity > 255) intensity = 255;

        double red = (double) color.getRed() / 255;
        double green = (double) color.getGreen() / 255;
        double blue = (double) color.getBlue() / 255;

        return new Color((int) (red * intensity), (int) (green * intensity), (int) (blue * intensity), color.getAlpha());
    }

    private double computeIntensityLight(Polygon3D polygon, Light3D light) {
        Point3D pCenter = UtilHelper.getCenterPolygon(polygon);
        Vector3D pn = UtilHelper.getNormal(polygon);
        Vector3D l = new Vector3D(light.getPoint(), pCenter);
        double angle = UtilHelper.getAngleRad(pn, l);
        return Math.max(lights.size() * 10, Math.cos(angle) * light.getIntensity());
    }

    private void renderLights(Graphics2D g2d) {
        for (Light3D light : lights.values()) {
            Point2D.Double p = projectPoint(light.getPoint());
            if (!isVisiblePoint(p)) continue;
            double distance = UtilHelper.getLength(camera.getPoint(), light.getPoint());
            int r = (int) (light.getIntensity() * 100 / distance);
            if (r > 50) r = 50;
            g2d.setColor(Color.WHITE);
            for (int i = 0; i < 360; i += 30) {
                int x1 = (int) p.x;
                int y1 = (int) p.y;
                int x2 = x1 + (int) (Math.cos(Math.toRadians(i)) * r);
                int y2 = y1 + (int) (Math.sin(Math.toRadians(i)) * r);
                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }
}
