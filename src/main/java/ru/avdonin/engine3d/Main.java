package ru.avdonin.engine3d;

import ru.avdonin.engine3d.helpers.SavedHelper;
import ru.avdonin.engine3d.rendering_panel.renders.impl.SimpleRender;
import ru.avdonin.engine3d.rendering_panel.util.objects.*;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.obj.House;
import ru.avdonin.engine3d.saver.Saver;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.obj.Cube;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender(600, 600);

            Camera3D camera = renderPanel.getCamera();
            camera.move(new Point3D(100, 150, -400));
            camera.rotation(camera.getPoint(), new Vector3D(1, 0, 0), 15);

            renderPanel.setSkeleton(false);

            Point3D c = new Point3D(-100, 60, 0);
            Object3D cube = new Cube().createObject(c, 100);
            cube.rotation(cube.getPoint(), new Vector3D(0, 1, 0), 37);
            cube.setColor(new Color(68, 227, 201));
            SavedHelper.addObjectToScene(cube);

            Point3D h = new Point3D(100, 60, 0);
            Object3D house = new House().createObject(h, 100);
            house.rotation(house.getPoint(), new Vector3D(0, 1, 0), -41);
            house.setColor(new Color(68, 227, 201));
            SavedHelper.addObjectToScene(house);

            Point3D l = new Point3D(150, 200, -150);
            Light3D light = new Light3D(l, 700);
            SavedHelper.addObjectToScene(light);

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
            renderPanel.setSkeleton(false);

            Camera3D camera = renderPanel.getCamera();

            Saver.saveScene("save", "scene");
            new EngineFrame("test", renderPanel, e -> {
                 camera.rotation(new Point3D(), new Vector3D(0, 1, 0), 2);
                renderPanel.repaint();
            });
        });
    }
}
