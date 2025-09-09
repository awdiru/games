package ru.avdonin.engine3d.space_builder;

import ru.avdonin.engine3d.helpers.SavedHelper;
import ru.avdonin.engine3d.rendering_panel.util.objects.Light3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Object3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.TestObj;

import java.awt.*;

public class SpaceBuilder {
    public int x;
    public int y;
    public int z;

    public <T extends TestObj> void createSpace(T t, Point3D p, double size, int offset, BooleanCustom bol) {
        int offsetX = (int) p.getX();
        int offsetY = (int) p.getY();
        int offsetZ = (int) p.getZ();

        for (x = -offset; x <= offset; x++) {
            for (y = -offset; y <= offset; y++) {
                for (z = -offset; z <= offset; z++) {
                    if (bol.get()) {
                        int xc = (int) (x * size / offset + offsetX);
                        int yc = (int) (y * size / offset + offsetY);
                        int zc = (int) (z * size / offset + offsetZ);

                        Point3D point = new Point3D(xc, yc, zc);
                        Object3D obj = t.createObject(point, size / offset - (size / 50));

                        int red = Math.min(Math.abs(25 * (x + 5)), 255);
                        int green = Math.min(Math.abs(25 * (y + 5)), 255);
                        int blue = Math.min(Math.abs(25 * (z + 5)), 255);

                        obj.setColor(new Color(red, green, blue, 255));
                        SavedHelper.addObjectToScene(t.getClass().getSimpleName(), obj);
                    }
                }
            }
        }
    }

    public <T extends TestObj> void createSphere(T t, Point3D p, double size) {
        double radius = size / 2;

        for (int i = 0; i <= 16; i++) {
            double theta = i * Math.PI / 16;
            double sinTheta = Math.sin(theta);
            double cosTheta = Math.cos(theta);

            for (int j = 0; j <= 32; j++) {
                double phi = j * 2 * Math.PI / 32;
                double sinPhi = Math.sin(phi);
                double cosPhi = Math.cos(phi);

                double x = cosPhi * sinTheta;
                double y = cosTheta;
                double z = sinPhi * sinTheta;

                Point3D point = new Point3D(
                        p.getX() + radius * x,
                        p.getY() + radius * y,
                        p.getZ() + radius * z
                );
                Object3D obj = t.createObject(point, size / 15,
                        new Color((int) Math.abs(x * 255), (int) Math.abs(y * 255), (int) Math.abs(z * 255), 255));
                SavedHelper.addObjectToScene(t.getClass().getSimpleName(), obj);
            }
        }
    }

    public <T extends TestObj> void createCube(T t, Point3D p, double size, int quantity) {
        int off = quantity / 2;
        boolean upDown = (Math.abs(x) == off || Math.abs(z) == off)
                && (Math.abs(y) == off);
        boolean frontBack = (Math.abs(z) == off || Math.abs(y) == off)
                && (Math.abs(x) == off);

        createSpace(t, p, size, off, () -> upDown || frontBack);
    }

    public void createLights(Point3D point, int size) {
        int offsetX = (int) point.getX();
        int offsetY = (int) point.getY();
        int offsetZ = (int) point.getZ();

        createLight(new Point3D(point), "Center");
        createLight(new Point3D(offsetX + size, offsetY, offsetZ), "Right");
        createLight(new Point3D(offsetX - size, offsetY, offsetZ), "Left");
        createLight(new Point3D(offsetX, offsetY + size, offsetZ), "Up");
        createLight(new Point3D(offsetX, offsetY - size, offsetZ), "Down");
        createLight(new Point3D(offsetX, offsetY, offsetZ + size), "Back");
        createLight(new Point3D(offsetX, offsetY, offsetZ - size), "Front");
    }

    private void createLight(Point3D p, String name) {
        Light3D light = new Light3D(p, 700);
        SavedHelper.addObjectToScene("Light" + name, light);
    }
}
