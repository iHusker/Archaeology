package com.ihusker.archaeology.utilities.storage.data;

import com.ihusker.archaeology.utilities.general.Chat;
import com.ihusker.archaeology.utilities.storage.YamlStorage;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class Config {

    public static Object BLOCKS = new String[] {
            "GRASS_BLOCK",
            "COBBLESTONE",
            "STONE",
            "ANDESITE",
            "DIORITE",
            "GRANITE",
            "DIRT",
            "COARSE_DIRT",
            "GRAVEL",
            "SAND"
    };

    public static Object NAMES = new String[] { "archaeologist" };
    public static Object WORLDS = new String[] { "world" };

    public static void Setup(YamlStorage yamlStorage) {
        for(Field field : Config.class.getDeclaredFields()) {
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

    public static void Deserialize(FileConfiguration configuration) {
        for (Field field : Config.class.getDeclaredFields()) {
            field.setAccessible(true);

            String name = field.getName().toLowerCase().replace("_", "-");
            try {
                if (field.get(null) instanceof Array) field.set(null, configuration.getStringList(name).stream().map(Chat::color).collect(Collectors.toList()));
                if (field.get(null) instanceof String) field.set(null, Chat.color(configuration.getString(name)));

                field.setAccessible(false);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
