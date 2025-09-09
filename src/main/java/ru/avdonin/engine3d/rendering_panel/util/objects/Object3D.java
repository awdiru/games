package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.SerializeHelper;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

import java.awt.*;
import java.util.*;

@Getter
@Setter
public class Object3D extends AbstractObject3D<Object3D> {
    public static final Color DEFAULT_COLOR = Color.WHITE;

    protected final Set<Polygon3D> polygons = new HashSet<>();
    protected final Set<Point3D> points = new HashSet<>();
    protected Point3D point = new Point3D();
    protected Color color = DEFAULT_COLOR;

    public Object3D() {
        this.points.add(point);
    }

    public Object3D(Object3D o) {
        this.points.add(point);
        this.color = o.color;
        for (Polygon3D p : o.polygons) addPolygon(new Polygon3D(p));
    }

    public Object3D(Set<Polygon3D> polygons) {
        this.points.add(point);
        for (Polygon3D p : polygons) addPolygon(p);
    }

    public Object3D(Color color, Polygon3D... polygons) {
        this.color = color;
        for (Polygon3D p : polygons) {
            addPolygon(p);
        }
    }

    @Override
    public void move(Point3D p) {
        Vector3D v = new Vector3D(new Point3D(point), new Point3D(p));
        for (Point3D po : points)
            po.translate(v);
    }

    @Override
    public void copyOf(Object3D object3D) {
        point.copyOf(object3D.point);

        points.clear();
        for (Point3D point : object3D.points)
            points.add(new Point3D(point));

        polygons.clear();
        for (Polygon3D polygon : object3D.polygons)
            addPolygon(polygon);

        color = object3D.color;
    }

    @Override
    public void translate(Vector3D v) {
        for (Point3D p : points)
            p.translate(v);
    }

    @Override
    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        for (Point3D p : points)
            p.rotationRad(point, normal, angle);
        for (Polygon3D p : polygons)
            p.calculateNormal();
    }

    @Override
    public void openCreateFrame() {
    }

    protected void addPolygon(Polygon3D pol) {
        pol.setParent(this);
        polygons.add(pol);

        for (Point3D p : points) {
            if (pol.getP1().equals(p)) pol.setP1(p);
            if (pol.getP2().equals(p)) pol.setP2(p);
            if (pol.getP3().equals(p)) pol.setP3(p);
        }

        points.add(pol.getP1());
        points.add(pol.getP2());
        points.add(pol.getP3());

        if (point.equals(new Point3D()))
            point.move(pol.getP1());
    }

    @Override
    public String serialize(int count) {
        String indent = SerializeHelper.getStringSplitter(count);
        String nextIndent = SerializeHelper.getStringSplitter(++count);

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!color.equals(DEFAULT_COLOR))
            builder.append(nextIndent).append("color=").append(SerializeHelper.serializeColor(color));

        if (!point.equals(new Point3D()))
            builder.append(nextIndent).append("startPoint=").append(point.serialize(count));

        for (Point3D p : points)
            builder.append(nextIndent).append("point=").append(p.serialize(count));

        for (Polygon3D p : polygons)
            builder.append(nextIndent).append("polygon=").append(p.serialize(count));

        builder.append(indent).append("]");
        return builder.toString();
    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!color.equals(DEFAULT_COLOR))
            builder.append("color=").append(SerializeHelper.serializeColor(color));

        if (!point.equals(new Point3D()))
            builder.append("startPoint=").append(point.serialize());

        for (Point3D p : points)
            builder.append("point=").append(p.serialize());

        for (Polygon3D p : polygons)
            builder.append("polygon=").append(p.serialize());

        builder.append("]");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "color" -> color = SerializeHelper.deserializeColor(value);
            case "startPoint" -> {
                Point3D p = new Point3D();
                p.deserialize(value);
                points.add(p);
                this.point.move(p);
            }
            case "point" -> {
                Point3D p = new Point3D();
                p.deserialize(value);
                points.add(p);
            }
            case "polygon" -> {
                Polygon3D p = new Polygon3D();
                p.deserialize(value);
                addPolygon(p);
            }
            default -> throw new RuntimeException("Некорректное название переменной " + key);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Object3D object3D = (Object3D) o;
        return Objects.equals(polygons, object3D.polygons)
                && Objects.equals(points, object3D.points)
                && Objects.equals(point, object3D.point)
                && Objects.equals(color, object3D.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(polygons, points, point, color);
    }
}
