package com.ihusker.archaeology.utilities.storage.data;


import com.ihusker.archaeology.utilities.general.Chat;
import com.ihusker.archaeology.utilities.storage.YamlStorage;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.stream.Collectors;

public class Message {

    public static Object PERMISSIONS = "&7You do not seem to have permissions for that command.";
    public static Object PREFIX = "&e&lArchaeology: &7";
    public static Object NON_EXISTENT = "The &f{name} &7artifact does not seem to exist.";
    public static Object FOUND = "You have found a &f{name} &7artifact.";
    public static Object RECEIVED = "You have received a &f{name} &7artifact.";
    public static Object OFFLINE = "The player &f{player} &7does not seem to be online.";
    public static Object RELOAD = "You have reloaded the configuration files.";
    public static Object SENT = "You have sent a &f{name} &7artifact to &f{player}&7.";
    public static Object REDEEM = "You have received &a${amount} &7from the artifact.";
    public static Object ARTIFACT_NAME = "{color}&l{name} &7[Artifact]";
    public static Object ARTIFACT_LORE = new String[] {
            "&7{description}",
            "&7Chance: &a{chance}%",
            "",
            "&f&oRedeem at the Archaeologist."
    };

    public static void Setup(YamlStorage yamlStorage) {
        for(Field field : Message.class.getDeclaredFields()) {
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
        for (Field field : Message.class.getDeclaredFields()) {
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
