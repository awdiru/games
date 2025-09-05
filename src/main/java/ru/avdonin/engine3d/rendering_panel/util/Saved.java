package ru.avdonin.engine3d.rendering_panel.util;

public interface Saved {
    /**
     * Изменить значение переменной
     *
     * @param key   название переменной
     * @param value новое значение
     */
    void setValue(String key, String value);

    /**
     * Записать объект из его строкового представления
     *
     * @param obj строковое представление объекта
     */
    void writeObject(String obj);
}
