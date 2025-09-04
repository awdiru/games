package ru.avdonin.engine3d;


import lombok.Getter;
import ru.avdonin.engine3d.menu_panels.left.LeftPanel;
import ru.avdonin.engine3d.rendering_panel.renders.Render;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@Getter
public class EngineFrame extends JFrame {
    private final EnginePanel enginePanel;

    public EngineFrame(String frameName, Render render, ActionListener e) {
        setTitle(frameName);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        enginePanel = new EnginePanel();
        enginePanel.addRender(render);
        enginePanel.addLeftPanel(new LeftPanel(render.getWidth() / 4, render.getHeight()));
        add(enginePanel);

        setSize(new Dimension(enginePanel.getWidth(), enginePanel.getHeight()));

        setVisible(true);
        Timer timer = new Timer(42, e);
        timer.start();
    }
}
