package ru.avdonin.engine3d.util.objects;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.util.Obj;
import ru.avdonin.engine3d.util.Saved;

import java.awt.*;

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
    public String getString() {
        return "[" + p1.getString() + " " + p2.getString() + " " + p3.getString() + " " + isReflection + "]";
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "p1" -> p1.writeObject(value);
            case "p2" -> p2.writeObject(value);
            case "p3" -> p3.writeObject(value);
            case "isReflection" -> isReflection = Boolean.parseBoolean(value);
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        String [] arr = obj.split("\n");
        if (arr.length != 1)
            throw new RuntimeException("Некорректная запись");
        String pol = arr[0];
        if (!pol.startsWith("[") || !pol.endsWith("]"))
            throw new RuntimeException("Некорректная запись");

        String str = pol.substring(1, pol.length() - 1);

        String p1 = Saved.getStr(str, "(", ")");
        setValue("p1", p1);
        str = Saved.offsetStr(str, ")");

        String p2 = Saved.getStr(str, "(", ")");
        setValue("p2", p2);
        str = Saved.offsetStr(str, ")");

        String p3 = Saved.getStr(str, "(", ")");
        setValue("p3", p3);
        str = str.substring(str.lastIndexOf(" ") + 1);

        setValue("isReflection", str);
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
}
