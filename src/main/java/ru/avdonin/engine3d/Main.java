package ru.avdonin.engine3d;

import ru.avdonin.engine3d.test_objects.Cube;
import ru.avdonin.engine3d.test_objects.Plane;
import ru.avdonin.engine3d.renders.impl.SimpleRender;
import ru.avdonin.engine3d.saver.Saver;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.util.objects.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender(new SceneStorage(), 600, 400);
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

            Edge3D edge = new Edge3D(new Point3D(-300, 20, 0), new Point3D(300, 80, 0));
            edge.setColor(new Color(114, 255, 0));
            storage.add("edge", edge);

            Vector3D vector = new Vector3D(new Point3D(-300, 10, -10), new Point3D(300, 100, 10));
            vector.setColor(new Color(0, 39, 255));
            storage.add("vector", vector);

            Point3D point = new Point3D(-300, 40, 0);
            point.setColor(new Color(203, 0, 255));
            storage.add("point", point);


            Saver saver = new Saver();
            saver.saveScene( "save", "scene", storage);

            new EngineFrame("test", renderPanel, e -> {
                cube.rotation(c, new Vector3D(0, 1, 0), 1);
                renderPanel.repaint();
            });
        });
    }
}
