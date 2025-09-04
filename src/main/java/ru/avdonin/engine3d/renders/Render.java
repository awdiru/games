package ru.avdonin.engine3d.renders;

import lombok.Getter;
import lombok.Setter;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.util.objects.*;

import javax.swing.*;
import java.awt.*;

@Getter
@Setter
public abstract class Render extends JPanel {
    public static final String MAIN_CAMERA_NAME = "main_camera";
    protected Camera3D camera = new Camera3D(new Vector3D(0, 0, 1));
    protected SceneStorage sceneStorage;

    public Render() {
        this(new SceneStorage());
    }

    public Render(SceneStorage storage) {
        this.sceneStorage = storage;
        Camera3D camera = this.sceneStorage.get(MAIN_CAMERA_NAME);
        if (camera == null)
            this.sceneStorage.add(MAIN_CAMERA_NAME, this.camera);
        else this.camera = camera;
    }
}
