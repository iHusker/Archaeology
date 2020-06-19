package com.ihusker.archaeology.listeners;

import com.ihusker.archaeology.Archaeology;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class PlayerListener implements Listener {

    private final Archaeology archaeology;

    public PlayerListener(Archaeology archaeology) {
        this.archaeology = archaeology;
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
