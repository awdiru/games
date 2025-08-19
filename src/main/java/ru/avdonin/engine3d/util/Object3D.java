package ru.avdonin.engine3d.util;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.UtilHelper;

import java.util.*;

@Getter
@Setter
public class Object3D {
    protected final Set<Polygon3D> polygons = new HashSet<>();
    protected final Set<Point3D> points = new HashSet<>();

    public Object3D() {
    }

    public Object3D(Object3D o) {
        this(o.getPolygons());
    }

    public Object3D(Polygon3D... polygons) {
        this.polygons.addAll(Arrays.asList(polygons));
        for (Polygon3D p: this.polygons) {
            this.points.add(p.getP1());
            this.points.add(p.getP2());
            this.points.add(p.getP3());
        }
    }

    public Object3D(Set<Polygon3D> polygons) {
        this.polygons.addAll(polygons);
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
        rotationRad(point, normal, UtilHelper.getRadians(angle));
    }

    protected void addPolygon(Polygon3D pol) {
        polygons.add(pol);
        points.add(pol.getP1());
        points.add(pol.getP2());
        points.add(pol.getP3());
    }
}
