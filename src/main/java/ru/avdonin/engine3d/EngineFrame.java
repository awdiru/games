package ru.avdonin.engine3d;


import lombok.Getter;
import ru.avdonin.engine3d.renders.Render;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@Getter
public class EngineFrame extends JFrame {
    private final Render render;

    public EngineFrame(String frameName, Render render, ActionListener e) {
        setPreferredSize(new Dimension(render.getWidth(), render.getHeight()));
        setTitle(frameName);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        this.render = render;
        add(this.render);
        setVisible(true);

        Timer timer = new Timer(42, e);
        timer.start();
    }
}
