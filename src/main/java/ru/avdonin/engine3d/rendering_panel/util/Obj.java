package ru.avdonin.engine3d.rendering_panel.util;

import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Vector3D;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.*;

public abstract class Obj<T> implements Saved {
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

    /**
     * Создать окно создания объекта
     */
    public abstract void getCreateFrame();

    /**
     * Создать базовый JFrame
     *
     * @return базовый JFrame
     */
    protected JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(230, 300));
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Создать базовый JPanel
     *
     * @return базовый JPanel
     */
    protected JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
}
