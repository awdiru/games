package ru.avdonin.engine3d.util;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.*;

@Getter
@Setter
public abstract class Object3D {
    protected final Set<Polygon3D> polygons = new HashSet<>();
    protected final Point3D point = new Point3D();
    protected final Set<Point3D> points = new HashSet<>(Set.of(point));
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
        for (Polygon3D p: this.polygons) {
            this.points.add(p.getP1());
            this.points.add(p.getP2());
            this.points.add(p.getP3());
        }
    }

    public void move(Point3D p) {
        Vector3D v = new Vector3D(point, p);
        for (Point3D po : points)
            po.translate(v);
    }

    public void translate(Vector3D v) {
        for (Point3D p: points)
            p.translate(v);
    }

    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        for (Point3D p: points)
            p.rotationRad(point, normal, angle);
    }

    public void rotation(Point3D point, Vector3D normal, double angle) {
        rotationRad(point, normal, Math.toRadians(angle));
    }

    protected void addPolygon(Polygon3D pol) {
        polygons.add(pol);
        points.add(pol.getP1());
        points.add(pol.getP2());
        points.add(pol.getP3());

        if (point.equals(new Point3D()))
            point.move(pol.getP1());
    }
}
