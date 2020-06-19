package com.ihusker.archaeology.utilities.storage.types;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonStorage {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void write(Plugin plugin, String fileName, Object object) {
        Path path = Paths.get(plugin.getDataFolder() + "/" + fileName);
        try {
            if(!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            GSON.toJson(object, bufferedWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <C> C read(Plugin plugin, String fileName, Type type) {
        Path path = Paths.get(plugin.getDataFolder() + "/" + fileName);
        boolean resources = false;
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
                resources = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader;

        if (resources) {
            InputStream inputStream = plugin.getResource("artifacts.json");
            if (inputStream == null) return null;
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            JsonStorage.write(plugin, fileName, GSON.fromJson(bufferedReader, type));
        }

        try {
            bufferedReader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

            return GSON.fromJson(bufferedReader, type);
        } catch (IOException e) {
            return null;
        }
    }
}
