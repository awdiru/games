package ru.avdonin.engine3d.rendering_panel.util;

import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.menu_panels.left.util_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.CoordsPane;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Vector3D;
import ru.avdonin.engine3d.storage.SceneStorage;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

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
     * Вернуть цвет объекта
     * @return цвет объекта
     */
    Color getColor();

    default JFrame getCreateFrame(){
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(230, 300));
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

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

    default Point3D getPoint(CoordsPane coords) {
        double x = coords.getValue("x");
        double y = coords.getValue("y");
        double z = coords.getValue("z");
        return new Point3D(x, y, z);
    }

    default Color getColor(ColorsPane color) {
        int red = color.getValue("red");
        int green = color.getValue("green");
        int blue = color.getValue("blue");
        int alpha = color.getValue("alpha");
        return new Color(red, green, blue, alpha);
    }

    default void saveObject(String name) {
        SceneStorage storage = Context.get(Constants.STORAGE_KEY);
        int size = storage.getObjects().size();
        storage.add(name + size, this);
    }
}
