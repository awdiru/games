package ru.avdonin.engine3d.util;

import ru.avdonin.engine3d.util.objects.Point3D;
import ru.avdonin.engine3d.util.objects.Vector3D;

import java.awt.*;

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
}
