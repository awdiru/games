package ru.avdonin.engine3d.util;

import java.awt.*;

public interface Saved {
    String getString();
    void setValue(String key, String value);
    void writeObject(String obj);

    static String getStr(String s, String char1, String char2) {
        int index1 = s.indexOf(char1);
        if (index1 == -1)
            throw new RuntimeException("Некорректная запись");

        int index2 = s.indexOf(char2) + 1;
        if (index2 == 0)
            throw new RuntimeException("Некорректная запись");

        return s.substring(index1, index2);
    }

    static String offsetStr(String s, String c) {
        int index = s.indexOf(c);
        if (index == -1)
            throw new RuntimeException("Некорректная запись");
        return s.substring(index);
    }

    static String getColorStr(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = color.getAlpha();
        return "[" + red + " " + green + " " + blue + " " + alpha + "]";
    }

    static Color getColor(String c) {
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
