package ru.avdonin.engine3d.util;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.UtilHelper;

@Getter
@Setter
public class Edge3D implements Saved {
    protected Point3D p1;
    protected Point3D p2;
    protected double length;

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

    public void move (Point3D p1, Point3D p2) {
        this.p1.move(p1);
        this.p2.move(p2);
        this.length = calculateLength();
    }

    public void move(Edge3D e) {
        this.move(e.p1, e.p2);
    }

    public void translate(Point3D dP1, Point3D dP2) {
        this.p1.translate(dP1);
        this.p2.translate(dP2);
        this.length = calculateLength();
    }

    public void translate(Edge3D e) {
        this.translate(e.p1, e.p2);
    }

    public void translate(Vector3D v) {
        this.p1.translate(v);
        this.p2.translate(v);
        this.length = calculateLength();
    }

    public void rotationRad(Point3D point, Vector3D normal, double angle){
        this.p1.rotationRad(point, normal, angle);
        this.p2.rotationRad(point, normal, angle);
    }

    public void rotation(Point3D point, Vector3D normal, double angle) {
        rotationRad(point, normal, Math.toRadians(angle));
    }

    protected double calculateLength() {
        return UtilHelper.getLength(p1, p2);
    }

    @Override
    public String getString() {
        return "[" + p1.getString() + " " + p2.getString() + "]";
    }

    @Override
    public void setValue(String key, String value) {
        switch (key) {
            case "p1" -> p1.writeObject(value);
            case "p2" -> p2.writeObject(value);
            default -> throw new RuntimeException("Некорректное название переменной");
        }
    }

    @Override
    public void writeObject(String obj) {
        if(!obj.startsWith("[") || !obj.endsWith("]"))
            throw new RuntimeException("Некорректная запись");

        String str = obj.substring(1, obj.length() - 1);

        String p1 = Saved.getStr(str, "(", ")");
        setValue("p1", p1);
        str = Saved.offsetStr(str, "(");

        String p2 = Saved.getStr(str, "(", ")");
        setValue("p2", p2);
    }
}
