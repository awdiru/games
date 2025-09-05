package ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels;

import ru.avdonin.engine3d.menu_panels.left.util_panels.InputPanel;

import javax.swing.*;

public class ColorsPane extends JPanel implements InputPanel<Integer> {
    SizeField<Integer> red = new ColorField("red");
    SizeField<Integer> green = new ColorField("green");
    SizeField<Integer> blue = new ColorField("blue");
    SizeField<Integer> alpha = new ColorField("alpha");

    public ColorsPane() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(red);
        add(green);
        add(blue);
        add(alpha);
    }

    @Override
    public Integer getValue(String name) {
        return switch (name) {
            case "red" -> red.getValue();
            case "green" -> green.getValue();
            case "blue" -> blue.getValue();
            default -> alpha.getValue();
        };
    }

    @Override
    public void clear() {
        red.clear();
        green.clear();
        blue.clear();
        alpha.clear();
    }

    private static class ColorField extends SizeField<Integer> {
        public ColorField(String name) {
            super(name);
        }

        @Override
        public Integer getValue() {
            String value = getValueText();
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                return 255;
            }
        }
    }
}
