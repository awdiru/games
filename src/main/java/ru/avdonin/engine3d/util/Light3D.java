package ru.avdonin.engine3d.util;

import lombok.Getter;
import ru.avdonin.engine3d.helpers.UtilHelper;

@Getter
public class Light3D extends Object3D {
    private int intensity;
    private Vector3D vector;
    private double angle;

    public Light3D(Point3D start) {
        this(start, 0);
    }

    public Light3D(Point3D start, int intensity) {
        this(start, intensity, 90);
    }

    public Light3D(Point3D start, int intensity, double angle) {
        this(start, intensity, angle, new Vector3D(0, 0, 1));
    }

    public Light3D(Point3D start, int intensity, double angle, Vector3D vector) {
        this.point.move(start);
        this.intensity = intensity;
        this.angle = angle;
        this.vector = UtilHelper.getNormalVector(vector);
    }

    @Override
    public void rotationRad(Point3D point, Vector3D normal, double angle) {
        super.rotationRad(point, normal, angle);
        this.vector.rotation(new Point3D(), normal, angle);
    }

    @Override
    protected void addPolygon(Polygon3D pol) {
    }

    public void setIntensity(int intensity) {
        this.intensity = Math.max(intensity, 0);
    }

    public void setVector(Vector3D vector) {
        this.vector = UtilHelper.getNormalVector(vector);
    }

    public void setAngleRad(double angle) {
        if (angle >= 0)
            this.angle = angle % (Math.PI * 2);
        else this.angle = -angle % (Math.PI * 2);
    }

    public void setAngle(double angle) {
        this.setAngleRad(Math.toRadians(angle));
    }
}
