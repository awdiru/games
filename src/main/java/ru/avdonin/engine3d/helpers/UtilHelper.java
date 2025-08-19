package ru.avdonin.engine3d.helpers;

import ru.avdonin.engine3d.util.Point3D;
import ru.avdonin.engine3d.util.Polygon3D;
import ru.avdonin.engine3d.util.Vector3D;

public class UtilHelper {
    public static double getLength(Point3D p1, Point3D p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        double dz = p1.getZ() - p2.getZ();
        return Math.sqrt(((dx * dx) + (dy * dy) + (dz * dz)));
    }

    public static double getRadians(double x) {
        return Math.PI * (x / 180);
    }

    public static double getDegrees(double x) {
        return x * (180 / Math.PI);
    }

    public static Vector3D getNormal(Point3D p1, Point3D p2, Point3D p3) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double z1 = p1.getZ();

        double x2 = p2.getX();
        double y2 = p2.getY();
        double z2 = p2.getZ();

        double x3 = p3.getX();
        double y3 = p3.getY();
        double z3 = p3.getZ();

        double nx = (y2 - y1) * (z3 - z1) - (z2 - z1) * (y3 - y1);
        double ny = (z2 - z1) * (x3 - x1) - (x2 - x1) * (z3 - z1);
        double nz = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);

        return new Vector3D(new Point3D(), new Point3D(nx, ny, nz));
    }

    public static Vector3D getNormal(Polygon3D p) {
        return getNormal(p.getP1(), p.getP2(), p.getP3());
    }

    public static double getAngleRad(Vector3D v1, Vector3D v2) {
        Vector3D nv1 = getNormalVector(v1);
        Vector3D nv2 = getNormalVector(v2);

        double x1 = nv1.getEnd().getX();
        double y1 = nv1.getEnd().getY();
        double z1 = nv1.getEnd().getZ();

        double x2 = nv2.getEnd().getX();
        double y2 = nv2.getEnd().getY();
        double z2 = nv2.getEnd().getZ();

        double m = x1 * x2 + y1 * y2 + z1 * z2;
        return Math.acos(m);
    }

    public static double getAngle(Vector3D v1, Vector3D v2) {
        double angle = getAngleRad(v1, v2);
        return getDegrees(angle);
    }

    public static Vector3D getNormalVector(Vector3D v) {
        double dx = v.getStart().getX();
        double dy = v.getStart().getY();
        double dz = v.getStart().getZ();

        double x = v.getEnd().getX() - dx;
        double y = v.getEnd().getY() - dy;
        double z = v.getEnd().getZ() - dz;

        double length = v.getLength();

        double xe = x / length;
        double ye = y / length;
        double ze = z / length;

        return new Vector3D(new Point3D(), new Point3D(xe, ye, ze));
    }

    public static Point3D getCenterPolygon(Polygon3D p) {

    }
}
