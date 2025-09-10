package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.*;
import ru.avdonin.engine3d.menu_panels.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Getter
@Setter
public class Edge3D extends AbstractObject3D<Edge3D> {
    protected Point3D p1;
    protected Point3D p2;
    protected AbstractObject3D<?> parent;

    public Edge3D() {
        this(new Point3D(), new Point3D());
    }

    public Edge3D(Edge3D e) {
        this(new Point3D(e.p1), new Point3D(e.p2));
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
    public void copyOf(Edge3D edge3D) {
        p1.move(edge3D.p1);
        p2.move(edge3D.p2);
        color = edge3D.color;
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
        return parent == null ? color : parent.getColor();
    }

    @Override
    public Point3D getPoint() {
        return p1;
    }

    @Override
    public void openCreateFrame() {
        JFrame frame = JFrameHelper.createFrame();
        frame.setTitle("New Edge");

        JPanel panel = JFrameHelper.createPanel();

        CoordsPane coord1 = new CoordsPane();
        CoordsPane coord2 = new CoordsPane();
        ColorsPane color = new ColorsPane();

        JButton button = new JButton("->");
        button.addActionListener(e -> {
            p1.move(MenuHelper.getPoint(coord1));
            p2.move(MenuHelper.getPoint(coord2));
            setColor(MenuHelper.getColor(color));
            String name = (this instanceof Vector3D ? "vector" : "edge");
            SavedHelper.addObjectToScene(name, this);
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
    }

    @Override
    public String serialize(int count) {
        String indent = SerializeHelper.getStringSplitter(count);
        String nextIndent = SerializeHelper.getStringSplitter(++count);

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!p1.equals(new Point3D()) && !p2.equals(new Point3D()))
            builder.append(nextIndent).append("p1=").append(p1.serialize(count))
                    .append(nextIndent).append("p2=").append(p2.serialize(count));

        if (!color.equals(DEFAULT_COLOR))
            builder.append(nextIndent).append("color=").append(SerializeHelper.serializeColor(color));

        builder.append(indent).append("]");
        return builder.toString();
    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!p1.equals(new Point3D()) && !p2.equals(new Point3D()))
            builder.append("p1=").append(p1.serialize())
                    .append("p2=").append(p2.serialize());

        if (!color.equals(DEFAULT_COLOR))
            builder.append("color=").append(SerializeHelper.serializeColor(color));

        builder.append("]");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "p1" -> p1.deserialize(value);
            case "p2" -> p2.deserialize(value);
            case "color" -> color = SerializeHelper.deserializeColor(value);
            default -> throw new RuntimeException("Некорректное название переменной " + key);
        }
    }

    public double getLength() {
        return VectorHelper.getLength(p1, p2);
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
