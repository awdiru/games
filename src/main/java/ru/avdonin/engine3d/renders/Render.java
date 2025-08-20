package ru.avdonin.engine3d.renders;

import lombok.Getter;
import ru.avdonin.engine3d.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Render extends JPanel {
    protected final Map<String, Object3D> space = new HashMap<>();
    protected final Map<String, Light3D> lights = new HashMap<>();
    protected final Camera3D camera = new Camera3D(new Vector3D(0, 0, 1));

    public void addObject(String name, Object3D o) {
        if (o instanceof Light3D)
            lights.put(name, (Light3D) o);
        space.put(name, o);
        repaint();
    }

    public Object3D getObject(String name) {
        return space.get(name);
    }

    public abstract void renderPoint(Point3D point, Color color);
}
