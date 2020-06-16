package com.ihusker.archaeology.listeners;

import com.ihusker.archaeology.Archaeology;
import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.utilities.general.Message;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class NPCListener implements Listener {

    private final Archaeology archaeology;

    public NPCListener(Archaeology archaeology) {
        this.archaeology = archaeology;
    }

    @EventHandler
    public void onClick(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();

        if (!event.getHand().equals(EquipmentSlot.HAND)) return;
        if (!(entity instanceof Player)) return;
        if (!entity.hasMetadata("NPC")) return;
        if (entity.getCustomName() == null) return;

        Player player = event.getPlayer();

        String name = ChatColor.stripColor(WordUtils.capitalizeFully(entity.getCustomName().toLowerCase()));
        if (name.equalsIgnoreCase("Archaeologist")) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) return;

            for (Artifact artifact : archaeology.getDataManager().getArtifacts()) {
                if (artifact.getMaterial() == itemStack.getType()) {
                    Bukkit.getLogger().info("Working");

                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta != null) {
                        Bukkit.getLogger().info("Working 2");
                        NamespacedKey namespacedKey = new NamespacedKey(archaeology, "Artifact");
                        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

                        if (container.has(namespacedKey, PersistentDataType.DOUBLE)) {
                            Bukkit.getLogger().info("Working 2");

                            Double aDouble = container.get(namespacedKey, PersistentDataType.DOUBLE);

                            if (aDouble != null) {
                                EconomyResponse economyResponse = archaeology.getEconomy().depositPlayer(player, artifact.getPrice());
                                if (economyResponse.transactionSuccess())
                                    player.sendMessage(Message.PREFIX.toString() + Message.REDEEM.toString().replace("{amount}", String.valueOf(aDouble)));
                                ItemStack handItem = player.getInventory().getItemInMainHand();
                                handItem.setAmount(handItem.getAmount() - 1);
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
