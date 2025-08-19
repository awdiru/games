package ru.avdonin.engine3d;


import lombok.Getter;
import ru.avdonin.engine3d.objects.Cube;
import ru.avdonin.engine3d.util.Camera3D;
import ru.avdonin.engine3d.util.Point3D;
import ru.avdonin.engine3d.util.Vector3D;

import javax.swing.*;
import java.awt.*;

@Getter
public class EngineFrame extends JFrame {
    private final RenderPanel renderPanel;

    public EngineFrame(String frameName, RenderPanel renderPanel) {
        setPreferredSize(new Dimension(800, 600));
        setTitle(frameName);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        this.renderPanel = renderPanel;
        add(this.renderPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RenderPanel renderPanel = new RenderPanel();
            new EngineFrame("test", renderPanel);

            Camera3D camera = renderPanel.getCamera();

            Point3D p1 = new Point3D(0, 0, 200);
            Point3D p2 = new Point3D(0, 1, 200);
            Point3D p3 = new Point3D(1, 0, 200);

            Cube cube = new Cube(p1, 200);

            renderPanel.addObject("Cube1", cube);

            Timer timer = new Timer(50, e -> {
               // cube.rotation(p1, new Vector3D(p1, p2), 5);
                camera.rotation(p1, new Vector3D(p1, p3), 5);
                renderPanel.repaint();
            });
            timer.start();
        });
    }
}
