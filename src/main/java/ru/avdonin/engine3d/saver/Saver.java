package ru.avdonin.engine3d.saver;

import ru.avdonin.engine3d.Constants;
import ru.avdonin.engine3d.Context;
import ru.avdonin.engine3d.rendering_panel.util.Creatable;
import ru.avdonin.engine3d.storage.SceneStorage;
import ru.avdonin.engine3d.rendering_panel.util.AbstractObject3D;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Saver {
    private final static String OBJ_FILE_EXTENSION = ".objc";
    private final static String SCENE_FILE_EXTENSION = ".scn";

    public static void openScene(String path) {
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

    public static void openObject(String path, SceneStorage storage) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        AbstractObject3D<?> obj = Creatable.newInstance(lines.getFirst());

        StringBuilder builder = new StringBuilder();
        builder.append(lines.get(1));
        for (int i = 2; i < lines.size(); i++)
            builder.append("\n").append(lines.get(i));

        obj.writeObject(builder.toString());
        storage.add(getName(path), obj);
    }

    public static void saveScene(String path, String name) {
        try {
            String p = (path.endsWith("/") ? path : path + "/") + "scene/";
            String objectsPath = p + "objects/";
            Path scenePath = Path.of(p + name + SCENE_FILE_EXTENSION);

            Files.createDirectories(scenePath.getParent());
            clearDirectory(scenePath.getParent());
            Files.createDirectories(Path.of(objectsPath));

            try (BufferedWriter writer = Files.newBufferedWriter(scenePath)) {
                for (Map.Entry<String, AbstractObject3D<?>> entry : getStorage().getObjects().entrySet()) {
                    String objName = entry.getKey();
                    AbstractObject3D<?> obj = entry.getValue();
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

    public static String saveObject(String path, String name, AbstractObject3D<?> obj) {
        String fullName = (path.endsWith("/") ? path : path + "/") + name + OBJ_FILE_EXTENSION;
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fullName))) {
            writer.write(obj.getClass().getName());
            writer.newLine();
            writer.write(obj.serialize(0));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения объекта " + fullName, e);
        }
        return fullName;
    }

    private static String getName(String path) {
        String[] p = path.split("/");
        String fileName = p[p.length - 1];
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    private static void clearDirectory(Path directory) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path file : stream) {
                if (Files.isRegularFile(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        System.err.println("Ошибка при удалении " + file + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при удалении", e);
        }
    }

    public static SceneStorage getStorage() {
        return Context.get(Constants.STORAGE_KEY);
    }
}
