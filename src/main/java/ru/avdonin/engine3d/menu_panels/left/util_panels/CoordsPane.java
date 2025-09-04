package ru.avdonin.engine3d.menu_panels.left.util_panels;

import javax.swing.*;
import java.awt.*;

public class CoordsPane extends JPanel {
    private final JTextField x = new JTextField(10);
    private final JTextField y = new JTextField(10);
    private final JTextField z = new JTextField(10);

    public CoordsPane() {
        init();
    }

    public double getValue(String name) {
        String s = switch (name) {
            case "x" -> this.x.getText();
            case "y" -> this.y.getText();
            default ->  this.z.getText();
        };
        return Double.parseDouble(s);
    }

    public void clear() {
        x.setText("");
        y.setText("");
        z.setText("");
    }

    protected void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        add(new JLabel("X: "), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        add(x, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        add(new JLabel("Y: "), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        add(y, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        add(new JLabel("Z: "), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        add(z, gbc);
    }
}