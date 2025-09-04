package ru.avdonin.engine3d.util.objects;

public class Vector3D extends Edge3D {
    public Vector3D() {
        this(new Point3D(), new Point3D());
    }

    public Vector3D(Edge3D vector) {
        this(new Point3D(vector.getP1()), new Point3D(vector.getP2()));
        color = vector.getColor();
    }

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

    public double dot(Vector3D other) {
        return this.getDelta().getX() * other.getDelta().getX() +
                this.getDelta().getY() * other.getDelta().getY() +
                this.getDelta().getZ() * other.getDelta().getZ();
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
