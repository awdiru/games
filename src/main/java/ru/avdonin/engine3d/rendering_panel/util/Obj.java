package ru.avdonin.engine3d.rendering_panel.util;

import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Vector3D;
import ru.avdonin.engine3d.storage.SceneStorage;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.*;

public interface Obj<T> extends Saved {
    /**
     * Переместить к выбранной точке
     *
     * @param p целевая точка
     */
    void move(Point3D p);

    /**
     * Переместить к выбранному объекту
     *
     * @param t целевой объект
     */
    void move(T t);

    /**
     * Сдвинуть объект на вектор
     *
     * @param v вектор движения
     */
    void translate(Vector3D v);

    /**
     * Повернуть объект
     *
     * @param point  точка поворота
     * @param normal вектор оси поворота
     * @param angle  угол поворота в радианах
     */
    void rotationRad(Point3D point, Vector3D normal, double angle);

    /**
     * Повернуть объект
     *
     * @param point  точка поворота
     * @param normal вектор оси поворота
     * @param angle  угол поворота в градусах
     */
    default void rotation(Point3D point, Vector3D normal, double angle) {
        rotationRad(point, normal, Math.toRadians(angle));
    }

    /**
     * Вернуть цвет объекта
     *
     * @return цвет объекта
     */
    Color getColor();

    /**
     * Вернуть точку расположения объекта
     *
     * @return точка расположения объекта
     */
    Point3D getPoint();

    /**
     * Вернуть окно создания объекта
     *
     * @return окно создания объекта
     */
    default JFrame getCreateFrame() {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(230, 300));
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Вернуть точку из панели ввода
     *
     * @param coords панель ввода
     * @return точка
     */
    default Point3D getPoint(CoordsPane coords) {
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
    default Color getColor(ColorsPane colorsPane) {
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
    default void saveObject(String name, Obj<?> obj) {
        SceneStorage storage = Context.get(Constants.STORAGE_KEY);
        int size = storage.getObjects().size();
        storage.add(name + size, obj);
    }
}
