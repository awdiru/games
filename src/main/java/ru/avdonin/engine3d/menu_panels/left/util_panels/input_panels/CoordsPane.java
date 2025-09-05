package ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels;

import ru.avdonin.engine3d.menu_panels.left.util_panels.InputPanel;

import javax.swing.*;

public class CoordsPane extends JPanel implements InputPanel<Double> {
    private final CoordsField x = new CoordsField("X");
    private final CoordsField y = new CoordsField("y");
    private final CoordsField z = new CoordsField("z");

    public CoordsPane() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(x);
        add(y);
        add(z);
    }

    @Override
    public Double getValue(String name) {
        return switch (name) {
            case "x" -> x.getValue();
            case "y" -> y.getValue();
            default -> z.getValue();
        };
    }

    @Override
    public void clear() {
        x.clear();
        y.clear();
        z.clear();
    }

    private static class CoordsField extends SizeField<Double> {
        public CoordsField(String name) {
            super(name);
        }

        @Override
        public Double getValue() {
            String value = valueField.getText();
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                return 0.0;
            }
        }
    }
}