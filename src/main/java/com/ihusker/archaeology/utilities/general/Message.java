package com.ihusker.archaeology.utilities.general;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

public enum Message {

    PERMISSIONS("permissions","&c&lError: &7You do not seem to have permissions for that command."),
    PREFIX("prefix", "&e&lArchaeology: &7"),
    FOUND("found", "You have found a &f{name} &7artifact"),
    REDEEM("redeem","You have received &a${amount} &7from the artifact"),
    ARTIFACT_NAME("artifact.name", "{color}&l{name} &7[Artifact]"),
    ARTIFACT_LORE("artifact.lore", Arrays.asList(
            "&7{description}",
            "&7Rarity: {color}{name}",
            "",
            "&f&oRedeem at the Archaeologist."
    ));


    private String path, def;
    private List<String> list;
    private static FileConfiguration config;

    Message(String path, String def) {
        this.path = path;
        this.def = def;
    }

    Message(String path, List<String> list) {
        this.path = path;
        this.list = list;
    }

    public String getDefault() {
        return this.def;
    }

    public List<String> getDefaultList() {
        return list;
    }

    public static void setConfiguration(FileConfiguration configuration) {
        config = configuration;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return Chat.color(config.getString(this.path, this.def));
    }

    public List<String> toList() {
        return config.getStringList(this.path);
    }
}
