package ru.avdonin.engine3d.test_objects;

import ru.avdonin.engine3d.menu_panels.left.helpers.MenuHelper;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.SizeField;
import ru.avdonin.engine3d.rendering_panel.util.objects.Object3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Polygon3D;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class Cube extends Object3D {

    public Cube() {
        this(new Point3D(), 100);
    }

    public Cube(Point3D p, double size) {
        this(p, size, Color.WHITE);
    }

    public Cube(Point3D p, double size, Color color) {
        this.point = p;
        this.points.add(point);
        this.color = color;
        initPolygons(size);
    }
    /*
        p5__________p8
        | \         | \
        |  p1__________p4
        |  |        |  |
        p6_|________p7 |
         \ |         \ |
           p2__________p3
    */

    private void initPolygons(double size) {
        List<Point3D> points = initPoints(size);
        // передняя грань
        Polygon3D p1 = new Polygon3D(points.get(4), points.get(2), points.get(1));
        Polygon3D p2 = new Polygon3D(points.get(4), points.get(3), points.get(2));
        // задняя грань
        Polygon3D p3 = new Polygon3D(points.get(8), points.get(6), points.get(7));
        Polygon3D p4 = new Polygon3D(points.get(8), points.get(5), points.get(6));
        // левая грань
        Polygon3D p5 = new Polygon3D(points.get(1), points.get(6), points.get(5));
        Polygon3D p6 = new Polygon3D(points.get(1), points.get(2), points.get(6));
        // правая грань
        Polygon3D p7 = new Polygon3D(points.get(4), points.get(8), points.get(7));
        Polygon3D p8 = new Polygon3D(points.get(4), points.get(7), points.get(3));
        // верхняя грань
        Polygon3D p9 = new Polygon3D(points.get(2), points.get(7), points.get(6));
        Polygon3D p10 = new Polygon3D(points.get(2), points.get(3), points.get(7));
        // нижняя грань
        Polygon3D p11 = new Polygon3D(points.get(1), points.get(5), points.get(8));
        Polygon3D p12 = new Polygon3D(points.get(1), points.get(8), points.get(4));

        Set<Polygon3D> polygons = Set.of(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12);

        for (Polygon3D pol : polygons) {
            addPolygon(pol);
        }
    }


    private List<Point3D> initPoints(double size) {
        double s = size / 2;

        Point3D p1 = new Point3D(point);
        p1.move(new Point3D(p1.getX() - s, p1.getY() - s, p1.getZ() - s));

        Point3D p2 = new Point3D(p1);
        p2.move(new Point3D(p1.getX(), p1.getY() + size, p1.getZ()));

        Point3D p3 = new Point3D(p2);
        p3.move(new Point3D(p2.getX() + size, p2.getY(), p2.getZ()));

        Point3D p4 = new Point3D(p3);
        p4.move(new Point3D(p3.getX(), p3.getY() - size, p3.getZ()));

        Point3D p5 = new Point3D(p1);
        p5.move(new Point3D(p1.getX(), p1.getY(), p1.getZ() + size));

        Point3D p6 = new Point3D(p5);
        p6.move(new Point3D(p5.getX(), p5.getY() + size, p5.getZ()));

        Point3D p7 = new Point3D(p6);
        p7.move(new Point3D(p6.getX() + size, p6.getY(), p6.getZ()));

        Point3D p8 = new Point3D(p7);
        p8.move(new Point3D(p7.getX(), p7.getY() - size, p7.getZ()));

        return List.of(point, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void getCreateFrame() {
        JFrame frame = createFrame();
        frame.setTitle("New Cube");

        JPanel panel = createPanel();

        CoordsPane p = new CoordsPane();
        ColorsPane c = new ColorsPane();
        SField s = new SField();

        JButton button = new JButton("->");
        button.addActionListener(e -> {
            Point3D point = MenuHelper.getPoint(p);
            Color color = MenuHelper.getColor(c);
            double size = s.getValue();

            Cube cube = new Cube(point, size, color);
            MenuHelper.saveObject("cube", cube);
            frame.dispose();
        });

        panel.add(new JLabel("point"));
        panel.add(p);
        panel.add(new JLabel("color"));
        panel.add(c);
        panel.add(new JLabel("size"));
        panel.add(s);
        panel.add(button);

        JScrollPane scroll = new JScrollPane(panel);
        frame.add(scroll);
    }

    private static class SField extends SizeField<Double> {
        public SField() {
            super();
        }

        @Override
        public Double getValue() {
            String value = getValueText();
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                return 100.0;
            }
        }
    }
}
