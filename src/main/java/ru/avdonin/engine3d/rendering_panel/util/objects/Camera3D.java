package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.SavedHelper;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

import java.awt.*;
import java.util.Objects;

@Getter
public class Camera3D extends AbstractObject3D<Camera3D>{
    public final static Basis DEFAULT_BASIS = new Basis();
    public final static double DEFAULT_ZOOM = 1;
    public final static double DEFAULT_VIEWING_ANGLE = Math.PI / 2;

    private final Basis basis;
    @Setter
    private double zoom = DEFAULT_ZOOM;
    private double viewingAngle = DEFAULT_VIEWING_ANGLE;

    public Camera3D() {
        this.basis = new Basis(DEFAULT_BASIS);
    }

    public Camera3D(Point3D p, Vector3D v) {
        this.basis = new Basis(p, v);
    }

    public Camera3D(Vector3D v) {
        this.basis = new Basis(v.getStart(), v);
    }

    public Camera3D(Camera3D camera3D) {
        this.basis = new Basis(camera3D.basis);
        this.zoom = camera3D.zoom;
        this.viewingAngle = camera3D.viewingAngle;
    }

    @Override
    public void move(Point3D p) {
        this.basis.move(p);
    }

    @Override
    public void copyOf(Camera3D camera3D) {
        basis.copyOf(camera3D.basis);
        zoom = camera3D.zoom;
        viewingAngle = camera3D.viewingAngle;
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
    public void openCreateFrame() {
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
    public String serialize(int count) {
        String indent = SavedHelper.getStringSplitter(count);
        String nextIndent = SavedHelper.getStringSplitter(++count);

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!basis.equals(DEFAULT_BASIS))
            builder.append(nextIndent).append("basis=").append(basis.serialize(count));

        if (zoom != DEFAULT_ZOOM)
            builder.append(nextIndent).append("zoom=[").append(zoom).append("]");

        if (viewingAngle != DEFAULT_VIEWING_ANGLE)
            builder.append(nextIndent).append("viewingAngle=[").append(viewingAngle).append("]");

        builder.append(indent).append("]");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "basis" -> basis.writeObject(value);
            case "zoom" -> zoom = Double.parseDouble(value);
            case "viewingAngle" -> viewingAngle = Double.parseDouble(value);
            default -> throw new RuntimeException("Некорректное название переменной " + key);
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
