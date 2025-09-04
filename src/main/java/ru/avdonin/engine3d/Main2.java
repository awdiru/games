package ru.avdonin.engine3d;

import ru.avdonin.engine3d.rendering_panel.renders.impl.SimpleRender;
import ru.avdonin.engine3d.rendering_panel.saver.Saver;
import ru.avdonin.engine3d.storage.SceneStorage;

import javax.swing.*;

public class Main2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SceneStorage storage = new SceneStorage();

            Saver saver = new Saver();
            saver.openScene("save/scene.scn");

            SimpleRender renderPanel = new SimpleRender(600, 400);
            renderPanel.setSkeleton(false);

            new EngineFrame("test", renderPanel, e -> {
                renderPanel.repaint();
            });
        });
    }
}
