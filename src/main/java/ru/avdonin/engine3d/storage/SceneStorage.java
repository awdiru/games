package ru.avdonin.engine3d.storage;

import lombok.Getter;
import ru.avdonin.engine3d.util.Camera3D;
import ru.avdonin.engine3d.util.Light3D;
import ru.avdonin.engine3d.util.Object3D;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SceneStorage {
    private final Map<String, Object3D> objects = new HashMap<>();
    private final Map<String, Light3D> lights = new HashMap<>();
    private final Map<String, Camera3D> cameras = new HashMap<>();

    public void add(String key, Object3D obj) {
        if (obj instanceof Light3D)
            lights.put(key, (Light3D) obj);
        if (obj instanceof Camera3D)
            cameras.put(key, (Camera3D) obj);
        objects.put(key, obj);
    }

    public <T extends Object3D> T get(String key) {
        return (T) objects.get(key);
    }

    public void remove(String key) {
        objects.remove(key);
        lights.remove(key);
    }
}
