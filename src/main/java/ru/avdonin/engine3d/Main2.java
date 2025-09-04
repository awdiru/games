package ru.avdonin.engine3d;

import ru.avdonin.engine3d.renders.impl.SimpleRender;
import ru.avdonin.engine3d.saver.Saver;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.util.objects.Object3D;
import ru.avdonin.engine3d.util.objects.Point3D;
import ru.avdonin.engine3d.util.objects.Vector3D;

import javax.swing.*;

public class Main2 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SceneStorage storage = new SceneStorage();

            Saver saver = new Saver();
            saver.openScene("save/scene.scn", storage);

            SimpleRender renderPanel = new SimpleRender(storage, 600, 400);
            Object3D cube = storage.get("cube");

            new EngineFrame("test", renderPanel, e -> {
                cube.rotation(new Point3D(), new Vector3D(0, 1, 0), 1);
                renderPanel.repaint();
            });
        });
    }
}
