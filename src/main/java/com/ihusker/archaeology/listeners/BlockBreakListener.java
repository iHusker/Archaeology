package com.ihusker.archaeology.listeners;

import com.ihusker.archaeology.Archaeology;
import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.managers.ArtifactManager;
import com.ihusker.archaeology.managers.PlayerManager;
import com.ihusker.archaeology.utilities.storage.data.Config;
import com.ihusker.archaeology.utilities.storage.data.Message;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BlockBreakListener implements Listener {

    private final ArtifactManager artifactManager;
    private final PlayerManager playerManager;
    private final Set<Material> blocks = new HashSet<>();

    public BlockBreakListener(Archaeology archaeology) {
        this.artifactManager = archaeology.getArtifactManager();
        this.playerManager = archaeology.getPlayerManager();

        Arrays.asList((String[]) Config.BLOCKS).forEach(s -> {
            Material material = Material.matchMaterial(s);
            if(material != null) blocks.add(material);
        });
    }

    @EventHandler
    public void onEvent(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        if (!Arrays.asList((String[]) Config.WORLDS).contains(event.getPlayer().getWorld().getName())) return;
        if (!blocks.contains(event.getBlock().getType())) return;

        boolean silkTouch = false;
        if (Config.SILK_TOUCH) {
            ItemStack handItem = event.getPlayer().getInventory().getItemInMainHand();
            ItemMeta itemMeta = handItem.getItemMeta();
            if (itemMeta != null) {
                if (itemMeta.getEnchants().containsKey(Enchantment.SILK_TOUCH)) silkTouch = true;
            }
        }

        Player player = event.getPlayer();

        int divide = (silkTouch) ? 2 : 1;
        double chance = new Random().nextInt((int) playerManager.getChance(player.getUniqueId()));

        if (chance / divide == 0) {
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
