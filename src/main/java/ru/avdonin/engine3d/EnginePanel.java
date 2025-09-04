package ru.avdonin.engine3d;

import lombok.Getter;
import ru.avdonin.engine3d.menu_panels.left.LeftPanel;
import ru.avdonin.engine3d.rendering_panel.renders.Render;

import javax.swing.*;
import java.awt.*;

@Getter
public class EnginePanel extends JPanel {
    private final JSplitPane leftPanel = new JSplitPane();

    public EnginePanel() {
        setLayout(new BorderLayout());
        leftPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        add(leftPanel, BorderLayout.CENTER);
    }

    public void addRender(Render render) {
        leftPanel.setRightComponent(render);
        updateHorSize(render);
    }

    public void addLeftPanel(LeftPanel panel) {
        leftPanel.setLeftComponent(panel);
        updateHorSize(panel);
    }

    private void updateHorSize(JComponent component) {
        int w = getWidth() + component.getWidth();
        int h = Math.max(getHeight(), component.getHeight());
        setSize(new Dimension(w, h));
    }
}
