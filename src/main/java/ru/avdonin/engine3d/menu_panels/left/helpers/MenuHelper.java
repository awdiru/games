package ru.avdonin.engine3d.menu_panels.left.helpers;

import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.rendering_panel.util.Obj;
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

    /**
     * Сохранить объект на сцене
     *
     * @param name имя объекта
     */
    public static void saveObject(String name, Obj<?> obj) {
        SceneStorage storage = Context.get(Constants.STORAGE_KEY);
        int size = storage.getObjects().size();
        storage.add(name + size, obj);
    }
}
