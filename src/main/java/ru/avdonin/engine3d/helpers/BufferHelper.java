package ru.avdonin.engine3d.helpers;

import ru.avdonin.engine3d.util.Camera3D;
import ru.avdonin.engine3d.util.Point3D;
import ru.avdonin.engine3d.util.Polygon3D;
import ru.avdonin.engine3d.util.Vector3D;

import java.awt.geom.Point2D;

public class BufferHelper {
    public static double getPointDepth(Point3D point, Camera3D camera) {
        Point3D cameraPoint = camera.getPoint();
        Vector3D cameraZ = UtilHelper.getNormalVector(camera.getVectorZ());
        Vector3D viewVector = new Vector3D(cameraPoint, point);
        return viewVector.dot(cameraZ);
    }


    public static double interpolateDepth(Polygon3D polygon, Camera3D camera, double w1, double w2, double w3) {
        double z1 = BufferHelper.getPointDepth(polygon.getP1(), camera);
        double z2 = BufferHelper.getPointDepth(polygon.getP2(), camera);
        double z3 = BufferHelper.getPointDepth(polygon.getP3(), camera);
        return w1 * z1 + w2 * z2 + w3 * z3;
    }


    public static double edgeFunction(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
        return (c.x - a.x) * (b.y - a.y) - (c.y - a.y) * (b.x - a.x);
    }
}
