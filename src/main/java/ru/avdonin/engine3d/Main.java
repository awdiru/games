package ru.avdonin.engine3d;

import ru.avdonin.engine3d.menu_panels.left.helpers.SavedHelper;
import ru.avdonin.engine3d.rendering_panel.renders.impl.SimpleRender;
import ru.avdonin.engine3d.rendering_panel.util.objects.*;
import ru.avdonin.engine3d.saver.Saver;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.obj.Cube;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        test2();
    }

    private static void test1() {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender(600, 600);

            Camera3D camera = renderPanel.getCamera();
            camera.move(new Point3D(100, 150, -400));
            camera.rotation(camera.getPoint(), new Vector3D(1, 0, 0), 15);

            renderPanel.setSkeleton(true);

            Point3D c = new Point3D(0, 60, 0);
            Object3D cube = new Cube().createObject(c, 100);
            SavedHelper.addObjectToScene("Cube", cube);

            Point3D l = new Point3D(150, 200, -150);
            Light3D light = new Light3D(l, 700);
            SavedHelper.addObjectToScene("Light", light);

            Saver.saveScene("save", "scene");

            new EngineFrame("test", renderPanel, e -> {
                renderPanel.repaint();
            });
        });
    }

    private static void test2() {
        SwingUtilities.invokeLater(() -> {
            Saver.openScene("save/scene.scn");
            SimpleRender renderPanel = new SimpleRender(600, 600);
            renderPanel.setSkeleton(true);
            new EngineFrame("test", renderPanel, e -> {
                renderPanel.repaint();
            });
        });
    }
}
