package ru.avdonin.engine3d.menu_panels;

import lombok.Getter;
import ru.avdonin.engine3d.rendering_panel.util.Creatable;
import ru.avdonin.engine3d.rendering_panel.util.objects.*;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.obj.Cube;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.obj.House;
import ru.avdonin.engine3d.rendering_panel.util.objects.test_objects.obj.Plane;
import ru.avdonin.engine3d.saver.Saver;

import javax.swing.*;
import java.awt.*;

public class UpPanel extends JPanel {
    public UpPanel() {
        setLayout(new FlowLayout());
        init();
    }

    private void init() {
        add(createSaveButton());
        add(createObjectButton());
    }

    private JButton createSaveButton() {
        JButton button = new JButton("save");
        button.addActionListener(e -> Saver.saveScene("save", "scene"));
        return button;
    }

    private JButton createObjectButton() {
        JButton button = new JButton("create object");
        button.addActionListener(e -> showCreateObjContextMenu(button));
        return button;
    }

    private void showCreateObjContextMenu(JButton button) {
        JPopupMenu menu = new JPopupMenu();

        for (ListObj obj : ListObj.values()) {
            JMenuItem item = new JMenuItem(obj.getName());
            item.addActionListener(e -> {
                Creatable o = obj.newInstance();
                o.openCreateFrame();
            });
            menu.add(item);
        }

        menu.show(button, 0, button.getHeight());
    }

    @Getter
    private enum ListObj {
        POINT("Point", Point3D.class),
        EDGE("Edge", Edge3D.class),
        VECTOR("Vector", Vector3D.class),
        POLYGON("Polygon", Polygon3D.class),
        LIGHT("Light", Light3D.class),
        PLANE("Plane", Plane.class),
        CUBE("Cube", Cube.class),
        HOUSE("House", House.class);

        private final String name;
        private final Class<? extends Creatable> aClass;

        ListObj(String name, Class<? extends Creatable> aClass) {
            this.name = name;
            this.aClass = aClass;
        }

        public Creatable newInstance() {
            return Creatable.newInstance(aClass.getName());
        }
    }
}
