package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.menu_panels.left.helpers.MenuHelper;
import ru.avdonin.engine3d.menu_panels.left.helpers.JFrameHelper;
import ru.avdonin.engine3d.menu_panels.left.helpers.SavedHelper;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.menu_panels.left.helpers.VectorHelper;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

@Getter
@Setter
public class Edge3D extends AbstractObject3D<Edge3D> {
    public static final Color DEFAULT_COLOR = Color.WHITE;

    protected Point3D p1;
    protected Point3D p2;
    protected Color color = Color.WHITE;
    protected AbstractObject3D<?> parent;

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
    public String getString (int count) {

        StringBuilder builder = new StringBuilder();
        builder.append("[");

        String splitter = SavedHelper.getStringSplitter(++count);
        if (!p1.equals(new Point3D()) && !p2.equals(new Point3D()))
            builder.append(splitter).append("p1=").append(p1.getString(count))
                    .append(splitter).append("p2=").append(p2.getString(count));

        if (!color.equals(DEFAULT_COLOR))
            builder.append(splitter).append("color=").append(SavedHelper.getColorStr(color));

        builder.append(SavedHelper.getStringSplitter(--count)).append("]");
        return builder.toString();
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "p1" -> p1.writeObject(value);
            case "p2" -> p2.writeObject(value);
            case "color" -> color = SavedHelper.getColor(value);
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
