package com.ihusker.archaeology.managers;

import com.google.common.util.concurrent.AtomicDouble;
import com.google.gson.reflect.TypeToken;
import com.ihusker.archaeology.Archaeology;
import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.utilities.general.Chat;
import com.ihusker.archaeology.utilities.storage.types.JsonStorage;
import com.ihusker.archaeology.utilities.storage.data.Message;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    public boolean redeem(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.AIR) return true;

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return true;

        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        String string = container.get(archaeology.getKey(), PersistentDataType.STRING);
        if (string == null) return true;

        Artifact artifact = archaeology.getArtifactManager().getArtifact(string);
        if (artifact == null) return true;

        artifact.getCommands().forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command
                .replace("{player}", player.getName()))
        );
        player.getInventory().getItemInMainHand().setAmount(itemStack.getAmount() -1);

        EconomyResponse economyResponse = archaeology.getEconomy().depositPlayer(player, artifact.getPrice());
        if (economyResponse.transactionSuccess()) player.sendMessage(Message.PREFIX.toString() + Message.REDEEM.toString()
                .replace("{price}", String.valueOf(artifact.getPrice()))
        );

        return false;
    }

    public ItemStack artifactItem(Artifact artifact) {
        ItemStack itemStack = new ItemStack(artifact.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();

        String displayName = Message.ARTIFACT_NAME.toString()
                .replace("{color}", "&" + artifact.getColor().getChar())
                .replace("{type}", (artifact.getType() != null) ? artifact.getType(): "Artifact")
                .replace("{name}", artifact.toString());

        //Copy constructor is put in place so that it doesn't override the static message variable
        List<String> strings = new ArrayList<>(Arrays.asList((String[]) Message.ARTIFACT_LORE));
        String[] lore = new String[strings.size()];

        for(int i = 0; i < strings.size(); i++) {
            strings.set(i , strings.get(i).replace("{type}", (artifact.getType() != null) ? artifact.getType(): "Artifact"));
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
        Map<Integer, Double> weights = new HashMap<>();
        List<Artifact> artifacts = new ArrayList<>(this.artifacts);

        for(int i = 0; i < artifacts.size(); i++) weights.put(i, artifacts.get(i).getChance());

        double chance = Math.random() * weights.values().stream().reduce(0D, Double::sum);
        AtomicDouble needle = new AtomicDouble();
        return weights.entrySet().stream()
                .filter(entry-> needle.addAndGet(entry.getValue()) >= chance)
                .findFirst().map(Map.Entry::getKey).map(artifacts::get)
                .orElse(null);
    }

    public Artifact getArtifact(String name) {
        return artifacts.stream().filter(artifact -> artifact.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Set<Artifact> getArtifacts() {
        return Collections.unmodifiableSet(artifacts);
    }
}
