package ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.surface;

import ru.avdonin.engine3d.helpers.JFrameHelper;
import ru.avdonin.engine3d.helpers.SavedHelper;
import ru.avdonin.engine3d.rendering_panel.util.Creatable;
import ru.avdonin.engine3d.rendering_panel.util.objects.PointsObject3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.default_obj.DefaultObj;

import javax.swing.*;
import java.util.List;

public abstract class SurfaceObj implements DefaultObj<PointsObject3D>, Creatable {
    protected Surface surface;

    @Override
    public abstract PointsObject3D createObject();

    @Override
    public void openCreateFrame() {
        JFrame frame = JFrameHelper.createFrame();
        JPanel panel = JFrameHelper.createPanel();

        String name = this.getClass().getSimpleName();
        frame.setTitle(name);

        List<SurfaceFields> fields = surface.getFields();

        for (SurfaceFields field : fields) {
            panel.add(new JLabel(field.getName()));
            panel.add(field.getCreatePanel());
        }
        JButton button = new JButton("create");
        button.addActionListener(e -> {
            PointsObject3D obj = createObject();
            SavedHelper.addObjectToScene(name, obj);
            frame.dispose();
        });
        panel.add(button);
        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);
    }
}
