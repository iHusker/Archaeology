package com.ihusker.archaeology.managers;

import com.ihusker.archaeology.utilities.storage.data.Config;
import com.ihusker.archaeology.utilities.storage.data.Message;
import com.ihusker.archaeology.utilities.storage.YamlStorage;
import org.bukkit.plugin.Plugin;

public class DataManager {

    private final YamlStorage configStorage = new YamlStorage("config.yml");
    private final YamlStorage messageStorage = new YamlStorage("message.yml");

    public void deserialize(Plugin plugin) {
        Config.Setup(configStorage.createNewFile(plugin));
        Config.Deserialize(configStorage.getConfig());

        Message.Setup(messageStorage.createNewFile(plugin));
        Message.Deserialize(messageStorage.getConfig());
    }
}
