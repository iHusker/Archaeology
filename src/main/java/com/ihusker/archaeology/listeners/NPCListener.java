package com.ihusker.archaeology.listeners;

import com.ihusker.archaeology.managers.ArtifactManager;
import com.ihusker.archaeology.utilities.storage.data.Config;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Arrays;

public class NPCListener implements Listener {

    private final ArtifactManager artifactManager;

    public NPCListener(ArtifactManager artifactManager) {
        this.artifactManager = artifactManager;
    }

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();

        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        if (!entity.hasMetadata("NPC")) return;
        if (entity.getCustomName() == null) return;

        String name = ChatColor.stripColor(WordUtils.capitalizeFully(entity.getCustomName().toLowerCase()));
        if (Arrays.asList((String[])Config.NAMES).contains(name.toLowerCase())) artifactManager.redeem(event.getPlayer());
    }
}
