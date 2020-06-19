package com.ihusker.archaeology.listeners;

import com.ihusker.archaeology.Archaeology;
import com.ihusker.archaeology.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {

    private final Archaeology archaeology;
    private final PlayerManager playerManager;

    public PlayerListener(Archaeology archaeology) {
        this.archaeology = archaeology;
        this.playerManager = archaeology.getPlayerManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        playerManager.deserialize(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playerManager.serialize(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();
        if (itemStack == null) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if(container.has(archaeology.getKey(), PersistentDataType.STRING)) event.setCancelled(true);
    }
}
