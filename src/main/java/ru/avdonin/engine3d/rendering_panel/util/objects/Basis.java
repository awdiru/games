package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import ru.avdonin.engine3d.rendering_panel.helpers.UtilHelper;
import ru.avdonin.engine3d.rendering_panel.util.Saved;

import java.util.Objects;

@Getter
public class Basis extends Point3D {
    public static final Vector3D DEFAULT_VECTOR_Z = new Vector3D(0, 0, 1);
    public static final Point3D DEFAULT_POINT = new Point3D();

    private final Vector3D vectorX;
    private final Vector3D vectorY;
    private final Vector3D vectorZ;

    public Basis() {
        this(DEFAULT_POINT, DEFAULT_VECTOR_Z);
    }

    public Basis(Point3D point, Vector3D vectorZ) {
        super(point);
        this.vectorX = new Vector3D();
        this.vectorY = new Vector3D();
        this.vectorZ = UtilHelper.getNormalVector(vectorZ);

        computeVectorX();
        computeVectorY();
    }

    @Override
    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        super.rotationRad(point, normal, angle);
        this.vectorX.rotationRad(new Point3D(), normal, angle);
        this.vectorY.rotationRad(new Point3D(), normal, angle);
        this.vectorZ.rotationRad(new Point3D(), normal, angle);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (!getPoint().equals(DEFAULT_POINT))
            builder.append(super.toString()).append(" ");
        if (!vectorZ.equals(DEFAULT_VECTOR_Z))
            builder.append(vectorZ);
        builder.append("]");

        String str = builder.toString();
        if (str.endsWith(" ]"))
            str = str.substring(0, str.length() - 2) + "]";

        return str;
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "x" -> x = Double.parseDouble(value);
            case "y" -> y = Double.parseDouble(value);
            case "z" -> z = Double.parseDouble(value);
            case "color" -> color = Saved.getColor(value);
            case "vectorZ" -> {
                Vector3D vector = new Vector3D();
                vector.writeObject(value);
                this.vectorZ.move(UtilHelper.getNormalVector(vector));
                computeVectorX();
                computeVectorY();
            }
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        String[] b = obj.split("\n");
        if (b.length != 1)
            throw new RuntimeException("Некорректная запись");

        String str = b[0].substring(1, b[0].length() - 1);

        String p = Saved.getSubString(str, '(', ')');
        if (p != null && !p.isBlank()) {
            super.writeObject(p);
            str = str.substring(p.length());
        }

        p = Saved.getSubString(str, '[', ']');
        if (p != null && !p.isBlank())
            vectorZ.writeObject(p);

        computeVectorX();
        computeVectorY();
    }

    private void computeVectorX() {
        Vector3D worldX = new Vector3D(1, 0, 0);

        double angle = UtilHelper.getAngleRad(vectorZ, worldX);

        double xx = Math.sin(angle);
        double yx = 0;
        double zx = Math.cos(angle);

        Vector3D vectorX = UtilHelper.getNormalVector(new Vector3D(xx, yx, zx));
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

        Vector3D vectorY = UtilHelper.getNormalVector(new Vector3D(xy, yy, zy));
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
}
