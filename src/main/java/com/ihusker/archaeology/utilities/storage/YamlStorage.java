package com.ihusker.archaeology.utilities.storage;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class YamlStorage {

    private FileConfiguration config;
    private File configFile;
    private final String fileName;

    public YamlStorage(String fileName) {
        this.fileName = fileName;
    }

    public YamlStorage createNewFile(Plugin plugin) {
        reloadConfig(plugin);
        saveConfig();
        loadConfig();
        return this;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    private void loadConfig() {
        config.options().copyDefaults(true);
        saveConfig();
    }

    public void reloadConfig(Plugin plugin) {
        if (configFile == null) configFile = new File(plugin.getDataFolder(), fileName);
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfig() {
        if (config == null || configFile == null) return;
        try {
            getConfig().save(configFile);
        } catch (final IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }
}
