package com.ihusker.archaeology.listeners;

import com.ihusker.archaeology.Archaeology;
import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.utilities.general.Chat;
import com.ihusker.archaeology.utilities.general.Message;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
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
        if(player.getGameMode() != GameMode.SURVIVAL) return;
        if(!archaeology.getDataManager().getMaterials().contains(event.getBlock().getType())) return;

        if (new Random().nextInt(archaeology.getDataManager().getChance(player)) == 0) {
            Artifact artifact = archaeology.getDataManager().getWeightedArtifact();

            if (artifact != null) {
                ItemStack itemStack = artifactItem(artifact);
                if(itemStack == null) return;
                Item item = event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);

                item.setGlowing(true);
                if(itemStack.getItemMeta() != null) item.setCustomName(itemStack.getItemMeta().getDisplayName());
                item.setCustomNameVisible(true);

                player.sendMessage(Message.PREFIX.toString() + Message.FOUND.toString()
                        .replace("{name}", WordUtils.capitalizeFully(artifact.getName().toLowerCase().replace("_", " ")))
                );

                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
        }
    }

    public ItemStack artifactItem(Artifact artifact) {
        ItemStack itemStack = new ItemStack(artifact.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        String displayName = Message.ARTIFACT_NAME.toString()
                .replace("{color}", "&" + artifact.getRarity().getColor().getChar())
                .replace("{name}", WordUtils.capitalizeFully(artifact.getName().toLowerCase().replace("_", " ")));

        List<String> updatedLore = new ArrayList<>();

        for(String string : Message.ARTIFACT_LORE.toList()) {
            string = string.replace("{description}", artifact.getDescription());
            string = string.replace("{color}", "&" + artifact.getRarity().getColor().getChar());
            string = string.replace("{name}", WordUtils.capitalizeFully(artifact.getRarity().getName().toLowerCase()));
            updatedLore.add(string);
        }
        if(itemMeta == null) return itemStack;

        itemMeta.getPersistentDataContainer().set(new NamespacedKey(archaeology, "Artifact"), PersistentDataType.DOUBLE, artifact.getPrice());
        itemMeta.setDisplayName(Chat.color(displayName));
        itemMeta.setLore(updatedLore.stream().map(Chat::color).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
