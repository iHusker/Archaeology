package com.ihusker.archaeology;

import com.ihusker.archaeology.commands.RedeemCommand;
import com.ihusker.archaeology.listeners.BlockBreakListener;
import com.ihusker.archaeology.listeners.NPCListener;
import com.ihusker.archaeology.managers.DataManager;
import com.ihusker.archaeology.utilities.command.Command;
import com.ihusker.archaeology.utilities.general.Message;
import com.ihusker.archaeology.utilities.storage.YamlStorage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Archaeology extends JavaPlugin {

    private Economy economy = null;
    private final DataManager dataManager = new DataManager();

    @Override
    public void onEnable() {
        if(registerDependencies()) {
            saveDefaultConfig();
            dataManager.deserialize(this);

            registerCommands(new RedeemCommand(this));

            registerListener(new BlockBreakListener(this), new NPCListener(this));
            registerMessages(new YamlStorage("messages.yml").createNewFile(this));

        } else {
            getLogger().warning("You need to install vault and citizens for plugin to work...");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private void registerCommands(Command... commands) {
        Arrays.asList(commands).forEach(command -> {
            PluginCommand pluginCommand = getCommand(command.name());
            if(pluginCommand != null) pluginCommand.setExecutor(command.commandExecutor());
        });
        getLogger().warning("Registered Commands.");
    }

    private void registerListener(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
        getLogger().warning("Registered Listeners.");
    }

    public void registerMessages(YamlStorage yamlStorage) {
        Message.setConfiguration(yamlStorage.getConfig());

        for (Message value : Message.values()) {
            if (value.getDefaultList() != null) {
                yamlStorage.getConfig().addDefault(value.getPath(), value.getDefaultList());
            } else {
                yamlStorage.getConfig().addDefault(value.getPath(), value.getDefault());
            }
        }

        yamlStorage.getConfig().options().copyDefaults(true);
        yamlStorage.saveConfig();
        getLogger().warning("Registered Messages.");
    }

    private boolean registerDependencies() {

        if (getServer().getPluginManager().getPlugin("Citizens") == null) return false;
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();

        getLogger().warning("Registered Dependencies.");
        return true;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public Economy getEconomy() {
        return economy;
    }
}
