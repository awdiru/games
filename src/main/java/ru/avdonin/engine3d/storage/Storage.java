package ru.avdonin.engine3d.storage;

import lombok.Getter;
import ru.avdonin.engine3d.util.Light3D;
import ru.avdonin.engine3d.util.Object3D;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Storage {
    private final Map<String, Object3D> objects = new HashMap<>();
    private final Map<String, Light3D> lights = new HashMap<>();

    public void add(String name, Object3D obj) {
        if(obj instanceof Light3D)
            lights.put(name, (Light3D) obj);
        objects.put(name, obj);
    }

    public <T extends Object3D> T get (String name) {
        return (T) objects.get(name);
    }

    public void remove(String name) {
        Object3D obj = objects.get(name);
        if(obj instanceof Light3D)
            lights.remove(name);
        objects.remove(name);
    }

    public void clear() {
        lights.clear();
        objects.clear();
    }
}
