package ru.avdonin.engine3d.test_objects;

import ru.avdonin.engine3d.rendering_panel.util.objects.Object3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Polygon3D;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class Plane extends Object3D {

    public Plane() {
        this(new Point3D(), 200);
    }

    public Plane(Point3D p, double size) {
        this(p, size, Color.WHITE);
    }

    public  Plane(Point3D p, double size, Color color) {
        this.point = p;
        this.points.add(point);
        this.color = color;
        initPolygons(size);
    }

    /*
    p1-----p2
     \      \
      p4-----p3
     */

    private void initPolygons(double size) {
        List<Point3D> points = initPoints(size);

        Polygon3D p1 = new Polygon3D(points.get(1), points.get(3), points.get(2));
        Polygon3D p2 = new Polygon3D(points.get(1), points.get(4), points.get(3));

        Set<Polygon3D> polygons = Set.of(p1, p2);

        for (Polygon3D pol : polygons) {
            pol.setParent(this);
            addPolygon(pol);
        }
    }

    private List<Point3D> initPoints(double size) {
        double s = size / 2;

        Point3D p1 = new Point3D(point);
        p1.move(new Point3D( p1.getX() - s, p1.getY(), p1.getZ() + s));

        Point3D p2 = new Point3D(p1);
        p2.move(new Point3D( p2.getX() + size, p2.getY(), p2.getZ()));

        Point3D p3 = new Point3D(p2);
        p3.move(new Point3D( p3.getX(), p3.getY(), p3.getZ() - size));

        Point3D p4 = new Point3D(p3);
        p4.move(new Point3D( p4.getX() - size, p4.getY(), p4.getZ()));

        return List.of(point, p1, p2, p3, p4);
    }
}
