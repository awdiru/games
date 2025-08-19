package ru.avdonin.engine3d.util;

public class Vector3D extends Edge3D{

    public Vector3D(Point3D start, Point3D end) {
        this.p1 = start;
        this.p2 = end;
    }

    public Point3D getStart() {
        return p1;
    }

    public Point3D getEnd() {
        return p2;
    }

    public Point3D getDelta() {
        return new Point3D(
                getEnd().getX() - getStart().getX(),
                getEnd().getY() - getStart().getY(),
                getEnd().getZ() - getStart().getZ()
        );
    }

    @Override
    public String toString() {
        return "Vector3D{" +
                "start=" + p1 +
                ", end=" + p2 +
                '}';
    }
}
