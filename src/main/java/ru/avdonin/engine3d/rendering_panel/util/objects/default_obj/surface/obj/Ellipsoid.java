package ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.surface.obj;

import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.PointsObject3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.surface.Surface;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.surface.SurfaceFields;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.surface.SurfaceObj;
import ru.avdonin.engine3d.space_builder.SurfaceBuilder;

import java.util.Set;

public class Ellipsoid extends SurfaceObj {
    private Point3D point = new Point3D();
    private Double size = 1000.0;
    private Double dx = 10.0;
    private Double dy = 10.0;
    private Double dz = 10.0;

    public Ellipsoid() {
        this.surface = Surface.ELLIPSOID;
    }

    @Override
    public PointsObject3D createObject() {
        for (SurfaceFields field : surface.getFields()) {
            if (field.equals(SurfaceFields.POINT)) {
                point = (Point3D) field.getCreatePanel().getInstance();
            } else {
                Double d = (Double) field.getCreatePanel().getInstance();
                switch (field) {
                    case SIZE -> size = d;
                    case DX -> dx = d;
                    case DY -> dy = d;
                    case DZ -> dz = d;
                }
            }
        }
        SurfaceBuilder builder = new SurfaceBuilder();
        Set<Point3D> points = builder.createEllipsoid(point, size, dx, dy, dz);
        return new PointsObject3D(points);
    }
}
