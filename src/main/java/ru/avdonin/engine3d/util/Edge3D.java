package ru.avdonin.engine3d.util;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.UtilHelper;

@Getter
@Setter
public class Edge3D {
    protected Point3D p1;
    protected Point3D p2;

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

    public void move (Point3D p1, Point3D p2) {
        this.p1.move(p1);
        this.p2.move(p2);
    }

    public void move(Edge3D e) {
        this.move(e.p1, e.p2);
    }

    public void translate(Point3D dP1, Point3D dP2) {
        this.p1.translate(dP1);
        this.p2.translate(dP2);
    }

    public void translate(Edge3D e) {
        this.translate(e.p1, e.p2);
    }

    public void translate(Vector3D v) {
        this.p1.translate(v);
        this.p2.translate(v);
    }

    public void rotationRad(Point3D point, Vector3D normal, double angle){
        this.p1.rotationRad(point, normal, angle);
        this.p2.rotationRad(point, normal, angle);
    }

    public void rotation(Point3D point, Vector3D normal, double angle) {
        rotationRad(point, normal, UtilHelper.getRadians(angle));
    }

    public double getLength() {
        return UtilHelper.getLength(p1, p2);
    }
}
