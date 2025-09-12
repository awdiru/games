package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.*;
import ru.avdonin.engine3d.menu_panels.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.util_panels.input_panels.PointPane;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Getter
@Setter
public class Polygon3D extends AbstractObject3D<Polygon3D> {
    private Point3D p1;
    private Point3D p2;
    private Point3D p3;

    private final Edge3D edge1;
    private final Edge3D edge2;
    private final Edge3D edge3;

    private boolean isReflection = false;

    private final Vector3D normal = new Vector3D();

    private AbstractObject3D<?> parent;

    private Color color = null;

    public Polygon3D() {
        this(new Point3D(), new Point3D(), new Point3D());
    }

    public Polygon3D(Polygon3D pol) {
        this(new Point3D(pol.p1),
                new Point3D(pol.p2),
                new Point3D(pol.p3));
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

        calculateNormal();
    }

    @Override
    public void move(Point3D p) {
        Vector3D vector = new Vector3D(p1, p);
        this.translate(vector);
    }

    @Override
    public void copyOf(Polygon3D polygon3D) {
        p1.copyOf(polygon3D.p1);
        p2.copyOf(polygon3D.p2);
        p3.copyOf(polygon3D.p3);
        isReflection = polygon3D.isReflection;
        calculateNormal();
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
        calculateNormal();
    }

    public Color getColor() {
        if (parent == null && color == null)
            return Color.WHITE;

        if (parent == null || (color != null && !parent.getColor().equals(color)))
            return color;

        return parent.getColor();
    }

    @Override
    public Point3D getPoint() {
        return p1;
    }

    @Override
    public void openCreateFrame() {
        JFrame frame = JFrameHelper.createFrame();
        frame.setTitle("New Polygon");

        JPanel panel = JFrameHelper.createPanel();

        PointPane coord1 = new PointPane();
        PointPane coord2 = new PointPane();
        PointPane coord3 = new PointPane();
        ColorsPane color = new ColorsPane();
        JButton button = new JButton("->");
        button.addActionListener(e -> {
            p1.move(MenuHelper.getPoint(coord1));
            p2.move(MenuHelper.getPoint(coord2));
            p3.move(MenuHelper.getPoint(coord3));
            setColor(MenuHelper.getColor(color));
            SavedHelper.addObjectToScene("polygon", this);
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
    }

    @Override
    public String serialize(int count) {
        String indent = SerializeHelper.getStringSplitter(count);
        String nextIndent = SerializeHelper.getStringSplitter(++count);

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!p1.equals(new Point3D()))
            builder.append(nextIndent).append("p1=").append(p1.serialize(count));
        if (!p2.equals(new Point3D()))
            builder.append(nextIndent).append("p2=").append(p2.serialize(count));
        if (!p3.equals(new Point3D()))
            builder.append(nextIndent).append("p3=").append(p3.serialize(count));

        if (color != null && !color.equals(Color.WHITE))
            builder.append(nextIndent).append("color=").append(SerializeHelper.serializeColor(color));

        builder.append(nextIndent).append("isReflection=[").append(isReflection).append("]")
                .append(indent).append("]");

        return builder.toString();
    }

    @Override
    public String serialize() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");

        if (!p1.equals(new Point3D()))
            builder.append("p1=").append(p1.serialize());
        if (!p2.equals(new Point3D()))
            builder.append("p2=").append(p2.serialize());
        if (!p3.equals(new Point3D()))
            builder.append("p3=").append(p3.serialize());

        if (color != null && !color.equals(Color.WHITE))
            builder.append("color=").append(SerializeHelper.serializeColor(color));

        builder.append("isReflection=[").append(isReflection).append("]")
                .append("]");

        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "p1" -> p1.deserialize(value);
            case "p2" -> p2.deserialize(value);
            case "p3" -> p3.deserialize(value);
            case "color" -> color = SerializeHelper.deserializeColor(value);
            case "isReflection" -> isReflection = Boolean.parseBoolean(value);
            default -> throw new RuntimeException("Некорректное название переменной " + key);
        }
    }

    @Override
    public void deserialize(String obj) {
        super.deserialize(obj);
        calculateNormal();
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

    public void calculateNormal() {
        Vector3D normal = VectorHelper.getNormal(p1, p2, p3);
        normal = VectorHelper.normalizeVector(normal);
        this.normal.copyOf(normal);
    }
}
