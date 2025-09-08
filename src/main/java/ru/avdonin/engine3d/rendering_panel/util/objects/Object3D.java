package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.menu_panels.left.helpers.SavedHelper;
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
        for(Polygon3D p : o.polygons) addPolygon(new Polygon3D(p));
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
    public void move(Object3D object3D) {
        Point3D point = object3D.getPoint();
        this.move(point);
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
    public String getString(int count) {

        StringBuilder builder = new StringBuilder();
        builder.append("[");
        String splitter = SavedHelper.getStringSplitter(++count);

        if (!point.equals(new Point3D()))
            builder.append(splitter).append("startPoint=").append(point.getString(count));

        if (!color.equals(DEFAULT_COLOR))
            builder.append(splitter).append("color=").append(SavedHelper.getColorStr(color));

        for (Point3D p : points)
            builder.append(splitter).append("point=").append(p.getString(count));

        for (Polygon3D p : polygons)
            builder.append(splitter).append("polygon=").append(p.getString(count));

        builder.append(SavedHelper.getStringSplitter(--count)).append("]");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "color" -> color = SavedHelper.getColor(value);
            case "startPoint" -> {
                Point3D p = new Point3D();
                p.writeObject(value);
                points.add(p);
                this.point.move(p);
            }
            case "point" -> {
                Point3D p = new Point3D();
                p.writeObject(value);
                points.add(p);
            }
            case "polygon" -> {
                Polygon3D p = new Polygon3D();
                p.writeObject(value);
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
