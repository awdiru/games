package ru.avdonin.engine3d.rendering_panel.util.objects.test_objects;

import ru.avdonin.engine3d.helpers.JFrameHelper;
import ru.avdonin.engine3d.helpers.MenuHelper;
import ru.avdonin.engine3d.helpers.SavedHelper;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.ColorsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.SizeField;
import ru.avdonin.engine3d.rendering_panel.util.Creatable;
import ru.avdonin.engine3d.rendering_panel.util.objects.Object3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Polygon3D;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public abstract class TestObj implements Creatable {
    public static final Point3D DEFAULT_POINT = new Point3D();
    public static final double DEFAULT_SIZE = 100;
    public static final Color DEFAULT_COLOR = Color.WHITE;

    public Object3D createObject() {
        return createObject(DEFAULT_POINT, DEFAULT_SIZE, DEFAULT_COLOR);
    }

    public Object3D createObject(Point3D p, double size) {
        return createObject(p, size, DEFAULT_COLOR);
    }

    public Object3D createObject(Point3D p, double size, Color color) {
        Set<Polygon3D> polygons = initPolygons(p, size);
        Object3D obj = new Object3D(polygons);
        obj.setPoint(p);
        obj.setColor(color);
        return obj;
    }

    @Override
    public void openCreateFrame() {
        JFrame frame = JFrameHelper.createFrame();

        String name = this.getClass().getSimpleName();
        frame.setTitle(name);

        JPanel panel = JFrameHelper.createPanel();

        CoordsPane p = new CoordsPane();
        ColorsPane c = new ColorsPane();
        TestObj.SField s = new TestObj.SField();

        JButton button = new JButton("->");
        button.addActionListener(e -> {
            Point3D point = MenuHelper.getPoint(p);
            Color color = MenuHelper.getColor(c);
            double size = s.getValue();

            Object3D obj = createObject(point, size, color);
            SavedHelper.addObjectToScene(name, obj);
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

    protected abstract Set<Polygon3D> initPolygons(Point3D p, double size);

    protected abstract List<Point3D> initPoints(Point3D p, double size);

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
