package ru.avdonin.engine3d.util;

public class Vector3D extends Edge3D {

    public Vector3D(double x, double y, double z) {
        this(new Point3D(x, y, z));
    }

    public Vector3D(Point3D end) {
        super(new Point3D(), end);
    }

    public Vector3D(Point3D start, Point3D end) {
        super(start, end);
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
        if (!getStart().equals(new Point3D()))
            return "Vector{" +
                    "start" + p1 +
                    ", end" + p2 +
                    ", length = " + length +
                    "}";

        String v = "Vector{" + p2;

        if (length != 1)
            return v + ", length = " + length + "};";
        else return v + "};";
    }
}
