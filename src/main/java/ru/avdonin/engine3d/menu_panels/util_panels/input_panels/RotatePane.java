package ru.avdonin.engine3d.menu_panels.util_panels.input_panels;

import javax.swing.*;

public class RotatePane extends JPanel {
    private final PointPane point = new PointPane();
    private final PointPane vector = new PointPane();
    private final AngleField angle = new AngleField();

    public RotatePane() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Point"));
        add(point);
        add(new JLabel("Vector"));
        add(vector);
        add(new JLabel("Angle"));
        add(angle);
    }

    public double getPointValue(String name) {
        return point.getValue(name);
    }

    public double getVectorValue(String name) {
        return vector.getValue(name);
    }

    public double getAngle() {
        return angle.getValue();
    }

    public void clear() {
        point.clear();
        vector.clear();
        angle.clear();
    }

    private class AngleField extends SizeField<Double> {
        public AngleField() {
            super("Angle");
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

        @Override
        public Double getInstance() {
            return getValue();
        }
    }
}
