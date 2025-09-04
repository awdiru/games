package ru.avdonin.engine3d;

import ru.avdonin.engine3d.rendering_panel.renders.impl.SimpleRender;
import ru.avdonin.engine3d.rendering_panel.util.objects.Camera3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Point3D;
import ru.avdonin.engine3d.saver.Saver;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Saver.openScene("save/scene.scn");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            SimpleRender renderPanel = new SimpleRender(600, 600);
            Camera3D camera = renderPanel.getCamera();
            camera.move(new Point3D(0, 0, -200));
            renderPanel.setSkeleton(false);

            new EngineFrame("test", renderPanel, e -> {
                renderPanel.repaint();
            });
        });
    }
}
