package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.menu_panels.left.helpers.SavedHelper;
import ru.avdonin.engine3d.rendering_panel.util.Obj;
import ru.avdonin.engine3d.rendering_panel.util.Saved;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

@Getter
@Setter
public class Object3D extends Obj<Object3D> {
    public static final Color DEFAULT_COLOR = Color.WHITE;

    protected final Set<Polygon3D> polygons = new HashSet<>();
    protected final List<Point3D> points = new ArrayList<>();
    protected Point3D point;
    protected Color color;

    public Object3D() {
        this.color = Color.WHITE;
    }

    public Object3D(Object3D o) {
        this(o.getPolygons());
    }

    public Object3D(Set<Polygon3D> polygons) {
        this.polygons.addAll(polygons);
        this.color = Color.WHITE;
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
    public void getCreateFrame() {
    }

    protected void addPolygon(Polygon3D pol) {
        pol.setParent(this);
        polygons.add(pol);

        if (!points.contains(pol.getP1())) points.add(pol.getP1());
        if (!points.contains(pol.getP2())) points.add(pol.getP2());
        if (!points.contains(pol.getP3())) points.add(pol.getP3());

        if (point.equals(new Point3D())) point.move(pol.getP1());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (!color.equals(DEFAULT_COLOR))
            builder.append("color=").append(SavedHelper.getColorStr(color)).append("\n");

        for (int i = 0; i < points.size(); i++)
            builder.append("p").append(i).append("=").append(points.get(i)).append("\n");

        for (Polygon3D pol : polygons)
            builder.append(pol).append("\n");

        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "color" -> color = SavedHelper.getColor(value);
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        String[] lines = obj.split("\n");

        int count = 0;
        for (; !lines[count].startsWith("p0"); count++) {
            String[] val = lines[count].split("=");
            setValue(val[0], val[1]);
        }
        for (; !lines[count].startsWith("["); count++) {
            String p = lines[count].substring(lines[count].indexOf("=") + 1);
            Point3D point = new Point3D();
            point.writeObject(p);
            points.add(point);
        }
        point = points.getFirst();
        for (; count < lines.length; count++) {
            Polygon3D polygon = new Polygon3D();
            polygon.writeObject(lines[count]);

            int index1 = points.indexOf(polygon.getP1());
            if (index1 == -1) {
                points.add(polygon.getP1());
                index1 = points.size() - 1;
            }
            polygon.setP1(points.get(index1));

            int index2 = points.indexOf(polygon.getP2());
            if (index2 == -1) {
                points.add(polygon.getP2());
                index2 = points.size() - 1;
            }
            polygon.setP2(points.get(index2));

            int index3 = points.indexOf(polygon.getP3());
            if (index3 == -1) {
                points.add(polygon.getP3());
                index3 = points.size() - 1;
            }
            polygon.setP3(points.get(index3));

            polygon.setParent(this);
            polygons.add(polygon);
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
