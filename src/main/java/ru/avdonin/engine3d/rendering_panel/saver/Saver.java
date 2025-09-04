package ru.avdonin.engine3d.rendering_panel.saver;

import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.rendering_panel.util.Obj;
import ru.avdonin.engine3d.rendering_panel.util.objects.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Saver {
    private final static String OBJ_FILE_EXTENSION = ".objc";
    private final static String SCENE_FILE_EXTENSION = ".scn";

    public void openScene(String path) {
        SceneStorage storage = getStorage();
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
        Obj<?> obj = getObj(lines.getFirst());

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
                for (Map.Entry<String, Obj<?>> entry : storage.getObjects().entrySet()) {
                    String objName = entry.getKey();
                    Obj<?> obj = entry.getValue();
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

    public String saveObject(String path, String name, Obj<?> obj) {
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

    private String getObjName(Obj<?> o) {
        if (o instanceof Light3D)
            return "Light3D";
        else if (o instanceof Camera3D)
            return "Camera3D";
        else if (o instanceof Vector3D)
            return "Vector3D";
        else if (o instanceof Edge3D)
            return "Edge3D";
        else if (o instanceof Polygon3D)
            return "Polygon3D";
        else if (o instanceof Point3D)
            return "Point3D";
        else if (o instanceof Object3D)
            return "Object3D";
        throw new RuntimeException("Неизвестный класс объекта");
    }

    private Obj<?> getObj(String name) {
        return switch (name) {
            case "Light3D" -> new Light3D();
            case "Camera3D" -> new Camera3D();
            case "Vector3D" -> new Vector3D();
            case "Edge3D" -> new Edge3D();
            case "Polygon3D" -> new Polygon3D();
            case "Point3D" -> new Point3D();
            case "Object3D" -> new Object3D();
            default -> throw new RuntimeException("Неизвестный тип объекта");
        };
    }

    private String getName(String path) {
        String[] p = path.split("/");
        String fileName = p[p.length - 1];
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public SceneStorage getStorage() {
        return Context.get(Constants.STORAGE_KEY);
    }
}
