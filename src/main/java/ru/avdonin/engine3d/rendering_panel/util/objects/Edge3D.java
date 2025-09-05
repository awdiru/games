package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.rendering_panel.helpers.UtilHelper;
import ru.avdonin.engine3d.rendering_panel.util.Obj;
import ru.avdonin.engine3d.rendering_panel.util.Saved;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Getter
@Setter
public class Edge3D implements Obj<Edge3D> {
    public static final Color DEFAULT_COLOR = Color.WHITE;

    protected Point3D p1;
    protected Point3D p2;
    protected Color color = Color.WHITE;
    protected Obj<?> parent;

    public Edge3D() {
        this(new Point3D(), new Point3D());
    }

    public Edge3D(Edge3D e) {
        this(e.p1, e.p2);
    }

    public Edge3D(Point3D p1, Point3D p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void move(Point3D p) {
        Vector3D vector = new Vector3D(p1, p);
        this.translate(vector);
    }

    @Override
    public void move(Edge3D e) {
        this.p1.move(e.p1);
        this.p2.move(e.p2);
    }

    @Override
    public void translate(Vector3D v) {
        this.p1.translate(v);
        this.p2.translate(v);
    }

    @Override
    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        this.p1.rotationRad(point, normal, angle);
        this.p2.rotationRad(point, normal, angle);
    }

    @Override
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
        frame.setTitle("New Edge");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        CoordsPane coord1 = new CoordsPane();
        CoordsPane coord2 = new CoordsPane();
        ColorsPane color = new ColorsPane();

        JButton button = new JButton("->");
        button.addActionListener(e -> {
            p1.move(getPoint(coord1));
            p2.move(getPoint(coord2));
            setColor(getColor(color));
            String name = (this instanceof Vector3D ? "vector" : "edge");
            saveObject(name, this);
            frame.dispose();
        });

        panel.add(new JLabel("start"));
        panel.add(coord1);
        panel.add(new JLabel("end"));
        panel.add(coord2);
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
        builder.append("[");
        if(!p1.equals(new Point3D()) && !p2.equals(new Point3D()))
            builder.append(p1).append(" ").append(p2);
        if (!color.equals(DEFAULT_COLOR))
            builder.append(" ").append(Saved.getColorStr(color));
        builder.append("]");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "p1" -> p1.writeObject(value);
            case "p2" -> p2.writeObject(value);
            case "color" -> color = Saved.getColor(value);
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        String[] arr = obj.split("\n");
        if (arr.length != 1)
            throw new RuntimeException("Некорректная запись");
        String edge = arr[0];
        if (!edge.startsWith("[") || !edge.endsWith("]"))
            throw new RuntimeException("Некорректная запись");

        String str = edge.substring(1, edge.length() - 1);

        String p = Saved.getSubString(str, '(', ')');
        if (p != null && !p.isBlank()) {
            setValue("p1", p);
            str = str.substring(p.length());
        }

        p = Saved.getSubString(str, '(', ')');
        if (p != null && !p.isBlank()) {
            setValue("p2", p);
            str = str.substring(p.length());
        }

        p = Saved.getSubString(str, '[', ']');
        if (p != null && !p.isBlank())
            setValue("color", p);
    }

    public double getLength() {
        return UtilHelper.getLength(p1, p2);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Edge3D edge3D = (Edge3D) o;
        return Objects.equals(p1, edge3D.p1)
                && Objects.equals(p2, edge3D.p2)
                && Objects.equals(color, edge3D.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(p1, p2, color, parent);
    }
}
