package ru.avdonin.engine3d.space_builder;

import lombok.Getter;
import ru.avdonin.engine3d.helpers.SavedHelper;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;
import ru.avdonin.engine3d.rendering_panel.util.Creatable;
import ru.avdonin.engine3d.rendering_panel.util.objects.Light3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Object3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.TestObj;

import java.awt.*;

@Getter
public class SpaceBuilder {
    protected double x;
    protected double y;
    protected double z;

    protected double scale = 100;
    protected double resolution = 1;

    protected boolean isChangeScale = false;
    protected boolean isChangeResolution = false;

    public <T extends Creatable> void createSpace(T t, Point3D p, double size, BooleanCustom bol) {
        double offsetX = p.getX();
        double offsetY = p.getY();
        double offsetZ = p.getZ();

        double halfScale = scale / 2;
        double halfSize = size / 2;

        for (x = -halfScale; x <= halfScale; x += resolution) {
            for (y = -halfScale; y <= halfScale; y += resolution) {
                for (z = -halfScale; z <= halfScale; z += resolution) {
                    if (bol.get()) {
                        AbstractObject3D<?> obj;
                        if (t instanceof TestObj to) {
                            obj = to.createObject();
                        } else if (t instanceof AbstractObject3D<?>) {
                            obj = Creatable.newInstance(t.getClass().getName());
                        } else throw new RuntimeException("Неизвестный класс " + t.getClass().getName());

                        double xReal = offsetX + (x / halfScale) * halfSize;
                        double yReal = offsetY + (y / halfScale) * halfSize;
                        double zReal = offsetZ + (z / halfScale) * halfSize;

                        Point3D point = new Point3D(xReal, yReal, zReal);

                        obj.move(point);

                        int red = (int) Math.abs(Math.sqrt(x / halfScale) * 255);
                        int green = (int) Math.abs(Math.sqrt(y / halfScale) * 255);
                        int blue = (int) Math.abs(Math.sqrt(z / halfScale) * 255);

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

        createSpace(t, p, size, () -> upDown || frontBack);
    }

    public void createLights(Point3D point, int size) {
        int offsetX = (int) point.getX();
        int offsetY = (int) point.getY();
        int offsetZ = (int) point.getZ();
        int intensity = 1000;
        int s = size / 2;

        //  createLight(new Point3D(point), intensity, "Center");
        createLight(new Point3D(offsetX + s, offsetY, offsetZ), intensity, "Right");
        createLight(new Point3D(offsetX - s, offsetY, offsetZ), intensity, "Left");
        createLight(new Point3D(offsetX, offsetY + s, offsetZ), intensity, "Up");
        createLight(new Point3D(offsetX, offsetY - s, offsetZ), intensity, "Down");
        createLight(new Point3D(offsetX, offsetY, offsetZ + s), intensity, "Back");
        createLight(new Point3D(offsetX, offsetY, offsetZ - s), intensity, "Front");
    }

    private void createLight(Point3D p, int intensity, String name) {
        Light3D light = new Light3D(p, intensity);
        SavedHelper.addObjectToScene("Light" + name, light);
    }

    public void setScale(int scale) {
        this.scale = scale;
        this.isChangeScale = true;
    }

    public void setResolution(double resolution) {
        this.resolution = resolution;
        this.isChangeScale = true;
    }
}
