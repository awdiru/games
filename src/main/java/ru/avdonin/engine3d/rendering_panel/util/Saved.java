package ru.avdonin.engine3d.rendering_panel.util;

import java.awt.*;

public interface Saved {
    /**
     * Вернуть строковое представление объекта
     *
     * @return строковое представление объекта
     */
    String getString();

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

    /**
     * Получить подстроку между заданными символами
     *
     * @param s     изначальная строка
     * @param char1 символ начала подстроки
     * @param char2 символ конца подстроки
     * @return искомая подстрока
     */
    static String getStr(String s, String char1, String char2) {
        int index1 = s.indexOf(char1);
        if (index1 == -1)
            throw new RuntimeException("Некорректная запись");

        int index2 = s.indexOf(char2) + 1;
        if (index2 == 0)
            throw new RuntimeException("Некорректная запись");

        return s.substring(index1, index2);
    }

    /**
     * Сместить начало строки до искомого символа
     *
     * @param s изначальная строка
     * @param c искомый символ
     * @return искомая строка
     */
    static String offsetStr(String s, String c) {
        int index = s.indexOf(c);
        if (index == -1)
            throw new RuntimeException("Некорректная запись");
        return s.substring(index + 1);
    }

    /**
     * Получить строковое представление цвета
     *
     * @param color цвет
     * @return строковое представление цвета
     */

    static String getColorStr(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();
        return "[" + red + " " + green + " " + blue + " " + alpha + "]";
    }

    /**
     * Получить цвет из строкового представления
     *
     * @param col строковое представление цвета
     * @return цвет
     */
    static Color getColor(String col) {
        String[] arr = col.split("\n");
        if (arr.length != 1)
            throw new RuntimeException("Некорректная запись");

        String c = arr[0];

        if (!c.startsWith("[") || !c.endsWith("]"))
            throw new RuntimeException("Некорректная запись");

        String s = c.substring(1, c.length() - 1);
        String[] array = s.split(" ");

        if (array.length != 4)
            throw new RuntimeException("Некорректная запись");

        int red = Integer.parseInt(array[0]);
        int green = Integer.parseInt(array[1]);
        int blue = Integer.parseInt(array[2]);
        int alpha = Integer.parseInt(array[3]);

        return new Color(red, green, blue, alpha);
    }
}
