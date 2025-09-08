package ru.avdonin.engine3d.rendering_panel.util;

import ru.avdonin.engine3d.helpers.SavedHelper;

public interface Saved {

    /**
     * Возвращает сериализованное строковое представление объекта с учетом уровня вложенности
     *
     * @param count уровень вложенности
     * @return сериализованное строковое представление объекта
     */
    String serialize(int count);

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
            if (str.isBlank()) return;

            String key = SavedHelper.getNameObject(str);
            String value = SavedHelper.getStrObject(str);

            if (key.isBlank() || value.isBlank())
                throw new RuntimeException("Некорректная запись\n" + obj);

            setValue(key, value);
            int size = key.length() + value.length() + 2;
            str = getStr(str,  size);
        }
    }

    private String getStr(String str, int size) {
        str = str.strip();
        if (str.length() < size) return "";
        str = str.substring(size);
        if (str.isBlank()) return "";
        return str;
    }
}
