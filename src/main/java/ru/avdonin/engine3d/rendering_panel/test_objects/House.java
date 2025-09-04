package ru.avdonin.engine3d.rendering_panel.test_objects;

import ru.avdonin.engine3d.rendering_panel.util.objects.Object3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Polygon3D;

import java.awt.*;
import java.util.List;
import java.util.Set;

public class House extends Object3D {
    private final double size;
    public House(Point3D p, double size) {
        this(p, size, Color.WHITE);
    }
    public House(Point3D p, double size, Color color) {
        this.point = p;
        this.points.add(point);
        this.color = color;
        this.size = size;
        initPolygons();
    }

    /*
               p9
             /   \
            /    p10
           /    /  \\
          /    /    \\
        p6____/_____p7\
        | \  /      | \\
        |  p2__________p3
        |  |        |  |
        p5_|________p8 |
         \ |         \ |
           p1__________p4
    */

    private void initPolygons() {
        List<Point3D> points = initPoints();
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
        // нижняя грань
        Polygon3D p9 = new Polygon3D(points.get(1), points.get(5), points.get(8));
        Polygon3D p10 = new Polygon3D(points.get(1), points.get(8), points.get(4));
        // крыша слева
        Polygon3D p11 = new Polygon3D(points.get(2), points.get(9), points.get(6));
        Polygon3D p12 = new Polygon3D(points.get(2), points.get(10), points.get(9));
        // крыша справа
        Polygon3D p13 = new Polygon3D(points.get(3), points.get(7), points.get(9));
        Polygon3D p14 = new Polygon3D(points.get(3), points.get(9), points.get(10));
        // крыша спереди
        Polygon3D p15 = new Polygon3D(points.get(2), points.get(3), points.get(10));
        // крыша сзади
        Polygon3D p16 = new Polygon3D(points.get(6), points.get(9), points.get(7));

        Set<Polygon3D> polygons = Set.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16);
        for (Polygon3D pol :polygons) addPolygon(pol);
    }

    private List<Point3D> initPoints() {
        double s = size / 2;

        Point3D p1 = new Point3D(point);
        p1.move(new Point3D(p1.getX() - s, p1.getY() - s, p1.getZ() - s));

        Point3D p2 = new Point3D(p1);
        p2.move(new Point3D(p1.getX(), p1.getY() + size, p1.getZ()));

        Point3D p3 = new Point3D(p2);
        p3.move(new Point3D(p2.getX() + size, p2.getY(), p2.getZ()));

        Point3D p4 = new Point3D(p3);
        p4.move(new Point3D(p3.getX(), p3.getY() - size, p3.getZ()));

        Point3D p5 = new Point3D(p1);
        p5.move(new Point3D(p1.getX(), p1.getY(), p1.getZ() + size));

        Point3D p6 = new Point3D(p5);
        p6.move(new Point3D(p5.getX(), p5.getY() + size, p5.getZ()));

        Point3D p7 = new Point3D(p6);
        p7.move(new Point3D(p6.getX() + size, p6.getY(), p6.getZ()));

        Point3D p8 = new Point3D(p7);
        p8.move(new Point3D(p7.getX(), p7.getY() - size, p7.getZ()));

        Point3D p9 = new Point3D(p6);
        p9.move(new Point3D(p9.getX() + s, p9.getY() + s, p9.getZ()));

        Point3D p10 = new Point3D(p9);
        p10.move(new Point3D(p10.getX(), p10.getY(), p10.getZ() - size));

        return List.of(point, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
    }
}
