package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import ru.avdonin.engine3d.helpers.SavedHelper;
import ru.avdonin.engine3d.helpers.VectorHelper;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

import java.awt.*;
import java.util.Objects;

@Getter
public class Basis extends AbstractObject3D<Basis> {
    public static final Vector3D DEFAULT_VECTOR_Z = new Vector3D(0, 0, 1);
    public static final Point3D DEFAULT_POINT = new Point3D();

    private final Point3D point;
    private final Vector3D vectorX;
    private final Vector3D vectorY;
    private final Vector3D vectorZ;

    public Basis() {
        this(DEFAULT_POINT, DEFAULT_VECTOR_Z);
    }

    public Basis(Basis basis) {
        this.point = new Point3D(basis.point);
        this.vectorX = new Vector3D(basis.vectorX);
        this.vectorY = new Vector3D(basis.vectorY);
        this.vectorZ = new Vector3D(basis.vectorZ);
    }

    public Basis(Point3D point, Vector3D vectorZ) {
        this.point = new Point3D(point);
        this.vectorX = new Vector3D();
        this.vectorY = new Vector3D();
        this.vectorZ = VectorHelper.getNormalVector(vectorZ);

        computeVectorX();
        computeVectorY();
    }

    @Override
    public void move(Point3D p) {
        point.move(p);
    }

    @Override
    public void copyOf(Basis basis) {
        point.move(basis.point);
        vectorX.copyOf(basis.vectorX);
        vectorY.copyOf(basis.vectorY);
        vectorZ.copyOf(basis.vectorZ);
    }

    @Override
    public void translate(Vector3D v) {
        this.point.translate(v);
    }

    @Override
    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        this.point.rotationRad(point, normal, angle);
        this.vectorX.rotationRad(new Point3D(), normal, angle);
        this.vectorY.rotationRad(new Point3D(), normal, angle);
        this.vectorZ.rotationRad(new Point3D(), normal, angle);
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public String serialize(int count) {
        String indent = SavedHelper.getStringSplitter(count);
        String nextIndent = SavedHelper.getStringSplitter(++count);

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!getPoint().equals(DEFAULT_POINT))
            builder.append(nextIndent).append("point=").append(point.serialize(count));

        if (!vectorZ.equals(DEFAULT_VECTOR_Z))
            builder.append(nextIndent).append("vectorZ=").append(vectorZ.serialize(count));

        builder.append(indent).append("]");

        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "point" -> point.writeObject(value);
            case "vectorZ" -> {
                Vector3D vector = new Vector3D();
                vector.writeObject(value);
                this.vectorZ.translate(VectorHelper.getNormalVector(vector));
                computeVectorX();
                computeVectorY();
            }
            default -> throw new RuntimeException("Некорректное название переменной " + key);
        }
    }

    private void computeVectorX() {
        Vector3D worldX = new Vector3D(1, 0, 0);

        double angle = VectorHelper.getAngleRad(vectorZ, worldX);

        double xx = Math.sin(angle);
        double yx = 0;
        double zx = Math.cos(angle);

        Vector3D vectorX = VectorHelper.getNormalVector(new Vector3D(xx, yx, zx));
        this.vectorX.copyOf(vectorX);
    }

    private void computeVectorY() {
        double xz = vectorZ.getEnd().getX();
        double yz = vectorZ.getEnd().getY();
        double zz = vectorZ.getEnd().getZ();

        double xx = vectorX.getEnd().getX();
        double yx = vectorX.getEnd().getY();
        double zx = vectorX.getEnd().getZ();

        double xy = yz * zx - zz * yx;
        double yy = zz * xx - xz * zx;
        double zy = xz * yx - yz * xx;

        Vector3D vectorY = VectorHelper.getNormalVector(new Vector3D(xy, yy, zy));
        this.vectorY.copyOf(vectorY);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Basis basis = (Basis) o;
        return Objects.equals(point, basis.point)
                && Objects.equals(vectorX, basis.vectorX)
                && Objects.equals(vectorY, basis.vectorY)
                && Objects.equals(vectorZ, basis.vectorZ);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, vectorX, vectorY, vectorZ);
    }

    @Override
    public void openCreateFrame() {

    }
}
