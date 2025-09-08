package ru.avdonin.engine3d.rendering_panel.util;

import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Vector3D;

import java.awt.*;

public abstract class AbstractObject3D<T> implements Saved, Creatable {
    /**
     * Переместить к выбранной точке
     *
     * @param p целевая точка
     */
    public abstract void move(Point3D p);

    /**
     * Переместить к выбранному объекту
     *
     * @param t целевой объект
     */
    public abstract void move(T t);

    /**
     * Сдвинуть объект на вектор
     *
     * @param v вектор движения
     */
    public abstract void translate(Vector3D v);

    /**
     * Повернуть объект
     *
     * @param point  точка поворота
     * @param normal вектор оси поворота
     * @param angle  угол поворота в радианах
     */
    public abstract void rotationRad(Point3D point, Vector3D normal, double angle);

    /**
     * Повернуть объект
     *
     * @param point  точка поворота
     * @param normal вектор оси поворота
     * @param angle  угол поворота в градусах
     */
    public void rotation(Point3D point, Vector3D normal, double angle) {
        rotationRad(point, normal, Math.toRadians(angle));
    }

    /**
     * Вернуть цвет объекта
     *
     * @return цвет объекта
     */
    public abstract Color getColor();

    /**
     * Вернуть точку расположения объекта
     *
     * @return точка расположения объекта
     */
    public abstract Point3D getPoint();
}
