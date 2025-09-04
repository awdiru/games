package ru.avdonin.engine3d.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.util.Obj;
import ru.avdonin.engine3d.util.Saved;

import java.awt.*;
import java.util.*;
import java.util.List;

@Getter
@Setter
public class Object3D implements Obj<Object3D> {
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
        this.polygons.addAll(Arrays.asList(polygons));
        for (Polygon3D p : this.polygons) {
            this.points.add(p.getP1());
            this.points.add(p.getP2());
            this.points.add(p.getP3());
        }
    }

    @Override
    public void move(Point3D p) {
        Vector3D v = new Vector3D(point, p);
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

    protected void addPolygon(Polygon3D pol) {
        pol.setParent(this);
        polygons.add(pol);

        if (!points.contains(pol.getP1()))
            points.add(pol.getP1());
        if (!points.contains(pol.getP2()))
            points.add(pol.getP2());
        if (!points.contains(pol.getP3()))
            points.add(pol.getP3());

        if (point.equals(new Point3D()))
            point.move(pol.getP1());
    }

    @Override
    public String getString() {
        StringBuilder builder = new StringBuilder();
        builder.append("color=").append(Saved.getColorStr(color)).append("\n");
        for (int i = 0; i < points.size(); i++)
            builder.append("p").append(i).append("=").append(points.get(i).getString()).append("\n");

        for (Polygon3D pol : polygons)
            builder.append(pol.getString()).append("\n");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "color" -> color = Saved.getColor(value);
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        String[] lines = obj.split("\n");

        int count = 0;
        for (; !lines[count].startsWith("p"); count++) {
            String[] val = lines[count].split("=");
            setValue(val[0], val[1]);
        }
        for (; !lines[count].startsWith("["); count++) {
            String p = lines[count].substring(lines[count].indexOf("=") + 1);
            Point3D point = new Point3D();
            point.writeObject(p);
            points.add(point);
        }
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
}
