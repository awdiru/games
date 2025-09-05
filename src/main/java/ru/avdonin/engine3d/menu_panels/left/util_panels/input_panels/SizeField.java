package ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels;


import javax.swing.*;
import java.awt.*;

public abstract class SizeField<T> extends JPanel {
    JTextField valueField = new JTextField(10);

    public SizeField() {
        this("size");
    }

    public SizeField(String name) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        add(new JLabel(name + ": "), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        add(valueField, gbc);
    }

    public String getValueText() {
        return valueField.getText();
    }

    public void clear() {
        valueField.setText("");
    }

    public abstract T getValue();
}
