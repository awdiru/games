package ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.obj;

import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Polygon3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.TestObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class Sphere extends TestObj {
    private static final int DEFAULT_RINGS = 16;
    private static final int DEFAULT_SECTORS = 32;

    @Override
    protected Set<Polygon3D> initPolygons(Point3D p, double size) {
        List<Point3D> points = initPoints(p, size);
        Set<Polygon3D> polygons = new HashSet<>();
        int pointsPerRing = DEFAULT_SECTORS + 1;

        for (int i = 0; i < DEFAULT_RINGS; i++) {
            for (int j = 0; j < DEFAULT_SECTORS; j++) {
                int first = i * pointsPerRing + j;
                int second = first + 1;
                int third = (i + 1) * pointsPerRing + j;
                int fourth = third + 1;

                if (i > 0) {
                    polygons.add(new Polygon3D(
                            points.get(first),
                            points.get(third),
                            points.get(second)
                    ));
                }

                if (i < DEFAULT_RINGS - 1) {
                    polygons.add(new Polygon3D(
                            points.get(second),
                            points.get(third),
                            points.get(fourth)
                    ));
                }
            }
        }

        return polygons;
    }

    @Override
    protected List<Point3D> initPoints(Point3D p, double size) {
        List<Point3D> points = new ArrayList<>();
        double radius = size / 2;

        for (int i = 0; i <= DEFAULT_RINGS; i++) {
            double theta = i * Math.PI / DEFAULT_RINGS;
            double sinTheta = Math.sin(theta);
            double cosTheta = Math.cos(theta);

            for (int j = 0; j <= DEFAULT_SECTORS; j++) {
                double phi = j * 2 * Math.PI / DEFAULT_SECTORS;
                double sinPhi = Math.sin(phi);
                double cosPhi = Math.cos(phi);

                double x = cosPhi * sinTheta;
                double y = cosTheta;
                double z = sinPhi * sinTheta;

                Point3D point = new Point3D(
                        p.getX() + radius * x,
                        p.getY() + radius * y,
                        p.getZ() + radius * z
                );
                points.add(point);
            }
        }
        return points;
    }
}