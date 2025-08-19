package ru.avdonin.engine3d.util;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.UtilHelper;

@Getter
public class Camera3D {
    private final Vector3D vector;
    private final Point3D point;
    @Setter
    private double zoom = 1;
    private double viewingAngle = Math.PI / 2;

    public Camera3D(Point3D p, Vector3D v) {
        this.vector = getNormalVector(v, p);
        this.point = p;
    }

    public Camera3D(Vector3D v) {
        this.vector = getNormalVector(v);
        this.point = v.getStart();
    }

    public void move(Point3D p) {
        this.vector.move(getNormalVector(vector, p));
    }

    public void translate(Vector3D v) {
        this.vector.translate(v);
    }

    public void rotationRad(Point3D p, Vector3D normal, double angle) {
        this.vector.rotationRad(p, normal, angle);
    }

    public void rotation(Point3D p, Vector3D normal, double angle) {
        rotationRad(p, normal, UtilHelper.getRadians(angle));
    }

    public void setlViewingAngle(double x) {
        this.viewingAngle = UtilHelper.getRadians(x);
    }

    public void setViewingAngleRad(double x) {
        this.viewingAngle = x;
    }

    public double getProjectDistance(int height) {
        double angle = viewingAngle / 2;
        double h = (double) height / 2;
        double tan = Math.tan(angle);
        return h / tan;
    }

    private Vector3D getNormalVector(Vector3D v, Point3D p) {
        double dx = v.getStart().getX() - p.getX();
        double dy = v.getStart().getY() - p.getY();
        double dz = v.getStart().getZ() - p.getZ();

        double xe = v.getEnd().getX() - dx;
        double ye = v.getEnd().getY() - dy;
        double ze = v.getEnd().getZ() - dz;

        double length = v.getLength();

        double x = p.getX() + (xe - p.getX()) / length;
        double y = p.getY() + (ye - p.getY()) / length;
        double z = p.getZ() + (ze - p.getZ()) / length;

        return new Vector3D(p, new Point3D(x, y, z));
    }

    private Vector3D getNormalVector(Vector3D v) {
        double xs = v.getStart().getX();
        double ys = v.getStart().getY();
        double zs = v.getStart().getZ();

        double length = v.getLength();

        double xe = xs + (v.getEnd().getX() - xs) / length;
        double ye = ys + (v.getEnd().getY() - ys) / length;
        double ze = zs + (v.getEnd().getZ() - zs) / length;

        return new Vector3D(v.getStart(), new Point3D(xe, ye, ze));
    }
}
