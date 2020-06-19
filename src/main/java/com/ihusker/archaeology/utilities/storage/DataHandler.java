package com.ihusker.archaeology.utilities.storage;

import com.ihusker.archaeology.utilities.general.Chat;
import com.ihusker.archaeology.utilities.storage.types.YamlStorage;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;

public class DataHandler {

    public static void Setup(YamlStorage yamlStorage, Class<?> clazz) {
        for(Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String name = field.getName().toLowerCase().replace("_", "-");
            try {
                yamlStorage.getConfig().addDefault(name, field.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            field.setAccessible(false);
        }
        yamlStorage.getConfig().options().copyDefaults(true);
        yamlStorage.saveConfig();
    }

    public static void Deserialize(FileConfiguration configuration, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            String name = field.getName().toLowerCase().replace("_", "-");
            try {
                if (field.get(null) instanceof String[]) field.set(null, configuration.getStringList(name).stream().map(Chat::color).toArray(String[]::new));
                if (field.get(null) instanceof String) field.set(null, Chat.color(configuration.getString(name)));

                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
