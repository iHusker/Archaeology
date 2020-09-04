package com.ihusker.archaeology;

import com.ihusker.archaeology.commands.ArchaeologyCommand;
import com.ihusker.archaeology.listeners.BlockBreakListener;
import com.ihusker.archaeology.listeners.NPCListener;
import com.ihusker.archaeology.listeners.PlayerListener;
import com.ihusker.archaeology.managers.ArtifactManager;
import com.ihusker.archaeology.managers.DataManager;
import com.ihusker.archaeology.managers.PlayerManager;
import com.ihusker.archaeology.utilities.command.Command;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Archaeology extends JavaPlugin {

    private Economy economy = null;
    private final NamespacedKey namespacedKey = new NamespacedKey(this, "Artifact");

    private final ArtifactManager artifactManager = new ArtifactManager(this);
    private final DataManager dataManager = new DataManager();
    private final PlayerManager playerManager = new PlayerManager();

    @Override
    public void onEnable() {
        if(registerDependencies()) {
            artifactManager.deserialize();
            dataManager.deserialize(this);

            registerCommands(new ArchaeologyCommand(this));

            registerListener(
                    new BlockBreakListener(this),
                    new NPCListener(artifactManager),
                    new PlayerListener(this)
            );

            for(Player player : getServer().getOnlinePlayers()) playerManager.deserialize(player.getUniqueId());

        } else {
            getLogger().warning("You need to install both vault and citizens for plugin to work...");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        artifactManager.serialize();

        for(Player player : getServer().getOnlinePlayers()) playerManager.serialize(player.getUniqueId());
    }

    private void registerCommands(Command... commands) {
        Arrays.asList(commands).forEach(command -> {
            PluginCommand pluginCommand = getCommand(command.name());
            if(pluginCommand != null) pluginCommand.setExecutor(command.commandExecutor());
        });
    }

    private void registerListener(Listener... listeners) {
        Arrays.asList(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private boolean registerDependencies() {

        if (getServer().getPluginManager().getPlugin("Citizens") == null) return false;
        if (getServer().getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return false;
        economy = rsp.getProvider();
        return true;
    }

    public NamespacedKey getKey() {
        return namespacedKey;
    }

    public ArtifactManager getArtifactManager() {
        return artifactManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Economy getEconomy() {
        return economy;
    }
}
