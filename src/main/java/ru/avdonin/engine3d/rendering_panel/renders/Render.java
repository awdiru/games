package ru.avdonin.engine3d.rendering_panel.renders;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.rendering_panel.util.objects.Camera3D;
import ru.avdonin.engine3d.rendering_panel.util.objects.Vector3D;

import javax.swing.*;

@Getter
@Setter
public abstract class Render extends JPanel {
    public static final String MAIN_CAMERA_NAME = "main_camera";
    protected Camera3D camera = new Camera3D(new Vector3D(0, 0, 1));

    public Render() {
        SceneStorage storage = getStorage();
        Camera3D camera = storage.get(MAIN_CAMERA_NAME);
        if (camera == null)
            storage.add(MAIN_CAMERA_NAME, this.camera);
        else this.camera = camera;
    }

    public SceneStorage getStorage() {
        return Context.get(Constants.STORAGE_KEY);
    }

    public void setSkeleton(Boolean isSkeleton) {
        Context.put(Constants.IS_SKELETON_KEY, isSkeleton);
    }
}
