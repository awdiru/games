package ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.test_objects.obj;

import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Polygon3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.test_objects.TestObj;

import java.util.List;
import java.util.Set;

public class Plane extends TestObj {

    /*
    p1-----p2
     \      \
      p4-----p3
     */

    @Override
    protected Set<Polygon3D> initPolygons(Point3D p, double size) {
        List<Point3D> points = initPoints(p, size);

        Polygon3D p1 = new Polygon3D(points.get(1), points.get(3), points.get(2));
        Polygon3D p2 = new Polygon3D(points.get(1), points.get(4), points.get(3));

        return Set.of(p1, p2);
    }

    @Override
    protected List<Point3D>  initPoints(Point3D p, double size) {
        double s = size / 2;

        Point3D p1 = new Point3D(p);
        p1.move(new Point3D( p1.getX() - s, p1.getY(), p1.getZ() + s));

        Point3D p2 = new Point3D(p1);
        p2.move(new Point3D( p2.getX() + size, p2.getY(), p2.getZ()));

        Point3D p3 = new Point3D(p2);
        p3.move(new Point3D( p3.getX(), p3.getY(), p3.getZ() - size));

        Point3D p4 = new Point3D(p3);
        p4.move(new Point3D( p4.getX() - size, p4.getY(), p4.getZ()));

        return List.of(p, p1, p2, p3, p4);
    }
}
