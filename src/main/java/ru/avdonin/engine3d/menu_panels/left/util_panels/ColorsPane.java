package ru.avdonin.engine3d.menu_panels.left.util_panels;

import javax.swing.*;
import java.awt.*;

public class ColorsPane extends JPanel {
    private final JTextField red = new JTextField(10);
    private final JTextField green = new JTextField(10);
    private final JTextField blue = new JTextField(10);
    private final JTextField alpha = new JTextField(10);

    public ColorsPane() {
        init();
    }

    public int getValue(String name) {
        String s = switch (name) {
            case "red" -> this.red.getText();
            case "green" -> this.green.getText();
            case "blue" -> this.blue.getText();
            default ->  this.alpha.getText();
        };
        double value = Double.parseDouble(s);
        if (value > 255) value = 255;
        if (value < 0) value = 0;
        return (int) value;
    }

    public void clear() {
        red.setText("");
        green.setText("");
        blue.setText("");
        alpha.setText("");
    }

    protected void init() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        add(new JLabel("Red: "), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        add(red, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        add(new JLabel("Green: "), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        add(green, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        add(new JLabel("Blue: "), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        add(blue, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        add(new JLabel("Alpha: "), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        add(alpha, gbc);
    }
}
