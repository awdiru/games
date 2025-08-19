package ru.avdonin.engine3d;

import lombok.Getter;
import ru.avdonin.engine3d.helpers.UtilHelper;
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

        for (Map.Entry<String, Object3D> entry : space.entrySet()) {
            Object3D obj = entry.getValue();
            for (Polygon3D polygon : obj.getPolygons())
                renderPolygon(g2d, polygon);
        }
    }

    private void renderPolygon(Graphics2D g2d, Polygon3D polygon) {
        Point2D.Double p1 = projectPoint(polygon.getP1());
        Point2D.Double p2 = projectPoint(polygon.getP2());
        Point2D.Double p3 = projectPoint(polygon.getP3());



        if (isPolygonVisible(polygon)) {
            g2d.setColor(new Color(255 - angle, 255 - angle, 255 - angle));
            int[] xPoints = {(int) p1.x, (int) p2.x, (int) p3.x};
            int[] yPoints = {(int) p1.y, (int) p2.y, (int) p3.y};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }

    private Point2D.Double projectPoint(Point3D point3D) {
        double distance = camera.getProjectDistance(getHeight()); // Расстояние до плоскости проекции

        double factor = distance / (distance + point3D.getZ());
        double x = point3D.getX() * factor + (double) getWidth() / 2;
        double y = point3D.getY() * factor + (double) getHeight() / 2;
        return new Point2D.Double(x, y);
    }

    private boolean isPolygonVisible(Polygon3D polygon) {
        Vector3D nV = UtilHelper.getNormal(polygon);
        double angle = UtilHelper.getAngle(nV, camera.getVector());
        return angle < 90;
    }
}
