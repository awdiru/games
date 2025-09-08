package ru.avdonin.engine3d.helpers;

import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.storage.SceneStorage;

import java.awt.*;

public class MenuHelper {

    /**
     * Вернуть точку из панели ввода
     *
     * @param coords панель ввода
     * @return точка
     */
    public static Point3D getPoint(CoordsPane coords) {
        double x = coords.getValue("x");
        double y = coords.getValue("y");
        double z = coords.getValue("z");
        return new Point3D(x, y, z);
    }

    /**
     * Вернуть цвет из панели ввода
     *
     * @param colorsPane панель цвета
     * @return цвет
     */
    public static Color getColor(ColorsPane colorsPane) {
        int red = colorsPane.getValue("red");
        int green = colorsPane.getValue("green");
        int blue = colorsPane.getValue("blue");
        int alpha = colorsPane.getValue("alpha");
        return new Color(red, green, blue, alpha);
    }
}
