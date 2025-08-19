package ru.avdonin.engine3d.util;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.UtilHelper;

import java.util.Objects;

@Getter
@Setter
public class Point3D {
    private double x;
    private double y;
    private double z;

    public Point3D() {
        this(0.0, 0.0, 0.0);
    }

    public Point3D(Point3D p) {
        this(p.x, p.y, p.z);
    }

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void move(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void move(Point3D p) {
        this.move(p.x, p.y, p.z);
    }

    public void translate(Double dx, Double dy, Double dz) {
        this.x += dx;
        this.y += dy;
        this.z += dz;
    }

    public void translate(Point3D dP) {
        this.translate(dP.x, dP.y, dP.z);
    }

    public void translate(Vector3D v) {
        this.translate(v.getDelta());
    }

    public void rotationRad(Point3D point, Vector3D normal, double angle){
        if (normal.getLength() < 1e-10) return;

        Point3D axis = normal.getDelta();
        double ax = axis.getX();
        double ay = axis.getY();
        double az = axis.getZ();

        double length = Math.sqrt(ax*ax + ay*ay + az*az);
        if (length < 1e-10) return;

        double nx = ax / length;
        double ny = ay / length;
        double nz = az / length;

        double vx = this.x - point.getX();
        double vy = this.y - point.getY();
        double vz = this.z - point.getZ();

        double dot = nx * vx + ny * vy + nz * vz;

        double crossX = ny * vz - nz * vy;
        double crossY = nz * vx - nx * vz;
        double crossZ = nx * vy - ny * vx;

        double cosA = Math.cos(angle);
        double sinA = Math.sin(angle);

        double newX = vx * cosA + crossX * sinA + nx * dot * (1 - cosA);
        double newY = vy * cosA + crossY * sinA + ny * dot * (1 - cosA);
        double newZ = vz * cosA + crossZ * sinA + nz * dot * (1 - cosA);

        this.x = point.getX() + newX;
        this.y = point.getY() + newY;
        this.z = point.getZ() + newZ;
    }

    public void rotation(Point3D point, Vector3D normal, double angle) {
        rotationRad(point, normal, UtilHelper.getRadians(angle));
    }

    public double dot(Point3D other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Point3D cross(Point3D other) {
        double x = this.y * other.z - this.z * other.y;
        double y = this.z * other.x - this.x * other.z;
        double z = this.x * other.y - this.y * other.x;
        return new Point3D(x, y, z);
    }

    public Point3D normalized() {
        double length = Math.sqrt(x*x + y*y + z*z);
        if (length < 1e-10) return new Point3D();
        return new Point3D(x/length, y/length, z/length);
    }

    public Point3D subtract(Point3D other) {
        return new Point3D(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point3D = (Point3D) o;
        return Double.compare(x, point3D.x) == 0 && Double.compare(y, point3D.y) == 0 && Double.compare(z, point3D.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Point3D{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
