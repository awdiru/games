package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.menu_panels.left.helpers.MenuHelper;
import ru.avdonin.engine3d.menu_panels.left.helpers.SavedHelper;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.rendering_panel.util.Obj;
import ru.avdonin.engine3d.rendering_panel.util.Saved;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Getter
@Setter
public class Point3D extends Obj<Point3D> {
    protected double x;
    protected double y;
    protected double z;
    protected Color color = Color.WHITE;
    protected Obj<?> parent;

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
    public String toString() {
        String str = "(" + x + ", " + y + ", " + z;
        if (color.equals(Color.WHITE))
            return str + ")";
        return str + ", " + SavedHelper.getColorStr(color) + ")";
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "x" -> x = Double.parseDouble(value);
            case "y" -> y = Double.parseDouble(value);
            case "z" -> z = Double.parseDouble(value);
            case "color" -> color = SavedHelper.getColor(value);
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

    @Override
    public Point3D getPoint() {
        return this;
    }

    @Override
    public void getCreateFrame() {
        JFrame frame = createFrame();
        frame.setTitle("New Point");

        JPanel panel = createPanel();
        CoordsPane coords = new CoordsPane();
        ColorsPane color = new ColorsPane();

        JButton button = new JButton("->");
        button.addActionListener(e -> {
            move(MenuHelper.getPoint(coords));
            setColor(MenuHelper.getColor(color));
            MenuHelper.saveObject("point", this);
            frame.dispose();
        });

        panel.add(new JLabel("Point"));
        panel.add(coords);
        panel.add(new JLabel("Color"));
        panel.add(color);
        panel.add(button);

        JScrollPane scroll = new JScrollPane(panel);
        frame.add(scroll);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Point3D point3D = (Point3D) o;
        return Double.compare(x, point3D.x) == 0
                && Double.compare(y, point3D.y) == 0
                && Double.compare(z, point3D.z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
