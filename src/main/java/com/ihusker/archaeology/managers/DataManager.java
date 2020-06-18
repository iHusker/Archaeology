package com.ihusker.archaeology.managers;

import com.ihusker.archaeology.utilities.general.Message;
import com.ihusker.archaeology.utilities.storage.YamlStorage;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class DataManager {

    private List<String> worlds = new ArrayList<>();
    private List<String> materials = new ArrayList<>();

    private final YamlStorage yamlStorage = new YamlStorage("message.yml");

    public void deserialize(Plugin plugin) {
        materials = plugin.getConfig().getStringList("blocks");
        worlds = plugin.getConfig().getStringList("worlds");

        Message.Setup(yamlStorage.createNewFile(plugin));
        Message.Deserialize(yamlStorage.getConfig());
    }


    public List<String> getWorlds() {
        return Collections.unmodifiableList(worlds);
    }

    public List<String> getMaterials() {
        return Collections.unmodifiableList(materials);
    }

    public YamlStorage getYamlStorage() {
        return yamlStorage;
    }
}
