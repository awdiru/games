package ru.avdonin.engine3d.rendering_panel.util.objects;

import ru.avdonin.engine3d.menu_panels.left.helpers.SavedHelper;
import ru.avdonin.engine3d.rendering_panel.util.Saved;

import javax.swing.*;

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
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!p1.equals(new Point3D()))
            builder.append(p1).append(" ");
        builder.append(p2);

        if (!color.equals(DEFAULT_COLOR))
            builder.append(" ").append(SavedHelper.getColorStr(color));
        builder.append("]");
        return builder.toString();
    }

    @Override
    public void writeObject(String obj) {
        String[] arr = obj.split("\n");
        if (arr.length != 1)
            throw new RuntimeException("Некорректная запись");
        String vector = arr[0];
        if (!vector.startsWith("[") || !vector.endsWith("]"))
            throw new RuntimeException("Некорректная запись");

        String str = vector.substring(1, vector.length() - 1);
        String p = SavedHelper.getSubString(str, '(', ')');
        if (p == null || p.isBlank())
            throw new RuntimeException("Некорректная запись");

        String pc = p;
        str = str.substring(p.length() + 1);
        p = SavedHelper.getSubString(str, '(', ')');
        if (p == null || p.isBlank())
            setValue("p2", pc);
        else {
            setValue("p1", pc);
            setValue("p2", p);
            str = str.substring(p.length() + 1);
        }
        p = SavedHelper.getSubString(str, '[', ']');
        if (p != null && !p.isBlank())
            setValue("color", p);
    }

    @Override
    public void getCreateFrame() {
        super.getCreateFrame();
    }
}
