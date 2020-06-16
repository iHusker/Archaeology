package com.ihusker.archaeology.managers;

import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.data.Rarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class DataManager {

    private final Set<Material> materials = new HashSet<>();
    private final Set<Rarity> rarities = new HashSet<>();
    private final Set<Artifact> artifacts = new HashSet<>();

    public void deserialize(Plugin plugin) {

        for (String string : plugin.getConfig().getStringList("blocks")) {
            Material material = Material.matchMaterial(string);
            if(material == null) {
                plugin.getLogger().info("You much provide valid materials for blocks.");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                return;
            } else {
                materials.add(material);
            }
        }

        for (String string : plugin.getConfig().getStringList("rarities")) {
            String[] strings = string.split(":");

            try {
                rarities.add(new Rarity(strings[0], Integer.parseInt(strings[1]), ChatColor.valueOf(strings[2])));
            } catch (Exception exc) {
                plugin.getLogger().info("You much provide valid configuration for rarities.");
                plugin.getServer().getPluginManager().disablePlugin(plugin);
                return;
            }
        }

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("artifacts");
        if(section == null) {
            plugin.getLogger().info("Artifacts have not been setup correctly in the config.");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } else {

            for(String string : section.getKeys(false)) {
                String key = "artifacts." + string;

                Rarity rarity = getRarity(plugin.getConfig().getString(key + ".rarity"));
                if(rarity == null) {
                    plugin.getLogger().severe("The " + string + " artifact does not have a correct rarity assigned to it.");
                    break;
                }

                String description = plugin.getConfig().getString(key + ".description");
                String material = plugin.getConfig().getString(key + ".material");
                int price = plugin.getConfig().getInt(key + ".price");

                artifacts.add(new Artifact(string, description, rarity, (material != null) ? Material.matchMaterial(material) : Material.BONE, price));
            }
        }
    }

    public Artifact getWeightedArtifact() {
        int totalWeight = 0;
        List<Artifact> artifacts = new ArrayList<>(this.artifacts);

        for(Artifact artifact : artifacts) totalWeight += artifact.getRarity().getChance();

        int index = new Random().nextInt(totalWeight);
        int sum = 0;
        int i = 0;

        while (sum < index) sum += artifacts.get(i++).getRarity().getChance();

        return artifacts.get(Math.max(0, i - 1));
    }

    public int getChance(Player player)  {
        int chance = 0;
        for(PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            String permission = permissionAttachmentInfo.getPermission();
            if(permission.startsWith("archaeology.chance.")) {
                String[] split = permission.split("\\.");
                chance += Integer.parseInt(split[2]);
            }
        }
        return (chance == 0) ? 1000 : chance;
    }


    public Rarity getRarity(String name) {
        return rarities.stream().filter(rarity -> rarity.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Set<Artifact> getArtifacts() {
        return artifacts;
    }

    public Set<Material> getMaterials() {
        return materials;
    }
}
