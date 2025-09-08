package ru.avdonin.engine3d.helpers;

import javax.swing.*;
import java.awt.*;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class JFrameHelper {
    /**
     * Создать базовый JFrame
     *
     * @return базовый JFrame
     */
    public static JFrame createFrame() {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(230, 300));
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        return frame;
    }

    /**
     * Создать базовый JPanel
     *
     * @return базовый JPanel
     */
    public static JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
}
