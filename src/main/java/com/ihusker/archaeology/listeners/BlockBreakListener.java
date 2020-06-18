package com.ihusker.archaeology.listeners;

import com.ihusker.archaeology.Archaeology;
import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.managers.ArtifactManager;
import com.ihusker.archaeology.utilities.general.Chat;
import com.ihusker.archaeology.utilities.general.Message;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockBreakListener implements Listener {

    private final Archaeology archaeology;

    public BlockBreakListener(Archaeology archaeology) {
        this.archaeology = archaeology;
    }


    @EventHandler
    public void onEvent(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(player.getGameMode() != GameMode.SURVIVAL) return;

        if(!archaeology.getDataManager().getMaterials().contains(block.getType().name())) return;
        if(!archaeology.getDataManager().getWorlds().contains(player.getWorld().getName())) return;

        ArtifactManager artifactManager = archaeology.getArtifactManager();

        if (new Random().nextInt(artifactManager.getChance(player)) == 0) {
            Artifact artifact = artifactManager.getWeightedArtifact();

            if (artifact != null) {
                ItemStack itemStack = artifactItem(artifact);
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

    public ItemStack artifactItem(Artifact artifact) {
        ItemStack itemStack = new ItemStack(artifact.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        String displayName = Message.ARTIFACT_NAME.toString()
                .replace("{color}", "&" + artifact.getColor().getChar())
                .replace("{name}", WordUtils.capitalizeFully(artifact.getName().toLowerCase().replace("_", " ")));

        String[] strings = (String[]) Message.ARTIFACT_LORE;
        String[] lore = new String[strings.length];

        for(int i = 0; i < strings.length; i++) {
            strings[i] = strings[i].replace("{description}", artifact.getDescription());
            strings[i] = strings[i].replace("{color}", "&" + artifact.getColor().getChar());
            strings[i] = strings[i].replace("{chance}", String.valueOf(artifact.getChance()));
            lore[i] = strings[i];
        }

        if(itemMeta == null) return itemStack;

        itemMeta.getPersistentDataContainer().set(archaeology.getKey(), PersistentDataType.DOUBLE, artifact.getPrice());
        itemMeta.setDisplayName(Chat.color(displayName));
        itemMeta.setLore(Arrays.stream(lore).map(Chat::color).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
