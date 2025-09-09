package ru.avdonin.engine3d;

import ru.avdonin.engine3d.helpers.SavedHelper;
import ru.avdonin.engine3d.rendering_panel.renders.impl.SimpleRender;
import ru.avdonin.engine3d.rendering_panel.util.objects.*;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.obj.House;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.obj.Sphere;
import ru.avdonin.engine3d.saver.Saver;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.obj.Cube;
import ru.avdonin.engine3d.space_builder.SpaceBuilder;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        test5();
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
            SavedHelper.addObjectToScene("Cube", cube);

            Point3D h = new Point3D(100, 60, 0);
            Object3D house = new House().createObject(h, 100);
            house.rotation(house.getPoint(), new Vector3D(0, 1, 0), -41);
            house.setColor(new Color(125, 255, 0));
            SavedHelper.addObjectToScene("House", house);

            Point3D l = new Point3D(150, 200, -150);
            Light3D light = new Light3D(l, 700);
            SavedHelper.addObjectToScene("Light", light);

            Point3D point = new Point3D(0, 100, -150);
            point.setColor(new Color(255, 0, 183));
            SavedHelper.addObjectToScene("Point", point);

            Saver.saveScene("save", "scene");

            new EngineFrame("test", renderPanel, e -> {
                camera.rotation(new Point3D(), new Vector3D(0, 1, 0), 2);
                renderPanel.repaint();
            });
        });
    }

    private static void test2() {
        SwingUtilities.invokeLater(() -> {
            String nameScene = "scene_sphere";
            Point3D center = new Point3D(500, -500, 500);

            Saver.openScene("save/" + nameScene + "/" + nameScene + ".scn");
            SimpleRender renderPanel = new SimpleRender(600, 600);
            renderPanel.setSkeleton(false);

            Camera3D camera = renderPanel.getCamera();
            Saver.saveScene("save", nameScene);
            new EngineFrame("test", renderPanel, e -> {
                camera.rotation(center, new Vector3D(0, 1, 0), 2);
                renderPanel.repaint();
            });
        });
    }

    private static void test3() {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender(600, 600);
            renderPanel.setSkeleton(false);

            SpaceBuilder builder = new SpaceBuilder();

            Point3D center = new Point3D(500, -500, 500);

            Camera3D camera = renderPanel.getCamera();
            camera.move(new Point3D(0, -500, -400));
            camera.getBasis().setVectorZ(new Vector3D(camera.getPoint(), center));

            builder.createLights(center, 700);
            builder.createCube(new Sphere(), center, 1000, 11);

            Saver.saveScene("save", "scene_sphere");

            new EngineFrame("test", renderPanel, e -> {
                camera.rotation(center, new Vector3D(0, 1, 0), 2);
                renderPanel.repaint();
            });
        });
    }

    public static void test4() {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender(600, 600);
            renderPanel.setSkeleton(false);

            SpaceBuilder builder = new SpaceBuilder();

            Point3D center = new Point3D(500, -500, 500);

            Camera3D camera = renderPanel.getCamera();
            camera.move(new Point3D(0, -500, -400));
            camera.getBasis().setVectorZ(new Vector3D(camera.getPoint(), center));

            builder.createLights(center, 700);
            builder.createSphere(new Sphere(), center, 1000);

            Saver.saveScene("save", "test4");

            new EngineFrame("test", renderPanel, e -> {
                camera.rotation(center, new Vector3D(0, 1, 0), 2);
                renderPanel.repaint();
            });
        });
    }

    public static void test5() {
        SwingUtilities.invokeLater(() -> {
            SimpleRender renderPanel = new SimpleRender(600, 600);
            renderPanel.setSkeleton(false);

            SpaceBuilder builder = new SpaceBuilder();

            Point3D center = new Point3D(500, -500, 500);

            Camera3D camera = renderPanel.getCamera();
            camera.move(new Point3D(0, -500, -1000));
            camera.getBasis().setVectorZ(new Vector3D(camera.getPoint(), center));

            builder.createLights(center, 700);

            builder.createSpace(new Sphere(), center, 1000, 21, () -> {
                int x = builder.x;
                int y = builder.y;
                int z = builder.z;

                boolean sphere = x * x + y * y + z * z <= 450 && x * x + y * y + z * z >= 350;
                boolean cil = (x - 10) * (x - 10) + y * y <= 110 && (x - 10) * (x - 10) + y * y >= 90;
                return sphere;
            });

            new EngineFrame("test", renderPanel, e -> {
                camera.rotation(center, new Vector3D(0, 1, 0), 2);
                renderPanel.repaint();
            });
        });
    }
}
