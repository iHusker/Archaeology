package com.ihusker.archaeology.managers;

import com.google.gson.reflect.TypeToken;
import com.ihusker.archaeology.Archaeology;
import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.utilities.general.Chat;
import com.ihusker.archaeology.utilities.storage.JsonStorage;
import com.ihusker.archaeology.utilities.storage.data.Message;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ArtifactManager {

    private Set<Artifact> artifacts = new HashSet<>();

    private final Archaeology archaeology;

    public ArtifactManager(Archaeology archaeology) {
        this.archaeology = archaeology;
    }

    public void deserialize() {
        Type type = new TypeToken<Set<Artifact>>() {}.getType();
        artifacts = JsonStorage.read(archaeology, "artifacts.json", type);
        archaeology.getLogger().info(artifacts.size() + " Artifacts loaded.");
    }

    public void serialize() {
        JsonStorage.write(archaeology,"artifacts.json", artifacts);
        archaeology.getLogger().info("Saved " + artifacts.size() + " Artifacts.");
    }

    public int getChance(Player player)  {
        int chance = Integer.MAX_VALUE;
        for(PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            String permission = permissionAttachmentInfo.getPermission();
            if(permission.startsWith("archaeology.chance.")) {
                String[] split = permission.split("\\.");
                int newChance = Integer.parseInt(split[2]);
                if(newChance < chance) chance = newChance;
            }
        }
        return (chance == 0) ? 1000 : chance;
    }

    public void redeem(Player player) {
        int amount = 0;
        int totalPrice = 0;

        ItemStack[] contents = player.getInventory().getContents();
        for (ItemStack itemStack : contents) {
            if (itemStack != null) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
                    String string = container.get(archaeology.getKey(), PersistentDataType.STRING);
                    if (string != null) {
                        Artifact artifact = archaeology.getArtifactManager().getArtifact(string);
                        if (artifact != null) {
                            amount += 1;
                            totalPrice += artifact.getPrice();
                            artifact.getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                                    .replace("{player}", player.getName()))
                            );
                            itemStack.setAmount(itemStack.getAmount() - 1);
                        }
                    }
                }
            }
        }

        EconomyResponse economyResponse = archaeology.getEconomy().depositPlayer(player, totalPrice);
        if (economyResponse.transactionSuccess()) {
            player.sendMessage(Message.PREFIX.toString() + Message.REDEEM.toString()
                    .replace("{price}", String.valueOf(totalPrice))
                    .replace("{amount}", String.valueOf(amount))
            );
        }
    }

    public ItemStack artifactItem(Artifact artifact) {
        ItemStack itemStack = new ItemStack(artifact.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();

        String displayName = Message.ARTIFACT_NAME.toString()
                .replace("{color}", "&" + artifact.getColor().getChar())
                .replace("{name}", artifact.toString());

        //Copy constructor is put in place so that it doesn't override the static message variable
        List<String> strings = new ArrayList<>(Arrays.asList((String[]) Message.ARTIFACT_LORE));
        String[] lore = new String[strings.size()];

        for(int i = 0; i < strings.size(); i++) {
            strings.set(i , strings.get(i).replace("{description}", artifact.getDescription()));
            strings.set(i , strings.get(i).replace("{chance}", String.valueOf(artifact.getChance())));
            strings.set(i , strings.get(i).replace("{color}", "&" + artifact.getColor().getChar()));
            lore[i] = strings.get(i);
        }

        if(itemMeta == null) return itemStack;

        itemMeta.getPersistentDataContainer().set(archaeology.getKey(), PersistentDataType.STRING, artifact.getName());
        itemMeta.setDisplayName(Chat.color(displayName));
        itemMeta.setLore(Arrays.stream(lore).map(Chat::color).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Artifact getWeightedArtifact() {
        int totalWeight = 0;
        List<Artifact> artifacts = new ArrayList<>(this.artifacts);

        for(Artifact artifact : artifacts) totalWeight += artifact.getChance();

        int index = new Random().nextInt(totalWeight);
        int sum = 0;
        int i = 0;

        while (sum < index) sum += artifacts.get(i++).getChance();

        return artifacts.get(Math.max(0, i - 1));
    }

    public Artifact getArtifact(String name) {
        return artifacts.stream().filter(artifact -> artifact.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Set<Artifact> getArtifacts() {
        return Collections.unmodifiableSet(artifacts);
    }
}
