package ru.avdonin.engine3d.rendering_panel.util;

import ru.avdonin.engine3d.menu_panels.left.helpers.SavedHelper;

public interface Saved {

    String getString(int count);
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
    default void writeObject(String obj) {
        if (!obj.startsWith("[") || !obj.endsWith("]"))
            throw new RuntimeException("Некорректная запись\n" + obj);

        String str = obj.substring(1, obj.length() - 1);
        while (true) {
            String key = SavedHelper.getNameObject(str);
            String value = SavedHelper.getStrObject(str);

            if(key.isBlank() || value.isBlank())
                throw new RuntimeException("Некорректная запись\n" + obj);

            setValue(key, value);

            int size = key.length() + value.length() + 2;
            str = str.strip();
            if (str.length() < size) return;
            str = str.substring(size);
            if (str.isBlank()) return;
        }
    }
}
