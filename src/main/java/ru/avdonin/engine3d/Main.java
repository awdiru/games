package ru.avdonin.engine3d;

import ru.avdonin.engine3d.objects.Cube;
import ru.avdonin.engine3d.objects.Plane;
import ru.avdonin.engine3d.renders.impl.SimpleRender;
import ru.avdonin.engine3d.util.Camera3D;
import ru.avdonin.engine3d.util.Light3D;
import ru.avdonin.engine3d.util.Point3D;
import ru.avdonin.engine3d.util.Vector3D;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender(600, 400);
            Point3D p0 = new Point3D();
            Point3D c = new Point3D(0, 60, 0);
            Point3D l = new Point3D(200, 300, 0);
            Point3D cam = new Point3D(0, 100, -700);

            Camera3D camera = renderPanel.getCamera();
            camera.move(cam);
            camera.rotation(p0, new Vector3D(0, 1, 0), -20);

            Cube cube = new Cube(c, 100, new Color(55, 241, 223));
            renderPanel.addObject("cube1", cube);

            Plane plane = new Plane(p0, new Color(147, 188, 248), 700);
            renderPanel.addObject("plane", plane);

            Light3D light = new Light3D(l, 500);
            renderPanel.addObject("light", light);

            new EngineFrame("test", renderPanel, e -> {
                camera.rotation(p0, new Vector3D(0,1,0), 1);
                renderPanel.repaint();
            });
        });
    }

}
