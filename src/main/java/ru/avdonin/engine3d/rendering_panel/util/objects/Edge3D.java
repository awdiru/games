package ru.avdonin.engine3d.rendering_panel.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.menu_panels.left.util_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.CoordsPane;
import ru.avdonin.engine3d.rendering_panel.helpers.UtilHelper;
import ru.avdonin.engine3d.rendering_panel.util.Obj;
import ru.avdonin.engine3d.rendering_panel.util.Saved;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public class Edge3D implements Obj<Edge3D> {
    protected Point3D p1;
    protected Point3D p2;
    protected double length;
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
        this.length = calculateLength();
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
        this.length = calculateLength();
    }

    @Override
    public void translate(Vector3D v) {
        this.p1.translate(v);
        this.p2.translate(v);
        this.length = calculateLength();
    }

    @Override
    public void rotationRad(Point3D point, Vector3D normal, double angle){
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
            calculateLength();
            saveObject("edge");
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

    protected double calculateLength() {
        return UtilHelper.getLength(p1, p2);
    }

    @Override
    public String getString() {
        return "[" + p1.getString() + " " + p2.getString() + " " + Saved.getColorStr(color) +"]";
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
        String [] arr = obj.split("\n");
        if (arr.length != 1)
            throw new RuntimeException("Некорректная запись");
        String edge = arr[0];
        if(!edge.startsWith("[") || !edge.endsWith("]") )
            throw new RuntimeException("Некорректная запись");

        String str = edge.substring(1, edge.length() - 1);

        String p1 = Saved.getStr(str, "(", ")");
        setValue("p1", p1);
        str = Saved.offsetStr(str, ")");

        String p2 = Saved.getStr(str, "(", ")");
        setValue("p2", p2);
        str = Saved.offsetStr(str, ")");

        String c = Saved.getStr(str, "[", "]");
        setValue("color", c);
    }

    public double getLength() {
        if (length == 0)
            length = UtilHelper.getLength(p1, p2);
        return length;
    }
}
