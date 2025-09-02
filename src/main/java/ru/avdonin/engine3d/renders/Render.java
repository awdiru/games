package ru.avdonin.engine3d.renders;

import lombok.Getter;
import ru.avdonin.engine3d.storage.Storage;
import ru.avdonin.engine3d.util.*;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

@Getter
public abstract class Render extends JPanel {
    protected final Storage storage = new Storage();
    protected Camera3D camera = new Camera3D(new Vector3D(0, 0, 1));

    public void addObject(String name, Object3D o) {
        storage.add(name, o);
        repaint();
    }

    public Object3D getObject(String name) {
        return storage.get(name);
    }

    public Map<String, Object3D> getObjects() {
        return storage.getObjects();
    }

    public Map<String, Light3D> getLights() {
        return storage.getLights();
    }

    public abstract void renderPoint(Point3D point, Color color);

    public abstract void renderLine(Edge3D edge3D, Color color);
}
