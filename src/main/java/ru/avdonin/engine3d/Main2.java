package ru.avdonin.engine3d;

import ru.avdonin.engine3d.renders.impl.SimpleRender;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.util.Vector3D;

import javax.swing.*;

public class Main2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender();
            SceneStorage storage = renderPanel.getSceneStorage();

            Saver saver = new Saver();
            saver.openScene("save/scene.scn", storage);

            new EngineFrame("test", renderPanel, e -> {
                renderPanel.repaint();
            });
        });
    }
}
