package ru.avdonin.engine3d.menu_panels.left;

import javax.swing.*;

public class RotatePane extends JPanel {
    private final CoordsPane point = new CoordsPane();
    private final CoordsPane vector = new CoordsPane();
    private final JTextField angle = new JTextField(10);

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
        return Double.parseDouble(angle.getText());
    }

    public void clear() {
        point.clear();
        vector.clear();
        angle.setText("");
    }
}
