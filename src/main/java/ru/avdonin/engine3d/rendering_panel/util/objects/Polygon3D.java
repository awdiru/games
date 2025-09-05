package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.rendering_panel.util.Obj;
import ru.avdonin.engine3d.rendering_panel.util.Saved;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Getter
@Setter
public class Polygon3D implements Saved, Obj<Polygon3D> {
    private Point3D p1;
    private Point3D p2;
    private Point3D p3;

    private final Edge3D edge1;
    private final Edge3D edge2;
    private final Edge3D edge3;

    private boolean isReflection = false;

    private Object3D parent;

    private Color color = Color.WHITE;

    public Polygon3D() {
        this(new Point3D(), new Point3D(), new Point3D());
    }

    public Polygon3D(Polygon3D pol) {
        this(pol.p1, pol.p2, pol.p3);
    }

    public Polygon3D(Point3D p1, Point3D p2, Point3D p3) {
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        this.edge1 = new Edge3D(p1, p2);
        this.edge2 = new Edge3D(p2, p3);
        this.edge3 = new Edge3D(p3, p1);

        this.edge1.setParent(this);
        this.edge3.setParent(this);
        this.edge2.setParent(this);
    }

    @Override
    public void move(Point3D p) {
        Vector3D vector = new Vector3D(p1, p);
        this.translate(vector);
    }

    @Override
    public void move(Polygon3D pol) {
        this.p1.move(pol.p1);
        this.p2.move(pol.p2);
        this.p3.move(pol.p3);
    }

    @Override
    public void translate(Vector3D v) {
        this.p1.translate(v);
        this.p1.translate(v);
        this.p1.translate(v);
    }

    @Override
    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        this.p1.rotationRad(point, normal, angle);
        this.p2.rotationRad(point, normal, angle);
        this.p3.rotationRad(point, normal, angle);
    }

    public Color getColor() {
        if (parent == null)
            return color;
        return parent.getColor();
    }

    @Override
    public Point3D getPoint() {
        return p1;
    }

    @Override
    public JFrame getCreateFrame() {
        JFrame frame = Obj.super.getCreateFrame();
        frame.setTitle("New Polygon");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        CoordsPane coord1 = new CoordsPane();
        CoordsPane coord2 = new CoordsPane();
        CoordsPane coord3 = new CoordsPane();
        ColorsPane color = new ColorsPane();
        JButton button = new JButton("->");
        button.addActionListener(e -> {
            p1.move(getPoint(coord1));
            p2.move(getPoint(coord2));
            p3.move(getPoint(coord3));
            setColor(getColor(color));
            saveObject("polygon", this);
            frame.dispose();
        });

        panel.add(new JLabel("p1"));
        panel.add(coord1);
        panel.add(new JLabel("p2"));
        panel.add(coord2);
        panel.add(new JLabel("p3"));
        panel.add(coord3);
        panel.add(new JLabel("color"));
        panel.add(color);
        panel.add(button);

        JScrollPane scroll = new JScrollPane(panel);
        frame.add(scroll);

        return frame;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("[")
                .append(p1.toString()).append(" ")
                .append(p2.toString()).append(" ")
                .append(p3.toString());

        if(!color.equals(Color.WHITE))
            builder.append(" ").append(Saved.getColorStr(color));

        builder.append(" ").append(isReflection).append("]");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "p1" -> p1.writeObject(value);
            case "p2" -> p2.writeObject(value);
            case "p3" -> p3.writeObject(value);
            case "color" -> color = Saved.getColor(value);
            case "isReflection" -> isReflection = Boolean.parseBoolean(value);
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        String[] arr = obj.split("\n");
        if (arr.length != 1)
            throw new RuntimeException("Некорректная запись");
        String pol = arr[0];
        if (!pol.startsWith("[") || !pol.endsWith("]"))
            throw new RuntimeException("Некорректная запись");

        String str = pol.substring(1, pol.length() - 1);

        String p = Saved.getSubString(str, '(', ')');
        if (p != null && !p.isBlank()) {
            setValue("p1", p);
            str = str.substring(p.length() + 1);
        }
        p = Saved.getSubString(str, '(', ')');
        if (p != null && !p.isBlank()) {
            setValue("p2", p);
            str = str.substring(p.length() + 1);
        }
        p = Saved.getSubString(str, '(', ')');
        if (p != null && !p.isBlank()) {
            setValue("p3", p);
            str = str.substring(p.length() + 1);
        }
        p = Saved.getSubString(str, '[', ']');
        if (p != null && !p.isBlank()) {
            setValue("color", p);
            str = str.substring(p.length() + 1);
        }
        p = str;
        if (!p.isBlank()) {
            p = p.substring(1, p.length() - 1);
            setValue("isReflection", p);
        }
    }

    public void setP1(Point3D p1) {
        this.p1 = p1;
        this.edge1.setP1(p1);
        this.edge3.setP2(p1);
    }

    public void setP2(Point3D p2) {
        this.p2 = p2;
        this.edge2.setP1(p2);
        this.edge1.setP2(p2);
    }

    public void setP3(Point3D p3) {
        this.p3 = p3;
        this.edge3.setP1(p3);
        this.edge2.setP2(p3);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Polygon3D polygon3D = (Polygon3D) o;
        return isReflection == polygon3D.isReflection
                && Objects.equals(p1, polygon3D.p1)
                && Objects.equals(p2, polygon3D.p2)
                && Objects.equals(p3, polygon3D.p3)
                && Objects.equals(color, polygon3D.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2, p3, isReflection, color);
    }
}
