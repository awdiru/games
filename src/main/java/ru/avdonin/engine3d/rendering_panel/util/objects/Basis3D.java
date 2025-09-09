package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import ru.avdonin.engine3d.helpers.SerializeHelper;
import ru.avdonin.engine3d.helpers.VectorHelper;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

import java.awt.*;
import java.util.Objects;

@Getter
public class Basis3D extends AbstractObject3D<Basis3D> {
    public static final Vector3D DEFAULT_VECTOR_Z = new Vector3D(0, 0, 1);
    public static final Point3D DEFAULT_POINT = new Point3D();

    private final Point3D point;
    private final Vector3D vectorX;
    private final Vector3D vectorY;
    private final Vector3D vectorZ;

    public Basis3D() {
        this(DEFAULT_POINT, DEFAULT_VECTOR_Z);
    }

    public Basis3D(Basis3D basis) {
        this.point = new Point3D(basis.point);
        this.vectorX = new Vector3D(basis.vectorX);
        this.vectorY = new Vector3D(basis.vectorY);
        this.vectorZ = new Vector3D(basis.vectorZ);
    }

    public Basis3D(Point3D point, Vector3D vectorZ) {
        this.point = new Point3D(point);
        this.vectorX = new Vector3D();
        this.vectorY = new Vector3D();
        this.vectorZ = VectorHelper.normalizeVector(vectorZ);

        computeVectorX();
        computeVectorY();
    }

    @Override
    public void move(Point3D p) {
        point.move(p);
    }

    @Override
    public void copyOf(Basis3D basis) {
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
        String indent = SerializeHelper.getStringSplitter(count);
        String nextIndent = SerializeHelper.getStringSplitter(++count);

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
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!getPoint().equals(DEFAULT_POINT))
            builder.append("point=").append(point.serialize());

        if (!vectorZ.equals(DEFAULT_VECTOR_Z))
            builder.append("vectorZ=").append(vectorZ.serialize());

        builder.append("]");

        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "point" -> point.deserialize(value);
            case "vectorZ" -> {
                Vector3D vector = new Vector3D();
                vector.deserialize(value);
                this.vectorZ.translate(VectorHelper.normalizeVector(vector));
                computeVectorX();
                computeVectorY();
            }
            default -> throw new RuntimeException("Некорректное название переменной " + key);
        }
    }

    private void computeVectorX() {
        Vector3D worldY = new Vector3D(0, 1, 0);
        Vector3D vectorX = worldY.cross(vectorZ);
        vectorX = VectorHelper.normalizeVector(vectorX);
        this.vectorX.copyOf(vectorX);
    }

    private void computeVectorY() {
        Vector3D vectorY = vectorZ.cross(vectorX);
        vectorY = VectorHelper.normalizeVector(vectorY);
        this.vectorY.copyOf(vectorY);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Basis3D basis = (Basis3D) o;
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

    public void setVectorZ(Vector3D vector) {
        Vector3D v = VectorHelper.normalizeVector(vector);
        vectorZ.copyOf(v);
        computeVectorX();
        computeVectorY();
    }
}
