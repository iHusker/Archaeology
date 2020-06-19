package com.ihusker.archaeology.listeners;

import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.managers.ArtifactManager;
import com.ihusker.archaeology.utilities.storage.data.Config;
import com.ihusker.archaeology.utilities.storage.data.Message;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;
import java.util.Random;

public class BlockBreakListener implements Listener {

    private final ArtifactManager artifactManager;

    public BlockBreakListener(ArtifactManager artifactManager) {
        this.artifactManager = artifactManager;
    }

    @EventHandler
    public void onEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(player.getGameMode() != GameMode.SURVIVAL) return;

        if(!Arrays.asList((String[]) Config.BLOCKS).contains(block.getType().name())) return;
        if(!Arrays.asList((String[]) Config.WORLDS).contains(player.getWorld().getName())) return;

        if (new Random().nextInt(artifactManager.getChance(player)) == 0) {
            Artifact artifact = artifactManager.getWeightedArtifact();

            if (artifact != null) {
                ItemStack itemStack = artifactManager.artifactItem(artifact);
                Item item = event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);

                item.setGlowing(true);
                item.setCustomNameVisible(true);

                if (itemStack.getItemMeta() != null) item.setCustomName(itemStack.getItemMeta().getDisplayName());

                player.sendMessage(Message.PREFIX + Message.FOUND.toString()
                        .replace("{name}", WordUtils.capitalizeFully(artifact.getName().toLowerCase().replace("_", " ")))
                );

                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

                event.setDropItems(false);
            }
        }
    }
}
