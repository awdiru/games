package ru.avdonin.engine3d.menu_panels.left.helpers;

import ru.avdonin.engine3d.rendering_panel.util.objects.Camera3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Polygon3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Vector3D;

public class BufferHelper {
    /**
     * Рассчитать глубину точки относительно камеры
     *
     * @param point  точка в пространстве
     * @param camera камера
     * @return рассчитанная глубина
     */
    public static double getPointDepth(Point3D point, Camera3D camera) {
        Point3D cameraPoint = camera.getPoint();
        Vector3D cameraZ = UtilHelper.getNormalVector(camera.getBasis().getVectorZ());
        Vector3D viewVector = new Vector3D(cameraPoint, point);
        return viewVector.dot(cameraZ);
    }

    /**
     * Интерполировать глубину точек полигона относительно вершин
     *
     * @param polygon полигон
     * @param camera  камера
     * @param w1      вес точки
     * @param w2      вес точки
     * @param w3      вес точки
     * @return интерполированная глубина
     */
    public static double interpolateDepth(Polygon3D polygon, Camera3D camera, double w1, double w2, double w3) {
        double z1 = getPointDepth(polygon.getP1(), camera);
        double z2 = getPointDepth(polygon.getP2(), camera);
        double z3 = getPointDepth(polygon.getP3(), camera);
        return w1 * z1 + w2 * z2 + w3 * z3;
    }
}
