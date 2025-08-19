package ru.avdonin.engine3d.helpers;

import ru.avdonin.engine3d.util.Point3D;

public class UtilHelper {
    public static double getLength(Point3D p1, Point3D p2) {
        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        double dz = p1.getZ() - p2.getZ();
        return Math.sqrt(((dx * dx) + (dy * dy) + (dz * dz)));
    }

    public static double getRadians(double x) {
        return Math.PI * (x / 180);
    }
}
