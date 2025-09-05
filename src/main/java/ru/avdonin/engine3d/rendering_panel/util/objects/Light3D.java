package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.SizeField;
import ru.avdonin.engine3d.rendering_panel.helpers.UtilHelper;
import ru.avdonin.engine3d.rendering_panel.util.Obj;
import ru.avdonin.engine3d.rendering_panel.util.Saved;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Getter
public class Light3D implements Obj<Light3D> {
    public final static Point3D DEFAULT_POINT = new Point3D();
    public final static Vector3D DEFAULT_VECTOR = new Vector3D(0, 0, 1);
    public final static int DEFAULT_INTENSITY = 500;
    public final static double DEFAULT_ANGLE = 90;
    public final static Color DEFAULT_COLOR = Color.WHITE;

    private final Point3D point = new Point3D();
    private int intensity;
    private Vector3D vector;
    private double angle;
    @Setter
    private Color color = Color.WHITE;

    public Light3D() {
        this(DEFAULT_POINT, DEFAULT_INTENSITY, DEFAULT_ANGLE, DEFAULT_VECTOR);
    }

    public Light3D(Point3D start) {
        this(start, DEFAULT_INTENSITY, DEFAULT_ANGLE, DEFAULT_VECTOR);
    }

    public Light3D(Point3D start, int intensity) {
        this(start, intensity, DEFAULT_ANGLE, DEFAULT_VECTOR);
    }

    public Light3D(Point3D start, int intensity, double angle) {
        this(start, intensity, angle, DEFAULT_VECTOR);
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
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (!point.equals(DEFAULT_POINT))
            builder.append("point=").append(point).append("\n");

        if (!vector.equals(DEFAULT_VECTOR))
            builder.append("vector=").append(vector).append("\n");

        if (intensity != DEFAULT_INTENSITY)
            builder.append("intensity=").append(intensity).append("\n");

        if (angle != DEFAULT_ANGLE)
            builder.append("angle=").append(angle).append("\n");

        if (!color.equals(DEFAULT_COLOR))
            builder.append("color=").append(Saved.getColorStr(color));

        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "point" -> point.writeObject(value);
            case "vector" -> vector.writeObject(value);
            case "intensity" -> intensity = Integer.parseInt(value);
            case "angle" -> angle = Double.parseDouble(value);
            case "color" -> color = Saved.getColor(value);
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        String[] lines = obj.split("\n");

        for (String line : lines) {
            String[] l = line.split("=");
            setValue(l[0], l[1]);
        }
    }


    @Override
    public JFrame getCreateFrame() {
        JFrame frame = Obj.super.getCreateFrame();
        frame.setTitle("New Light");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        CoordsPane p = new CoordsPane();
        CoordsPane v = new CoordsPane();
        ColorsPane c = new ColorsPane();
        DoubleField a = new DoubleField("Angle");
        DoubleField i = new DoubleField("Intensity");

        JButton button = new JButton("->");

        button.addActionListener(e -> {
            point.move(getPoint(p));
            vector.setP2(getPoint(v));
            setColor(getColor(c));
            angle = a.getValue();
            double intensity = i.getValue();
            this.intensity = (int) intensity;

            saveObject("Light", this);
            frame.dispose();
        });

        panel.add(new JLabel("point"));
        panel.add(p);
        panel.add(new JLabel("vector"));
        panel.add(v);
        panel.add(new JLabel("color"));
        panel.add(c);
        panel.add(new JLabel("angle"));
        panel.add(a);
        panel.add(new JLabel("intensity"));
        panel.add(i);
        panel.add(button);

        JScrollPane scroll = new JScrollPane(panel);
        frame.add(scroll);
        return frame;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Light3D light3D = (Light3D) o;
        return intensity == light3D.intensity
                && Double.compare(angle, light3D.angle) == 0
                && Objects.equals(point, light3D.point)
                && Objects.equals(vector, light3D.vector)
                && Objects.equals(color, light3D.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, intensity, vector, angle, color);
    }

    private static class DoubleField extends SizeField<Double> {
        public DoubleField(String name) {
            super(name);
        }

        @Override
        public Double getValue() {
            String value = getValueText();
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                return 0.0;
            }
        }

    }
}
