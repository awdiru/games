package ru.avdonin.engine3d.rendering_panel.util;

import java.awt.*;

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

    /**
     * Получить подстроку между заданными символами.
     * При этом учитывается количество открывающих и закрывающих символов.
     *
     * @param s     изначальная строка
     * @param char1 символ начала подстроки
     * @param char2 символ конца подстроки
     * @return искомая подстрока
     */
    static String getSubString(String s, Character char1, Character char2) {
        if (char1 == null || char2 == null) return s;

        int startIndex = s.indexOf(char1);
        if (startIndex == -1)
            return null;

        int count = 1;

        if (char1.equals(char2)) {
            String str = s.substring(startIndex + 1);
            int endIndex = str.indexOf(char2) + 2;
            return s.substring(startIndex, endIndex);
        }

        for (int i = startIndex + 1; i < s.length(); i++) {
            char currentChar = s.charAt(i);
            if (char1.equals(currentChar))
                count++;
            else if (char2.equals(currentChar)) {
                count--;
                if (count == 0)
                    return s.substring(startIndex, i + 1);
            }
        }
        return null;
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
