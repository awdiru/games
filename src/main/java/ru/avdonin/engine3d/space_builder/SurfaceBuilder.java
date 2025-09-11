package ru.avdonin.engine3d.space_builder;

import lombok.Getter;
import ru.avdonin.engine3d.helpers.SavedHelper;
import ru.avdonin.engine3d.helpers.VectorHelper;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Vector3D;

import java.awt.*;

@Getter
public class SurfaceBuilder extends SpaceBuilder {
    protected double delta = 0.1;
    protected boolean isChangeDelta = false;

    public void createEllipsoid(Point3D p, double size, double dx, double dy, double dz) {
        createCoordinateGrid(p, size);
        if (!isChangeScale)
            this.scale = Math.max(dx, Math.max(dy, dz)) * 2.5;
        if (!isChangeResolution)
            this.resolution = scale / 200;
        if (!isChangeDelta)
            this.delta = 0.005 * Math.log(1 + scale / resolution);
        resetFlags();

        createSpace(new Point3D(), p, size, () -> {
            double surface = ((x * x / (dx * dx)) + (y * y / (dy * dy)) + (z * z / (dz * dz)));
            return surface <= 1 + delta && surface >= 1 - delta;
        });
    }

    public void createHyperboloid(Point3D p, double size, boolean isBicameral, double dx, double dy, double dz, double r) {
        createCoordinateGrid(p, size);
        if (!isChangeScale)
            this.scale = Math.max(dx, Math.max(dy, dz)) * 40;
        if (!isChangeResolution)
            this.resolution = scale / 500;
        if (!isChangeDelta)
            this.delta = 0.01 * Math.log(1 + scale / resolution);
        resetFlags();

        createSpace(new Point3D(), p, size, () -> {
            double sum = (isBicameral ? -1 : 1) * r;
            double surface = (x * x / (dx * dx)) + (y * y / (dy * dy)) - (z * z / (dz * dz));

            return surface <= sum + delta && surface >= sum - delta;
        });
    }

    public void createSinWave(Point3D p, double size, double period, double A) {
        createCoordinateGrid(p, size);
        if (!isChangeScale)
            this.scale = Math.max(period * 6, A * 12);
        if (!isChangeResolution)
            this.resolution = scale / 300;
        if (!isChangeDelta)
            this.delta = 0.01 * Math.log(1 + scale / resolution);
        resetFlags();

        createSpace(new Point3D(), p, size, () -> {
            double surface = y - A * Math.sin(((2 * Math.PI) / period) * x);
            return surface <= delta && surface >= -delta;
        });
    }

    public void createThor(Point3D p, double size, double R, double r) {
        createCoordinateGrid(p, size);
        if (!isChangeScale)
            this.scale = (R + r) * 2.2;
        if (!isChangeResolution)
            this.resolution = scale / 400;
        if (!isChangeDelta)
            this.delta = 0.01 * Math.log(1 + scale / resolution);
        resetFlags();

        createSpace(new Point3D(), p, size, () -> {
            double sum = (4 * R * R) * ((x * x) + (y * y));
            double surface = Math.pow((x * x) + (y * y) + (z * z) + (R * R) - (r * r), 2);

            return surface <= sum + delta && surface >= sum - delta;
        });
    }

    public void createBoySurface(Point3D p, double size) {
        createCoordinateGrid(p, size);
        if (!isChangeScale)
            this.scale = 7;
        if (!isChangeResolution)
            this.resolution = scale / 200;
        if (!isChangeDelta)
            this.delta = 0.1 * Math.log(1 + scale / resolution);
        resetFlags();

        createSpace(new Point3D(), p, size, () -> {
            double sum = Math.pow(((x * x) + (y * y) + (z * z)), 2);
            double surface = (2 * x * x) + (2 * y * y)
                    + Math.pow((y * y) + (x * x) - (z * z), 2);

            return surface <= sum + delta && surface >= sum - delta;
        });
    }

    public void createKlyainSurface(Point3D p, double size) {
        createCoordinateGrid(p, size);
        if (!isChangeScale)
            this.scale = 9;
        if (!isChangeResolution)
            this.resolution = scale / 300;
        if (!isChangeDelta)
            this.delta = 0.08 * Math.log(1 + scale / resolution);
        resetFlags();

        createSpace(new Point3D(), p, size, () -> {
            double sum = 0;
            double T = (x * x) + (y * y) + (z * z);
            double surface = (T + (2 * y) - 1)
                    * (Math.pow(T - (2 * y) - 1, 2) - 8 * (z * z))
                    + 16 * x * z * (T - (2 * y) - 1);

            return surface <= sum + delta && surface >= sum - delta;
        });
    }

    public void createBarthSextic(Point3D p, double size) {
        createCoordinateGrid(p, size);
        if (!isChangeScale)
            this.scale = 10;
        if (!isChangeResolution)
            this.resolution = scale / 400;
        if (!isChangeDelta)
            this.delta = 0.05 * Math.log(1 + scale / resolution);
        resetFlags();

        createSpace(new Point3D(), p, size, () -> {
            double T = (1 + Math.sqrt(5)) / 2;
            double sum = 0;
            double surface = 4 * (T * T * x * x - y * y) * (T * T * y * y - z * z) * (T * T * z * z - x * x)
                    - (2 * T + 1) * Math.pow(x * x + y * y + z * z - 1, 2);

            return surface <= sum + delta && surface >= sum - delta;
        });
    }

    public void createHeard(Point3D p, double size) {
        setValues(4, 0.1, 0.05);

        createSpace(new Point3D(), p, size, () -> {
            double sum = 0;
            double surface = Math.pow(x * x + (9.0 / 4.0) * z * z + y * y - 1, 3)
                    - x * x * y * y * y - (9.0 / 80.0) * z * z * y * y * y;

            return surface <= sum + delta && surface >= sum - delta;
        });
    }

    public void createDiniSurface(Point3D p, double size) {
        setValues(10, 0.1, 0.05);

        createSpace(new Point3D(), p, size, () -> {
            double sum = 0;
            double surface = (Math.cos(z) * Math.sin(x) - Math.sin(z) * Math.cos(x)) - 1 * Math.sinh(y) * Math.cosh(y);

            return surface <= sum + delta && surface >= sum - delta;
        });
    }

    public void setDelta(double delta) {
        this.delta = delta;
        this.isChangeDelta = true;
    }

    private void createCoordinateGrid(Point3D o, double size) {
        Vector3D vectorX = new Vector3D(1, 0, 0);
        Vector3D vectorY = new Vector3D(0, 1, 0);
        Vector3D vectorZ = new Vector3D(0, 0, 1);

        vectorX.setColor(new Color(255, 0, 0));
        vectorY.setColor(new Color(0, 255, 0));
        vectorZ.setColor(new Color(0, 0, 255));

        double halfSize = size / 2;

        vectorX.move(o);
        vectorY.move(o);
        vectorZ.move(o);

        vectorX.translate(new Vector3D(-halfSize, 0, 0));
        vectorY.translate(new Vector3D(0, -halfSize, 0));
        vectorZ.translate(new Vector3D(0, 0, -halfSize));

        vectorX = VectorHelper.changeLenVector(vectorX, size);
        vectorY = VectorHelper.changeLenVector(vectorY, size);
        vectorZ = VectorHelper.changeLenVector(vectorZ, size);

        SavedHelper.addObjectToScene("VectorX", vectorX);
        SavedHelper.addObjectToScene("VectorY", vectorY);
        SavedHelper.addObjectToScene("VectorZ", vectorZ);
    }

    private void setValues(double scale, double baseDelta, double baseResolution) {
        if (!isChangeScale) {
            this.scale = scale;
        }
        if (!isChangeDelta) {
            this.delta = baseDelta * Math.sqrt(scale);
        }
        if (!isChangeResolution) {
            this.resolution = baseResolution * Math.sqrt(scale);
        }
        resetFlags();
    }

    private void resetFlags() {
        isChangeResolution = false;
        isChangeScale = false;
        isChangeDelta = false;
    }
}
