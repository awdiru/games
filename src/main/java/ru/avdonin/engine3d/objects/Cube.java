package ru.avdonin.engine3d.objects;

import ru.avdonin.engine3d.util.Object3D;
import ru.avdonin.engine3d.util.Point3D;
import ru.avdonin.engine3d.util.Polygon3D;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class Cube extends Object3D {

    public Cube(Point3D p, double size) {
        this(p, size, Color.WHITE);
    }
    public Cube(Point3D p, double size, Color color) {
        this.point.move(p);
        this.color = color;
        initPolygons(size);
    }
    /*
        p6______p7
        |   p2_______p3
        |   |   |    |
        p5__|___p8   |
            p1_______p4
    */

    private void initPolygons(double size) {
        List<Point3D> points = initPoints(size);
        // передняя грань
        Polygon3D p1 = new Polygon3D(points.get(4), points.get(2), points.get(1));
        Polygon3D p2 = new Polygon3D(points.get(4), points.get(3), points.get(2));
        // задняя грань
        Polygon3D p3 = new Polygon3D(points.get(8), points.get(6), points.get(7));
        Polygon3D p4 = new Polygon3D(points.get(8), points.get(5), points.get(6));
        // левая грань
        Polygon3D p5 = new Polygon3D(points.get(1), points.get(6), points.get(5));
        Polygon3D p6 = new Polygon3D(points.get(1), points.get(2), points.get(6));
        // правая грань
        Polygon3D p7 = new Polygon3D(points.get(4), points.get(8), points.get(7));
        Polygon3D p8 = new Polygon3D(points.get(4), points.get(7), points.get(3));
        // верхняя грань
        Polygon3D p9 = new Polygon3D(points.get(2), points.get(7), points.get(6));
        Polygon3D p10 = new Polygon3D(points.get(2), points.get(3), points.get(7));
        // нижняя грань
        Polygon3D p11 = new Polygon3D(points.get(1), points.get(5), points.get(8));
        Polygon3D p12 = new Polygon3D(points.get(1), points.get(8), points.get(4));

        Set<Polygon3D> polygons = Set.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);

        for (Polygon3D pol : polygons) {
            pol.setParent(this);
            addPolygon(pol);
        }
    }


    private List<Point3D> initPoints(double size) {
        double s = size / 2;

        Point3D p1 = new Point3D(point);
        p1.move(p1.getX() - s, p1.getY() - s, p1.getZ() - s);

        Point3D p2 = new Point3D(p1);
        p2.move(p1.getX(), p1.getY() + size, p1.getZ());

        Point3D p3 = new Point3D(p2);
        p3.move(p2.getX() + size, p2.getY(), p2.getZ());

        Point3D p4 = new Point3D(p3);
        p4.move(p3.getX(), p3.getY() - size, p3.getZ());

        Point3D p5 = new Point3D(p1);
        p5.move(p1.getX(), p1.getY(), p1.getZ() + size);

        Point3D p6 = new Point3D(p5);
        p6.move(p5.getX(), p5.getY() + size, p5.getZ());

        Point3D p7 = new Point3D(p6);
        p7.move(p6.getX() + size, p6.getY(), p6.getZ());

        Point3D p8 = new Point3D(p7);
        p8.move(p7.getX(), p7.getY() - size, p7.getZ());

        return List.of(point, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
