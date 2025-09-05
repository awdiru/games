package ru.avdonin.engine3d.menu_panels.left;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.CoordsPane;
import ru.avdonin.engine3d.menu_panels.left.util_panels.input_panels.RotatePane;
import ru.avdonin.engine3d.rendering_panel.util.Obj;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Vector3D;
import ru.avdonin.engine3d.storage.SceneStorage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

@Getter
@Setter
public class LeftPanel extends JPanel {

    public LeftPanel(int weight, int height) {
        setBackground(Constants.BACKGROUND);
        setSize(new Dimension(weight, height));
        setPreferredSize(new Dimension(weight, height));
        setLayout(new BorderLayout());

        init();
    }

    private void init() {
        JPanel move = createMovePanel(Move.MOVE);
        JPanel translate = createMovePanel(Move.TRANSLATE);
        JPanel rotation = createRotatePanel();

        JPanel panel = new JPanel();
        panel.setSize(new Dimension(getWidth(), getHeight()));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(move);
        panel.add(translate);
        panel.add(rotation);

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createMovePanel(Move name) {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        CoordsPane coords = new CoordsPane();
        JButton button = createButton(e -> {
            double x = coords.getValue("x");
            double y = coords.getValue("y");
            double z = coords.getValue("z");
            coords.clear();

            Obj<?> obj = getStorage().getSelectedObject();
            if (obj != null) {
                switch (name) {
                    case MOVE -> obj.move(new Point3D(x, y, z));
                    case TRANSLATE -> obj.translate(new Vector3D(x, y, z));
                }
            }
        });

        JPanel p = new JPanel(new FlowLayout());
        p.add(coords);
        p.add(button);

        panel.add(new JLabel(name.getName()));
        panel.add(p);
        return panel;
    }

    private JPanel createRotatePanel() {
        JPanel rotate = new JPanel();
        rotate.setLayout(new BoxLayout(rotate, BoxLayout.Y_AXIS));

        RotatePane rotatePane = new RotatePane();
        JButton button = createButton(e -> {
            Obj<?> obj = getStorage().getSelectedObject();
            if (obj == null) return;

            Point3D point = new Point3D(
                    rotatePane.getPointValue("x"),
                    rotatePane.getPointValue("y"),
                    rotatePane.getPointValue("z")
            );

            Vector3D vector = new Vector3D(
                    rotatePane.getVectorValue("x"),
                    rotatePane.getVectorValue("y"),
                    rotatePane.getVectorValue("z")
            );

            double angle = rotatePane.getAngle();

            rotatePane.clear();
            obj.rotation(point, vector, angle);
        });
        JPanel p = new JPanel(new FlowLayout());
        p.add(rotatePane);
        p.add(button);

        rotate.add(new JLabel("Rotation"));
        rotate.add(p);

        return rotate;
    }

    private JButton createButton(ActionListener a) {
        JButton button = new JButton("->");
        button.addActionListener(a);
        return button;
    }

    public SceneStorage getStorage() {
        return Context.get(Constants.STORAGE_KEY);
    }

    @Getter
    private enum Move {
        MOVE("Move"),
        TRANSLATE("Translate");

        private final String name;

        Move(String name) {
            this.name = name;
        }
    }
}
