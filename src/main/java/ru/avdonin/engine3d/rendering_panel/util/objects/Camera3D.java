package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.rendering_panel.helpers.UtilHelper;
import ru.avdonin.engine3d.rendering_panel.util.Obj;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Getter
public class Camera3D implements Obj<Camera3D> {
    public final static Basis DEFAULT_BASIS = new Basis();
    public final static Point3D DEFAULT_POINT = new Point3D();
    public final static double DEFAULT_ZOOM = 1;
    public final static double DEFAULT_VIEWING_ANGLE = Math.PI / 2;

    private final Basis basis;
    @Setter
    private double zoom = DEFAULT_ZOOM;
    private double viewingAngle = DEFAULT_VIEWING_ANGLE;

    public Camera3D() {
        this.basis = DEFAULT_BASIS;
    }

    public Camera3D(Point3D p, Vector3D v) {
        this.basis = new Basis(p, v);
    }

    public Camera3D(Vector3D v) {
        this.basis = new Basis(v.getStart(), v);
    }

    @Override
    public void move(Point3D p) {
        this.basis.move(p);
    }

    @Override
    public void move(Camera3D camera3D) {
        Point3D point = camera3D.getPoint();
        move(point);
    }

    @Override
    public void translate(Vector3D v) {
        this.basis.translate(v);
    }

    @Override
    public void rotationRad(Point3D point, Vector3D vector, double angle) {
        this.basis.rotationRad(point, vector, angle);
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public Point3D getPoint() {
        return basis.getPoint();
    }

    @Override
    public JFrame getCreateFrame() {
        return null;
    }

    public double getViewingAngle() {
        return Math.toDegrees(viewingAngle);
    }

    public double getViewingAngleRad() {
        return viewingAngle;
    }

    public void setViewingAngle(double x) {
        this.viewingAngle = Math.toRadians(x);
    }

    public void setViewingAngleRad(double x) {
        this.viewingAngle = x;
    }

    public double getProjectDistance(int height) {
        double angle = viewingAngle / 2;
        double h = (double) height / 2;
        double tan = Math.tan(angle);
        return h / tan;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!basis.equals(DEFAULT_BASIS))
            builder.append("basis=").append(basis).append("\n");
        if (zoom != DEFAULT_ZOOM)
            builder.append("zoom=").append(zoom).append("\n");
        if (viewingAngle != DEFAULT_VIEWING_ANGLE)
            builder.append("viewingAngle=").append(viewingAngle);
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "basis" -> basis.writeObject(value);
            case "zoom" -> zoom = Double.parseDouble(value);
            case "viewingAngle" -> viewingAngle = Double.parseDouble(value);
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        String[] lines = obj.split("\n");
        for (String line : lines) {
            String[] l = line.split("=");
            setValue(l[0], l[1]);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Camera3D camera3D = (Camera3D) o;
        return Double.compare(zoom, camera3D.zoom) == 0
                && Double.compare(viewingAngle, camera3D.viewingAngle) == 0
                && Objects.equals(basis, camera3D.basis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(basis, zoom, viewingAngle);
    }
}
