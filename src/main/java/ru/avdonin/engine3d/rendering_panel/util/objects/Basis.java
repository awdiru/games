package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import ru.avdonin.engine3d.menu_panels.left.helpers.SavedHelper;
import ru.avdonin.engine3d.menu_panels.left.helpers.VectorHelper;
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

    public Basis(Point3D point, Vector3D vectorZ) {
        this.point = point;
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
    public void move(Basis basis) {
        Point3D point = basis.getPoint();
        this.point.move(point);
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
    public String getString(int count) {
        StringBuilder builder = new StringBuilder();

        builder.append("[");
        String splitter = SavedHelper.getStringSplitter(++count);

        if (!getPoint().equals(DEFAULT_POINT))
            builder.append(splitter).append("point=").append(point.getString(count));

        if (!vectorZ.equals(DEFAULT_VECTOR_Z))
            builder.append(splitter).append("vectorZ=").append(vectorZ.getString(count));

        builder.append(SavedHelper.getStringSplitter(--count)).append("]");

        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "point" -> point.writeObject(value);
            case "vectorZ" -> {
                Vector3D vector = new Vector3D();
                vector.writeObject(value);
                this.vectorZ.move(VectorHelper.getNormalVector(vector));
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
        this.vectorX.move(vectorX);
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
        this.vectorY.move(vectorY);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Basis basis = (Basis) o;
        return Objects.equals(vectorX, basis.vectorX)
                && Objects.equals(vectorY, basis.vectorY)
                && Objects.equals(vectorZ, basis.vectorZ);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vectorX, vectorY, vectorZ);
    }

    @Override
    public void openCreateFrame() {

    }
}
