package ru.avdonin.engine3d.helpers;

import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;
import ru.avdonin.engine3d.storage.SceneStorage;

public class SavedHelper {
    /**
     * Сохранить объект в сцену
     *
     * @param name имя объекта
     * @param obj  сохраняемы объект
     */
    public static void addObjectToScene(String name, AbstractObject3D<?> obj) {
        SerializeHelper.validateNameObject(name);
        SceneStorage storage = Context.get(Constants.STORAGE_KEY);
        int size = storage.getObjects().size();
        storage.put(name + size, obj);
    }

    /**
     * Сохранить объект в сцену
     *
     * @param obj сохраняемы объект
     */
    public static void addObjectToScene(AbstractObject3D<?> obj) {
        String name = obj.getClass().getSimpleName();
        SerializeHelper.validateNameObject(name);
        SceneStorage storage = Context.get(Constants.STORAGE_KEY);
        int size = storage.getObjects().size();
        storage.put(name + size, obj);
    }
}
