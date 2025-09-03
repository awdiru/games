package ru.avdonin.engine3d.util;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class Polygon3D {
    private final Point3D p1;
    private final Point3D p2;
    private final Point3D p3;

    private final Edge3D edge1;
    private final Edge3D edge2;
    private final Edge3D edge3;

    private boolean isReflection = false;

    private Object3D parent;

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

    public void move(Point3D p1, Point3D p2, Point3D p3) {
        this.p1.move(p1);
        this.p2.move(p2);
        this.p3.move(p3);
    }

    public void move(Polygon3D pol) {
        this.move(pol.p1, pol.p2, pol.p3);
    }

    public void translate(Point3D dP1, Point3D dP2, Point3D dP3) {
        this.p1.translate(dP1);
        this.p1.translate(dP2);
        this.p1.translate(dP3);
    }

    public void translate(Polygon3D dPol) {
        this.translate(dPol.p1, dPol.p2, dPol.p3);
    }

    public void translate(Vector3D v) {
        this.p1.translate(v);
        this.p1.translate(v);
        this.p1.translate(v);
    }

    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        this.p1.rotationRad(point, normal, angle);
        this.p2.rotationRad(point, normal, angle);
        this.p3.rotationRad(point, normal, angle);
    }

    public void rotation(Point3D point, Vector3D normal, double angle) {
        rotationRad(point, normal, Math.toRadians(angle));
    }

    public Color getColor() {
        return parent.getColor();
    }
}
