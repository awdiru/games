package ru.avdonin.engine3d.rendering_panel.helpers;

import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.rendering_panel.util.objects.Light3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Polygon3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Vector3D;

import java.awt.*;

public class RenderHelper {
    public static double computeIntensityLight(int minIntensity, Polygon3D polygon, Light3D light) {
        Point3D pCenter = UtilHelper.getCenterPolygon(polygon);
        Vector3D pn = UtilHelper.getNormalVector(UtilHelper.getNormal(polygon));
        Vector3D l = new Vector3D(light.getPoint(), pCenter);
        double angle = UtilHelper.getAngleRad(pn, l);
        return Math.max(minIntensity, Math.cos(angle) * light.getIntensity());
    }

    public static Color computeColor(Polygon3D polygon) {
        SceneStorage sceneStorage = Context.get(Constants.STORAGE_KEY);
        double intensity = 0;
        for (Light3D l : sceneStorage.getLights().values()) {
            double distance = UtilHelper.getLength(UtilHelper.getCenterPolygon(polygon), l.getPoint()) / 100;
            intensity += computeIntensityLight(sceneStorage.getLights().size() * 10, polygon, l) / distance;
        }
        if (intensity > 255) intensity = 255;
        if (intensity < 20) intensity = 20;

        Color color = polygon.getColor();
        double red = (double) color.getRed() / 255;
        double green = (double) color.getGreen() / 255;
        double blue = (double) color.getBlue() / 255;

        return new Color((int) (red * intensity), (int) (green * intensity), (int) (blue * intensity), color.getAlpha());
    }
}
