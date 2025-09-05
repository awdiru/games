package ru.avdonin.engine3d;

import ru.avdonin.engine3d.rendering_panel.renders.impl.SimpleRender;
import ru.avdonin.engine3d.rendering_panel.util.Saved;
import ru.avdonin.engine3d.rendering_panel.util.objects.Camera3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Light3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.saver.Saver;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.test_objects.Cube;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender(600, 600);
            Camera3D camera = renderPanel.getCamera();
            camera.move(new Point3D(100, 150, -400));
            renderPanel.setSkeleton(true);

            SceneStorage storage = Context.get(Constants.STORAGE_KEY);

            Point3D c = new Point3D(0, 60, 0);
            Cube cube = new Cube(c, 100);
            storage.add("Cube", cube);

            Point3D l = new Point3D(150, 200, -150);
            Light3D light = new Light3D(l, 700);
            storage.add("Light", light);

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
            new EngineFrame("test", renderPanel, e -> {
                renderPanel.repaint();
            });
        });
    }
}
