package ru.avdonin.engine3d.helpers;

import ru.avdonin.engine3d.menu_panels.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.util_panels.input_panels.PointPane;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;

import java.awt.*;

public class MenuHelper {

    /**
     * Вернуть точку из панели ввода
     *
     * @param coords панель ввода
     * @return точка
     */
    public static Point3D getPoint(PointPane coords) {
        return coords.getInstance();
    }

    /**
     * Вернуть цвет из панели ввода
     *
     * @param colorsPane панель цвета
     * @return цвет
     */
    public static Color getColor(ColorsPane colorsPane) {
        return colorsPane.getInstance();
    }
}
