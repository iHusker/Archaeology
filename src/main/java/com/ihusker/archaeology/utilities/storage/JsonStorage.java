package com.ihusker.archaeology.utilities.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonStorage {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void write(Path path, Object object) {
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

    public static <C> C read(Path path, Type type) {
        try {
            if(!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader bufferedReader =  Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return GSON.fromJson(bufferedReader,type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
