package com.ihusker.archaeology.commands;

import com.ihusker.archaeology.Archaeology;
import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.utilities.command.AbstractCommand;
import com.ihusker.archaeology.utilities.general.Message;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginDescriptionFile;

public class ArchaeologyCommand extends AbstractCommand {

    private final Archaeology archaeology;

    public ArchaeologyCommand(Archaeology archaeology) {
        super("archaeology");
        this.archaeology = archaeology;
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args[0].equalsIgnoreCase("version")) {
            PluginDescriptionFile descriptionFile = archaeology.getDescription();
            player.sendMessage(Message.PREFIX.toString());
            player.sendMessage("Author(s): " + descriptionFile.getAuthors());
            player.sendMessage("Version: " + descriptionFile.getVersion());
            return;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            archaeology.reloadConfig();
            archaeology.getArtifactManager().deserialize(archaeology);
            archaeology.getDataManager().deserialize(archaeology);
            player.sendMessage(Message.PREFIX.toString() + "You have reloaded the configuration.");
            return;
        }

        if(args[0].equalsIgnoreCase("redeem")) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) return;

            for (Artifact artifact : archaeology.getArtifactManager().getArtifacts()) {
                if (artifact.getMaterial() == itemStack.getType()) {

                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (itemMeta != null) {
                        NamespacedKey namespacedKey = new NamespacedKey(archaeology, "Artifact");
                        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

                        if (container.has(namespacedKey, PersistentDataType.DOUBLE)) {

                            Double aDouble = container.get(namespacedKey, PersistentDataType.DOUBLE);

                            if (aDouble != null) {
                                EconomyResponse economyResponse = archaeology.getEconomy().depositPlayer(player, artifact.getPrice());
                                if (economyResponse.transactionSuccess()) player.sendMessage(Message.PREFIX.toString() + Message.REDEEM.toString().replace("{amount}", String.valueOf(aDouble)));
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
