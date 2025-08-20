package ru.avdonin.engine3d.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ray {
    private final Point3D point;
    private final Vector3D vector;
    private int maxDepth = 4;
    private int distance = 100;
    private int stepSize = 20;

    public Ray(Point3D point, Vector3D vector) {
        this.point = point;
        this.vector = vector;
    }

    public void step () {
        Vector3D vector = new Vector3D(this.vector.getStart(), this.vector.getEnd());
        Point3D newEnd = vector.getEnd();
        newEnd.translate(this.vector);
        vector.move(vector.getStart(), newEnd);
    }
}
