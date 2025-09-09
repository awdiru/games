package ru.avdonin.engine3d.rendering_panel.util.objects;

import ru.avdonin.engine3d.helpers.SerializeHelper;
import ru.avdonin.engine3d.helpers.VectorHelper;

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

    public Vector3D cross(Vector3D other){
        double xz = this.getEnd().getX();
        double yz = this.getEnd().getY();
        double zz = this.getEnd().getZ();

        double xx = other.getEnd().getX();
        double yx = other.getEnd().getY();
        double zx = other.getEnd().getZ();

        double xy = yz * zx - zz * yx;
        double yy = zz * xx - xz * zx;
        double zy = xz * yx - yz * xx;

        return new Vector3D(xy, yy, zy);
    }

    @Override
    public String serialize(int count) {
        String indent = SerializeHelper.getStringSplitter(count);
        String nextIndent = SerializeHelper.getStringSplitter(++count);

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!getStart().equals(new Point3D()))
            builder.append(nextIndent).append("start=").append(p1.serialize(count));

        builder.append(nextIndent).append("end=").append(p2.serialize(count));

        if (!color.equals(DEFAULT_COLOR))
            builder.append(nextIndent).append("color=").append(SerializeHelper.serializeColor(color));

        builder.append(indent).append("]");
        return builder.toString();
    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!getStart().equals(new Point3D()))
            builder.append("start=").append(p1.serialize());

        builder.append("end=").append(p2.serialize());

        if (!color.equals(DEFAULT_COLOR))
            builder.append("color=").append(SerializeHelper.serializeColor(color));

        builder.append("]");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "start" -> p1.deserialize(value);
            case "end" -> p2.deserialize(value);
            case "color" -> color = SerializeHelper.deserializeColor(value);
            default -> throw new RuntimeException("Некорректное название переменной " + key);
        }
    }

    @Override
    public void openCreateFrame() {
        super.openCreateFrame();
    }
}
