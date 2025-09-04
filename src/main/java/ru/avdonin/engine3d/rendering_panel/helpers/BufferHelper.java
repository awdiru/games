package ru.avdonin.engine3d.rendering_panel.helpers;

import ru.avdonin.engine3d.rendering_panel.util.objects.Camera3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Polygon3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Vector3D;

public class BufferHelper {
    public static double getPointDepth(Point3D point, Camera3D camera) {
        Point3D cameraPoint = camera.getPoint();
        Vector3D cameraZ = UtilHelper.getNormalVector(camera.getVectorZ());
        Vector3D viewVector = new Vector3D(cameraPoint, point);
        return viewVector.dot(cameraZ);
    }


    public static double interpolateDepth(Polygon3D polygon, Camera3D camera, double w1, double w2, double w3) {
        double z1 = getPointDepth(polygon.getP1(), camera);
        double z2 = getPointDepth(polygon.getP2(), camera);
        double z3 = getPointDepth(polygon.getP3(), camera);
        return w1 * z1 + w2 * z2 + w3 * z3;
    }
}
