package ru.avdonin.engine3d.helpers;

import ru.avdonin.engine3d.util.objects.Edge3D;
import ru.avdonin.engine3d.util.objects.Point3D;
import ru.avdonin.engine3d.util.objects.Polygon3D;
import ru.avdonin.engine3d.util.objects.Vector3D;

public class UtilHelper {
    public static double getLength(Point3D p1, Point3D p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        double dz = p1.getZ() - p2.getZ();
        return Math.sqrt(((dx * dx) + (dy * dy) + (dz * dz)));
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

        return new Vector3D(nx, ny, nz);
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
        m = Math.max(-1.0, Math.min(1.0, m));
        return Math.acos(m);
    }

    public static double getAngle(Vector3D v1, Vector3D v2) {
        double angle = getAngleRad(v1, v2);
        return Math.toDegrees(angle);
    }

    public static Vector3D getNormalVector(Vector3D v) {
        Point3D d = v.getDelta();

        double length = v.getLength();

        if (length < 1e-10)
            return new Vector3D(0, 0, 0);

        double xe = d.getX() / length;
        double ye = d.getY() / length;
        double ze = d.getZ() / length;

        return new Vector3D(xe, ye, ze);
    }

    public static Vector3D changeLenVector(Vector3D vector, double newLength) {
        double dx = vector.getEnd().getX() - vector.getStart().getX();
        double dy = vector.getEnd().getY() - vector.getStart().getY();
        double dz = vector.getEnd().getZ() - vector.getStart().getZ();

        Point3D start = vector.getStart();

        double length = vector.getLength();

        if (length < 1e-10)
            return new Vector3D(vector.getStart(), vector.getEnd());

        double xe = (dx / length) * newLength + start.getX();
        double ye = (dy / length) * newLength + start.getY();
        double ze = (dz / length) * newLength + start.getZ();

        Vector3D v = new Vector3D(vector.getStart(), new Point3D(xe, ye, ze));
        v.setColor(vector.getColor());
        return v;
    }

    public static Point3D getCenterPolygon(Polygon3D p) {
        Point3D p1 = p.getP1();
        Point3D p2 = p.getP2();
        Point3D p3 = p.getP3();

        double x = (p1.getX() + p2.getX() + p3.getX()) / 3;
        double y = (p1.getY() + p2.getY() + p3.getY()) / 3;
        double z = (p1.getZ() + p2.getZ() + p3.getZ()) / 3;

        return new Point3D(x, y, z);
    }

    public static Point3D calculateCollision(Polygon3D polygon, Edge3D edge) {
        Point3D p1 = polygon.getP1();
        Point3D p2 = polygon.getP2();
        Point3D p3 = polygon.getP3();

        Point3D e1 = edge.getP1();
        Point3D e2 = edge.getP2();

        Vector3D normal = UtilHelper.getNormal(polygon);

        double a = normal.getEnd().getX();
        double b = normal.getEnd().getY();
        double c = normal.getEnd().getZ();
        double d = -(a * p1.getX() + b * p1.getY() + c * p1.getZ());

        double dx = e2.getX() - e1.getX();
        double dy = e2.getY() - e1.getY();
        double dz = e2.getZ() - e1.getZ();

        double denominator = (a * dx + b * dy + c * dz);

        if (Math.abs(denominator) < 1e-10) return null;

        double numerator = -(a * e1.getX() + b * e1.getY() + c * e1.getZ() + d);
        double t = numerator / denominator;

        if (t < 0 || t > 1) return null;

        double xp = e1.getX() + (t * dx);
        double yp = e1.getY() + (t * dy);
        double zp = e1.getZ() + (t * dz);

        Point3D intersectionPoint = new Point3D(xp, yp, zp);

        Vector3D v0 = new Vector3D(p2.getX() - p1.getX(), p2.getY() - p1.getY(), p2.getZ() - p1.getZ());
        Vector3D v1 = new Vector3D(p3.getX() - p1.getX(), p3.getY() - p1.getY(), p3.getZ() - p1.getZ());
        Vector3D v2 = new Vector3D(intersectionPoint.getX() - p1.getX(),
                intersectionPoint.getY() - p1.getY(),
                intersectionPoint.getZ() - p1.getZ());

        double dot00 = v0.dot(v0);
        double dot01 = v0.dot(v1);
        double dot02 = v0.dot(v2);
        double dot11 = v1.dot(v1);
        double dot12 = v1.dot(v2);

        double invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return (u >= 0) && (v >= 0) && (u + v <= 1) ? intersectionPoint : null;
    }

}
