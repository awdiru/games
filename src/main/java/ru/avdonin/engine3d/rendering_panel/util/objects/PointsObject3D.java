package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import ru.avdonin.engine3d.helpers.SerializeHelper;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
public class PointsObject3D extends AbstractObject3D<PointsObject3D> {
    private final Point3D center = new Point3D();
    private final Set<Point3D> points = new HashSet<>();

    public PointsObject3D() {
    }

    public PointsObject3D(Collection<Point3D> points) {
        for (Point3D p : points) {
            Point3D point = new Point3D(p);
            point.setParent(this);
            this.points.add(point);
        }
    }

    @Override
    public void move(Point3D p) {
        for (Point3D point : points) {
            point.move(p);
        }
    }

    @Override
    public void copyOf(PointsObject3D pointsObject3D) {
        points.clear();
        for (Point3D point : pointsObject3D.points) {
            Point3D p = new Point3D(point);
            p.setParent(this);
            points.add(p);
        }
    }

    @Override
    public void translate(Vector3D v) {
        for (Point3D point : points)
            point.translate(v);
    }

    @Override
    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        for (Point3D p : points)
            point.rotationRad(point, normal, angle);
    }

    @Override
    public Point3D getPoint() {
        return center;
    }

    @Override
    public void openCreateFrame() {
    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        for (Point3D p : points)
            builder.append(p.serialize());

        builder.append("]");
        return builder.toString();
    }

    @Override
    public String serialize(int count) {
        String indent = SerializeHelper.getStringSplitter(count);
        String nextIndent = SerializeHelper.getStringSplitter(++count);

        StringBuilder builder = new StringBuilder();
        builder.append("[").append(nextIndent).append("center=").append(center.serialize());
        for (Point3D p : points)
            builder.append(nextIndent).append("point=").append(p.serialize());

        builder.append(indent).append("]");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "point" -> {
                Point3D p = new Point3D();
                p.deserialize(value);
                points.add(p);
            }
            case "center" -> {
                Point3D p = new Point3D();
                p.deserialize(value);
                center.move(p);
            }
            default -> throw new RuntimeException("Некорректное название переменной " + key);
        }
    }
}
