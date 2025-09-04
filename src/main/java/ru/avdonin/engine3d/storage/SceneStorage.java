package ru.avdonin.engine3d.storage;

import lombok.Getter;
import ru.avdonin.engine3d.util.Obj;
import ru.avdonin.engine3d.util.objects.Camera3D;
import ru.avdonin.engine3d.util.objects.Light3D;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SceneStorage {
    private final Map<String, Obj<?>> objects = new HashMap<>();
    private final Map<String, Light3D> lights = new HashMap<>();
    private final Map<String, Camera3D> cameras = new HashMap<>();

    public void add(String key, Obj<?> obj) {
        if (obj instanceof Light3D)
            lights.put(key, (Light3D) obj);
        if (obj instanceof Camera3D)
            cameras.put(key, (Camera3D) obj);
        objects.put(key, obj);
    }

    public <T extends Obj<?>> T get(String key) {
        return (T) objects.get(key);
    }

    public void remove(String key) {
        objects.remove(key);
        lights.remove(key);
    }
}
