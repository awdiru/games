package ru.avdonin.engine3d;

import ru.avdonin.engine3d.objects.Cube;
import ru.avdonin.engine3d.objects.Plane;
import ru.avdonin.engine3d.renders.impl.SimpleRender;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.util.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender(600, 400);
            SceneStorage storage = renderPanel.getSceneStorage();

            Point3D p0 = new Point3D();
            Point3D l = new Point3D(250, 250, 0);
            Point3D c = new Point3D(0, 60, 0);
            Point3D cam = new Point3D(0, 0, -400);

            Camera3D camera = renderPanel.getCamera();
            camera.move(cam);
            camera.rotation(p0, new Vector3D(1, 0, 0), 30);

            Light3D light = new Light3D(l, 500);
            storage.add("light", light);

            Cube cube = new Cube(c, 100, new Color(70, 255, 210));
            storage.add("cube", cube);

            Plane plane = new Plane(p0, 700);
            storage.add("plane", plane);

            new EngineFrame("test", renderPanel, e -> {
                cube.rotation(c, new Vector3D(0, 1, 0), 1);
                renderPanel.repaint();
            });
        });
    }
}
