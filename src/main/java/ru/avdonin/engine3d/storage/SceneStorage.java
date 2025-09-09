package ru.avdonin.engine3d.storage;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Camera3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Light3D;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SceneStorage {
    private final Map<String, AbstractObject3D<?>> objects = new HashMap<>();
    private final Map<String, Light3D> lights = new HashMap<>();
    private final Map<String, Camera3D> cameras = new HashMap<>();
    @Setter
    private AbstractObject3D<?> selectedObject = null;

    public void put(String key, AbstractObject3D<?> obj) {
        if (obj instanceof Light3D)
            lights.put(key, (Light3D) obj);
        if (obj instanceof Camera3D)
            cameras.put(key, (Camera3D) obj);
        objects.put(key, obj);
    }

    public <T extends AbstractObject3D<?>> T get(String key) {
        return (T) objects.get(key);
    }

    public void remove(String key) {
        objects.remove(key);
        lights.remove(key);
    }
}
