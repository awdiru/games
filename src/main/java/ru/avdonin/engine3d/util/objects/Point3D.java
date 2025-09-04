package ru.avdonin.engine3d.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.util.Obj;
import ru.avdonin.engine3d.util.Saved;

import java.awt.*;
import java.util.Objects;

@Getter
@Setter
public class Point3D implements Obj<Point3D> {
    private double x;
    private double y;
    private double z;
    private Color color = Color.WHITE;
    private Obj<?> parent;

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

    @Override
    public void move(Point3D p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    @Override
    public void translate(Vector3D v) {
        Point3D dP = v.getDelta();
        this.x += dP.x;
        this.y += dP.y;
        this.z += dP.z;
    }

    @Override
    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        if (normal.getLength() < 1e-10) return;

        Point3D axis = normal.getDelta();
        double ax = axis.getX();
        double ay = axis.getY();
        double az = axis.getZ();

        double length = Math.sqrt(ax * ax + ay * ay + az * az);
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
        if (color.equals(Color.WHITE))
            return "(" + x + ", " + y + ", " + z + ")";
        return "(" + x + ", " + y + ", " + z + ", " + Saved.getColorStr(color) + ")";
    }

    @Override
    public String getString() {
        return toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "x" -> x = Double.parseDouble(value);
            case "y" -> y = Double.parseDouble(value);
            case "z" -> z = Double.parseDouble(value);
            case "color" -> color = Saved.getColor(value);
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        String[] arr = obj.split("\n");
        if (arr.length != 1)
            throw new RuntimeException("Некорректная запись");
        String point = arr[0];

        if (!point.startsWith("(") || !point.endsWith(")"))
            throw new RuntimeException("Некорректная запись");

        String str = point.substring(1, point.length() - 1);
        String[] array = str.split(", ");

        if (array.length >= 3) {
            setValue("x", array[0]);
            setValue("y", array[1]);
            setValue("z", array[2]);
        }
        if (array.length == 4)
            setValue("color", array[3]);
        else if (array.length != 3) throw new RuntimeException("Некорректная запись");
    }

    public Color getColor() {
        if (parent == null)
            return color;
        return parent.getColor();
    }
}
