package com.ihusker.archaeology.managers;

import com.ihusker.archaeology.utilities.storage.DataHandler;
import com.ihusker.archaeology.utilities.storage.data.Config;
import com.ihusker.archaeology.utilities.storage.data.Message;
import com.ihusker.archaeology.utilities.storage.types.YamlStorage;
import org.bukkit.plugin.Plugin;

public class DataManager {

    private final YamlStorage configStorage = new YamlStorage("config.yml");
    private final YamlStorage messageStorage = new YamlStorage("message.yml");

    public void deserialize(Plugin plugin) {
        DataHandler.Setup(configStorage.createNewFile(plugin), Config.class);
        DataHandler.Deserialize(configStorage.getConfig(), Config.class);

        DataHandler.Setup(messageStorage.createNewFile(plugin), Message.class);
        DataHandler.Deserialize(messageStorage.getConfig(), Message.class);
    }
}
