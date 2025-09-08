package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.MenuHelper;
import ru.avdonin.engine3d.helpers.JFrameHelper;
import ru.avdonin.engine3d.helpers.SavedHelper;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.SizeField;
import ru.avdonin.engine3d.helpers.VectorHelper;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Getter
public class Light3D extends AbstractObject3D<Light3D> {
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

    public Light3D(Light3D light3D) {
        point.copyOf(light3D.point);
        vector.copyOf(light3D.vector);
        intensity = light3D.intensity;
        color = light3D.color;
        angle = light3D.angle;
    }

    public Light3D(Point3D start, int intensity, double angle, Vector3D vector) {
        this.point.move(start);
        this.intensity = intensity;
        this.angle = angle;
        this.vector = VectorHelper.getNormalVector(vector);
    }

    @Override
    public void move(Point3D p) {
        Vector3D vector = new Vector3D(point, p);
        this.translate(vector);
    }

    @Override
    public void copyOf(Light3D light3D) {
        point.copyOf(light3D.point);
        vector.copyOf(light3D.vector);
        intensity = light3D.intensity;
        color = light3D.color;
        angle = light3D.angle;
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
        this.vector = VectorHelper.getNormalVector(vector);
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
    public String serialize(int count) {
        String indent = SavedHelper.getStringSplitter(count);
        String nextIndent = SavedHelper.getStringSplitter(++count);

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!point.equals(DEFAULT_POINT))
            builder.append(nextIndent).append("point=").append(point.serialize(count));

        if (!vector.equals(DEFAULT_VECTOR))
            builder.append(nextIndent).append("vector=").append(vector.serialize(count));

        if (intensity != DEFAULT_INTENSITY)
            builder.append(nextIndent).append("intensity=[").append(intensity).append("]");

        if (angle != DEFAULT_ANGLE)
            builder.append(nextIndent).append("angle=[").append(angle).append("]");

        if (!color.equals(DEFAULT_COLOR))
            builder.append(nextIndent).append("color=").append(SavedHelper.getColorStr(color));

        builder.append(indent).append("]");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "point" -> point.writeObject(value);
            case "vector" -> vector.writeObject(value);
            case "intensity" -> intensity = Integer.parseInt(value.substring(1, value.length() - 1));
            case "angle" -> angle = Double.parseDouble(value.substring(1, value.length() - 1));
            case "color" -> color = SavedHelper.getColor(value);
            default -> throw new RuntimeException("Некорректное название переменной " + key);
        }
    }

    @Override
    public void openCreateFrame() {
        JFrame frame = JFrameHelper.createFrame();
        frame.setTitle("New Light");

        JPanel panel = JFrameHelper.createPanel();
        CoordsPane p = new CoordsPane();
        CoordsPane v = new CoordsPane();
        ColorsPane c = new ColorsPane();
        DoubleField a = new DoubleField("Angle");
        DoubleField i = new DoubleField("Intensity");

        JButton button = new JButton("->");

        button.addActionListener(e -> {
            point.move(MenuHelper.getPoint(p));
            vector.setP2(MenuHelper.getPoint(v));
            setColor(MenuHelper.getColor(c));
            angle = a.getValue();
            double intensity = i.getValue();
            this.intensity = (int) intensity;

            SavedHelper.addObjectToScene("Light", this);
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
