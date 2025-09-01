package ru.avdonin.engine3d;

import ru.avdonin.engine3d.objects.Cube;
import ru.avdonin.engine3d.renders.impl.SimpleRender;
import ru.avdonin.engine3d.util.*;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static boolean redV = true;
    private static boolean greenV = true;
    private static boolean blueV = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender();
            Camera3D camera = renderPanel.getCamera();

            Point3D p1 = new Point3D(0, 0, 400);
            Point3D p2 = new Point3D(0, 1, 700);
            Point3D p3 = new Point3D(400, 610, 400);

            Cube cube1 = new Cube(p1, 300, new Color(26, 255, 0));
            Cube cube2 = new Cube(p2, 400, new Color(255, 0, 0));
            cube2.rotation(p2, new Vector3D(0, 1, 0), 45);

            Light3D light = new Light3D(p3, 1000, 90, new Vector3D(p3, p1));

            camera.rotation(p1, camera.getVectorX(), 5);
            camera.translate(new Vector3D(0, 400, -300));

            renderPanel.addObject("Cube1", cube1);
            renderPanel.addObject("Cube2", cube2);
            renderPanel.addObject("Light", light);

            Point3D point1 = new Point3D(-500, 50, 300);
            Point3D point2 = new Point3D(500, 150, 300);
            Edge3D edge = new Edge3D(point1, point2);

            new EngineFrame("test", renderPanel, e -> {
                //  camera.rotation(p1, new Vector3D(0, 1, 0), -1);
                renderPanel.renderPoint(p1, Color.GREEN);
                renderPanel.renderLine(edge, Color.BLUE);
                cube1.rotation(p1, new Vector3D(0, 1, 0), -1);
                cube1.setColor(getColor(cube1.getColor()));
                // camera.translate(new Vector3D(5, 0, 0));
                light.rotation(p1, new Vector3D(0, 1, 0), 1);
                renderPanel.repaint();
            });
        });
    }

    private static Color getColor(Color c) {
        int offsetRed = 3;
        int red = redV ? c.getRed() + offsetRed : c.getRed() - offsetRed;
        if (red > 255) {
            redV = false;
            red = 255;
        }
        if (red < 0) {
            redV = true;
            red = 0;
        }

        int offsetGreen= 5;
        int green = greenV ? c.getGreen() + offsetGreen : c.getGreen() - offsetGreen;

        if (green > 255) {
            greenV = false;
            green = 255;
        }
        if (green < 0) {
            greenV = true;
            green = 0;
        }

        int offsetBlue= 8;
        int blue = blueV ? c.getBlue() + offsetBlue : c.getBlue() - offsetBlue;

        if (blue > 255) {
            blueV = false;
            blue = 255;
        }
        if (blue < 0) {
            blueV = true;
            blue = 0;
        }

        return new Color(red, green, blue);
    }
}
