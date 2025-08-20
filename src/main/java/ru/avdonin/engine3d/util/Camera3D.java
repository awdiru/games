package ru.avdonin.engine3d.util;

import jdk.jshell.execution.Util;
import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.helpers.UtilHelper;

@Getter
public class Camera3D {
    private final Vector3D vectorX = new Vector3D(0, 0, 0);
    private final Vector3D vectorY = new Vector3D(0, 0, 0);
    private final Vector3D vectorZ = new Vector3D(0, 0, 0);
    private final Point3D point;
    @Setter
    private double zoom = 1;
    private double viewingAngle = Math.PI / 2;

    public Camera3D(Point3D p, Vector3D v) {
        this.point = p;
        this.vectorZ.move(UtilHelper.getNormalVector(v));
        computeVectorX();
        computeVectorY();
    }

    public Camera3D(Vector3D v) {
        this.point = v.getStart();
        this.vectorZ.move(UtilHelper.getNormalVector(v));
        computeVectorX();
        computeVectorY();
    }

    public void move(Point3D p) {
        this.point.move(p);
    }

    public void translate(Vector3D v) {
        this.point.translate(v);
    }

    public void rotationRad(Point3D point, Vector3D vector, double angle) {
        this.vectorX.rotationRad(new Point3D(), vector, angle);
        this.vectorY.rotationRad(new Point3D(), vector, angle);
        this.vectorZ.rotationRad(new Point3D(), vector, angle);
        this.point.rotationRad(point, vector, angle);
    }

    public void rotation(Point3D point, Vector3D vector, double angle) {
        rotationRad(point, vector, Math.toRadians(angle));
    }

    public void setlViewingAngle(double x) {
        this.viewingAngle = Math.toRadians(x);
    }

    public void setViewingAngleRad(double x) {
        this.viewingAngle = x;
    }

    public double getProjectDistance(int height) {
        double angle = viewingAngle / 2;
        double h = (double) height / 2;
        double tan = Math.tan(angle);
        return h / tan;
    }

    private void computeVectorX() {
        Vector3D worldX = new Vector3D(1, 0, 0);

        double angle = UtilHelper.getAngleRad(vectorZ, worldX);

        double xx = Math.sin(angle);
        double yx = 0;
        double zx = Math.cos(angle);
        Vector3D vectorX = UtilHelper.getNormalVector(new Vector3D(xx, yx, zx));
        this.vectorX.move(vectorX);
    }

    private void computeVectorY() {
        double xz = vectorZ.getEnd().getX();
        double yz = vectorZ.getEnd().getY();
        double zz = vectorZ.getEnd().getZ();

        double xx = vectorX.getEnd().getX();
        double yx = vectorX.getEnd().getY();
        double zx = vectorX.getEnd().getZ();

        double xy = yz * zx - zz * yx;
        double yy = zz * xx - xz * zx;
        double zy = xz * yx - yz * xx;

        Vector3D vectorY = new Vector3D(xy, yy, zy);
        if (vectorY.getLength() > 1.001 || vectorY.getLength() < 0.999)
            vectorY = UtilHelper.getNormalVector(vectorY);

        this.vectorY.move(vectorY);
    }

    public Point3D getVectorXEnd() {
        return vectorX.getEnd();
    }

    public Point3D getVectorYEnd() {
        return vectorY.getEnd();
    }

    public Point3D getVectorZEnd() {
        return vectorZ.getEnd();
    }
}
