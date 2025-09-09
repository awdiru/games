package ru.avdonin.engine3d.helpers;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializeHelper {
    /**
     * Проверить корректность имени объекта
     *
     * @param name имя объекта
     */
    public static void validateNameObject(String name) {
        List<String> chars = List.of("[", "]", ",", ".", " ", "=");
        for (String s : chars) {
            if (name.contains(s))
                throw new RuntimeException("Имя объекта " + name + " содержит недопустимый символ '" + s + "'");
        }
    }

    /**
     * Получить имя первого объекта в записи
     *
     * @param s изначальная запись
     * @return имя первого объекта
     */
    public static String getNameObject(String s) {
        int index = s.indexOf("=");
        String name = s.substring(0, index);
        validateNameObject(name);
        name = name.strip();
        return name;
    }

    /**
     * Получить сериализованную строку первого объекта в общей записи
     *
     * @param s изначальная запись
     * @return запись объекта
     */
    public static String getSerializeObject(String s) {
        Character char1 = '[';
        Character char2 = ']';

        int startIndex = s.indexOf(char1);
        if (startIndex == -1) return "";

        int count = 1;
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
        return "";
    }

    /**
     * Сериализовать цвет
     *
     * @param color цвет
     * @return сериализованнный цвет
     */
    public static String serializeColor(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();
        return "[red=[" + red + "] green=[" + green + "] blue=[" + blue + "] alpha=[" + alpha + "]]";
    }

    /**
     * Десериализовать цвет
     *
     * @param col сериализованный цвет
     * @return цвет
     */
    public static Color deserializeColor(String col) {
        if (!col.startsWith("[") && !col.endsWith("]"))
            throw new RuntimeException("Некорректная запись\n" + col);

        String str = col.substring(1, col.length() - 1);

        Map<String, Integer> values = new HashMap<>();
        while (!str.isBlank()) {
            String name = getNameObject(str);
            String value = getSerializeObject(str);
            int size = name.length() + value.length() + 1;
            str = shortenString(str, size);
            int color = getValue(value);
            values.put(name, color);
        }

        return new Color(
                values.putIfAbsent("red", 255),
                values.putIfAbsent("green", 255),
                values.putIfAbsent("blue", 255),
                values.putIfAbsent("alpha", 255)
        );
    }

    /**
     * Вернуть отступ по уровню вложенности
     *
     * @param count уровень вложенности
     * @return отступ
     */
    public static String getStringSplitter(int count) {
        return "\n" + "\t".repeat(count);
    }

    /**
     * Сократить строку с начала на size. Пробелы, табуляция и переносы строк не учитываются и сокращаются
     *
     * @param str  изначальная строка
     * @param size размер сокращения
     * @return сокращенная строка
     */
    public static String shortenString(String str, int size) {
        str = str.strip();
        if (str.length() < size) return "";
        str = str.substring(size);
        if (str.isBlank()) return "";
        return str;
    }

    private static Integer getValue(String value) {
        if (value.startsWith("["))
            value = value.substring(1);
        if (value.endsWith("]"))
            value = value.substring(0, value.length() - 1);
        return Integer.parseInt(value);
    }
}
