package ru.avdonin.engine3d;

import ru.avdonin.engine3d.util.objects.Point3D;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Point3D> points = List.of(new Point3D(), new Point3D(0, 0, 1), new Point3D(0, 1, 1));
        int index = points.indexOf(new Point3D(1, 1, 1));
        System.out.println(index);
    }
}
