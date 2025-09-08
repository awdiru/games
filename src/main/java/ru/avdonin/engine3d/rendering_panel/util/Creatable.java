package ru.avdonin.engine3d.rendering_panel.util;

import java.lang.reflect.Constructor;

public interface Creatable {
    /**
     * Открыть окно создания объекта
     */
    void openCreateFrame();

    /**
     * Создать экземпляр объекта
     *
     * @param className полное имя класса
     * @param <T>       класс возвращаемого объекта
     * @return новый экземпляр объекта
     */
    static <T extends Creatable> T newInstance(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor();
            return (T) constructor.newInstance();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Класс " + className + " не найден", e);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Нет конструктора без параметров для класса " + className, e);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании экземпляра класса " + className, e);
        }
    }
}
