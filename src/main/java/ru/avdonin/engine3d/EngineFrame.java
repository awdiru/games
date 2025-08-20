package ru.avdonin.engine3d;


import lombok.Getter;
import ru.avdonin.engine3d.objects.Cube;
import ru.avdonin.engine3d.renders.Render;
import ru.avdonin.engine3d.renders.impl.RayTracingRenderCustom;
import ru.avdonin.engine3d.util.Camera3D;
import ru.avdonin.engine3d.util.Light3D;
import ru.avdonin.engine3d.util.Point3D;
import ru.avdonin.engine3d.util.Vector3D;

import javax.swing.*;
import java.awt.*;

@Getter
public class EngineFrame extends JFrame {
    private final Render render;

    public EngineFrame(String frameName, Render render) {
        setPreferredSize(new Dimension(1280, 720));
        setTitle(frameName);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        this.render = render;
        add(this.render);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Render renderPanel = new RayTracingRenderCustom();
            new EngineFrame("test", renderPanel);

            Camera3D camera = renderPanel.getCamera();

            Point3D p1 = new Point3D(0, 0, 200);
            Point3D p2 = new Point3D(0, 1, 200);
            Point3D p3 = new Point3D(100, 0, 200);
            Point3D pLight = new Point3D(400, 400, 0);

            Cube cube = new Cube(p1, 200);
            Light3D light = new Light3D(pLight, 600, 90, new Vector3D(pLight, p1));

            camera.translate(new Vector3D(0, 200, -200));

            renderPanel.addObject("Cube1", cube);
            renderPanel.addObject("Light", light);

            Timer timer = new Timer(42, e -> {
                camera.rotation(p1, new Vector3D(0, 1, 0), -1);
                renderPanel.renderPoint(p1, Color.GREEN);
               // cube.rotation(p1, new Vector3D(0, 1, 0), 1);
               // camera.translate(new Vector3D(5, 0, 0));
                renderPanel.repaint();
            });
            timer.start();
        });
    }
}
