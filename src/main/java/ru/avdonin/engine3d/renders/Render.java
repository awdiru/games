package ru.avdonin.engine3d.renders;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.util.*;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public abstract class Render extends JPanel {
    protected final Camera3D camera = new Camera3D(new Vector3D(0, 0, 1));
    protected SceneStorage sceneStorage = new SceneStorage();

    public Render() {
        sceneStorage.add("camera", camera);
    }

    public void addObject(String name, Object3D o) {
        if (o instanceof Light3D)
            sceneStorage.getLights().put(name, (Light3D) o);
        sceneStorage.add(name, o);
        repaint();
    }

    public Object3D getObject(String name) {
        return sceneStorage.get(name);
    }

    public abstract void renderPoint(Point3D point, Color color);

    public abstract void renderLine(Edge3D edge3D, Color color);

}
