package ru.avdonin.engine3d;

import ru.avdonin.engine3d.storage.SceneStorage;

import java.util.HashMap;
import java.util.Map;

public class Context {
    private final static Map<String, Object> context = new HashMap<>();

    static {
        context.put(Constants.STORAGE_KEY, new SceneStorage());
        context.put(Constants.IS_SKELETON_KEY, Boolean.FALSE);
    }

    public static <V> V get(String key) {
        return (V) context.get(key);
    }

    public static void put(String key, Object value) {
        context.put(key, value);
    }

    public static void remove(String key) {
        context.remove(key);
    }
}
