package com.ihusker.archaeology.commands;

import com.ihusker.archaeology.Archaeology;
import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.utilities.command.AbstractCommand;
import com.ihusker.archaeology.utilities.general.Message;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class RedeemCommand extends AbstractCommand {

    private final Archaeology archaeology;

    public RedeemCommand(Archaeology archaeology) {
        super("redeem");
        this.archaeology = archaeology;
    }

    @Override
    public void execute(Player player, String[] args) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.AIR) return;

        for (Artifact artifact : archaeology.getDataManager().getArtifacts()) {
            if (artifact.getMaterial() == itemStack.getType()) {

                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    NamespacedKey namespacedKey = new NamespacedKey(archaeology, "Artifact");
                    PersistentDataContainer container = itemMeta.getPersistentDataContainer();

                    if (container.has(namespacedKey, PersistentDataType.DOUBLE)) {

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
