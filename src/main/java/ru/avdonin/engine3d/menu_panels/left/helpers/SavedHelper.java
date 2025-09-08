package ru.avdonin.engine3d.menu_panels.left.helpers;

import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;
import ru.avdonin.engine3d.storage.SceneStorage;

import java.awt.*;
import java.util.List;

public class SavedHelper {

    /**
     * Сохранить объект на сцене
     *
     * @param name имя объекта
     */
    public static void addObjectToScene(String name, AbstractObject3D<?> obj) {
        validateNameObject(name);
        SceneStorage storage = Context.get(Constants.STORAGE_KEY);
        int size = storage.getObjects().size();
        storage.add(name + size, obj);
    }

    /**
     * Проверить корректность имени объекта
     *
     * @param name имя объекта
     */
    public static void validateNameObject(String name) {
        List<String> chars = List.of("[", "]", ",", ".", " ");
        for (String s : chars) {
            if (name.contains(s))
                throw new RuntimeException("Имя объекта " + name + " содержит недопустимый символ " + s);
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
     * Получить запись первого объекта в общей записи
     *
     * @param s изначальная запись
     * @return запись объекта
     */
    public static String getStrObject(String s) {
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
     * Получить строковое представление цвета
     *
     * @param color цвет
     * @return строковое представление цвета
     */
    public static String getColorStr(Color color) {
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
    public static Color getColor(String col) {
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

    public static String getStringSplitter(int count) {
        return "\n" + "\t".repeat(count);
    }
}
