package ru.avdonin.engine3d.rendering_panel.util;

import ru.avdonin.engine3d.helpers.SerializeHelper;

public interface Serializable {
    /**
     * Изменить значение переменной
     *
     * @param key   название переменной
     * @param value новое значение
     */
    void setValue(String key, String value);

    /**
     * Возвращает сериализованное строковое представление объекта с учетом уровня вложенности
     *
     * @param count уровень вложенности
     * @return сериализованное строковое представление объекта
     */
    String serialize(int count);

    /**
     * Возвращает сериализованное строковое представление объекта
     *
     * @return сериализованное строковое представление объекта
     */
    String serialize();

    /**
     * Десериализовать объект из строки
     *
     * @param obj сериализованный объект
     */
    default void deserialize(String obj) {
        if (!obj.startsWith("[") || !obj.endsWith("]"))
            throw new RuntimeException("Некорректная запись\n" + obj);

        String str = obj.substring(1, obj.length() - 1);
        while (true) {
            if (str.isBlank()) return;

            String key = SerializeHelper.getNameObject(str);
            String value = SerializeHelper.getSerializeObject(str);

            if (key.isBlank() || value.isBlank())
                throw new RuntimeException("Некорректная запись\n" + obj);

            setValue(key, value);
            int size = key.length() + value.length() + 2;
            str = SerializeHelper.shortenString(str, size);
        }
    }
}
