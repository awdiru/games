package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.rendering_panel.helpers.UtilHelper;
import ru.avdonin.engine3d.rendering_panel.util.Obj;

import javax.swing.*;
import java.awt.*;

@Getter
public class Light3D implements Obj<Light3D> {
    private final Point3D point = new Point3D();
    private int intensity;
    private Vector3D vector;
    private double angle;
    @Setter
    private Color color = Color.WHITE;

    public Light3D() {
        this(new Point3D());
    }

    public Light3D(Point3D start) {
        this(start, 0);
    }

    public Light3D(Point3D start, int intensity) {
        this(start, intensity, 90);
    }

    public Light3D(Point3D start, int intensity, double angle) {
        this(start, intensity, angle, new Vector3D(0, 0, 1));
    }

    public Light3D(Point3D start, int intensity, double angle, Vector3D vector) {
        this.point.move(start);
        this.intensity = intensity;
        this.angle = angle;
        this.vector = UtilHelper.getNormalVector(vector);
    }

    @Override
    public void move(Point3D p) {
        Vector3D vector = new Vector3D(point, p);
        this.translate(vector);
    }

    @Override
    public void move(Light3D light3D) {
        Point3D point = light3D.getPoint();
        this.point.move(point);
    }

    @Override
    public void translate(Vector3D v) {
        point.translate(v);
    }

    @Override
    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        this.vector.rotationRad(new Point3D(), normal, angle);
        this.point.rotationRad(point, normal, angle);
    }

    @Override
    public JFrame getCreateFrame() {
        return null;
    }

    public void setIntensity(int intensity) {
        this.intensity = Math.max(intensity, 0);
    }

    public void setVector(Vector3D vector) {
        this.vector = UtilHelper.getNormalVector(vector);
    }

    public void setAngleRad(double angle) {
        if (angle >= 0)
            this.angle = angle % (Math.PI * 2);
        else this.angle = -angle % (Math.PI * 2);
    }

    public void setAngle(double angle) {
        this.setAngleRad(Math.toRadians(angle));
    }

    @Override
    public String getString() {
        return "point=" + point.getString() + "\n" +
                "vector=" + vector.getString() + "\n" +
                "intensity=" + intensity + "\n" +
                "angle=" + angle;
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "point" -> point.writeObject(value);
            case "vector" -> vector.writeObject(value);
            case "intensity" -> intensity = Integer.parseInt(value);
            case "angle" -> angle = Double.parseDouble(value);
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        String[] lines = obj.split("\n");
        if (lines.length != 4)
            throw new RuntimeException("Некорректная запись");

        for (String line : lines) {
            String[] l = line.split("=");
            setValue(l[0], l[1]);
        }
    }
}
