package ru.avdonin.engine3d;

import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.util.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Saver {
    private final static String OBJ_FILE_EXTENSION = ".objc";
    private final static String SCENE_FILE_EXTENSION = ".scn";

    public void openScene(String path, SceneStorage storage) {
        Path filePath = Path.of(path);
        if (!Files.exists(filePath))
            throw new RuntimeException("Ошибка открытия сцены, файл не существует");

        if (!path.endsWith(SCENE_FILE_EXTENSION))
            throw new RuntimeException("Ошибка открытия сцены, неверное расширения файла");

        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines)
                openObject(line, storage);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка открытия сцены", e);
        }

    }

    public void openObject(String path, SceneStorage storage) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        Object3D obj = getObj(lines.getFirst());

        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < lines.size(); i++)
            builder.append(lines.get(i)).append("\n");

        obj.writeObject(builder.toString());
        storage.add(getName(path), obj);
    }

    public void saveScene(String path, String name, SceneStorage storage) {
        try {
            String p = path.endsWith("/") ? path : path + "/";
            String objectsPath = p + "objects/";
            Path scenePath = Path.of(p + name + SCENE_FILE_EXTENSION);

            Files.createDirectories(scenePath.getParent());
            Files.createDirectories(Path.of(objectsPath));

            try (BufferedWriter writer = Files.newBufferedWriter(scenePath)) {
                for (Map.Entry<String, Object3D> entry : storage.getObjects().entrySet()) {
                    String objName = entry.getKey();
                    Object3D obj = entry.getValue();
                    String fileName = saveObject(objectsPath, objName, obj);
                    writer.write(fileName);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException("Ошибка сохранения сцены " + name, e);
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения сцены", e);
        }
    }

    public String saveObject(String path, String name, Object3D obj) {
        String fullName = (path.endsWith("/") ? path : path + "/") + name + OBJ_FILE_EXTENSION;
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fullName))) {
            writer.write(getObjName(obj));
            writer.newLine();
            writer.write(obj.getString());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения объекта " + fullName, e);
        }
        return fullName;
    }

    private String getObjName(Object3D o) {
        if (o instanceof Light3D)
            return "Light";
        else if (o instanceof Camera3D)
            return "Camera";
        return "Object";
    }

    private Object3D getObj(String name) {
        return switch (name) {
            case "Light" -> new Light3D(new Point3D());
            case "Camera" -> new Camera3D(new Vector3D(0, 0, 0));
            default -> new Object3D();
        };
    }

    private String getName(String path) {
        String[] p = path.split("/");
        String fileName = p[p.length - 1];
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
