package com.ihusker.archaeology.managers;

import com.google.gson.reflect.TypeToken;
import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.utilities.storage.JsonStorage;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import java.lang.reflect.Type;
import java.util.*;

public class ArtifactManager {

    private Set<Artifact> artifacts = new HashSet<>();

    public void deserialize(Plugin plugin) {
        Type type = new TypeToken<Set<Artifact>>() {}.getType();
        artifacts = JsonStorage.read(plugin, "artifacts.json", type);
        plugin.getLogger().info(artifacts.size() + " Artifacts loaded.");
    }

    public void serialize(Plugin plugin) {
        JsonStorage.write(plugin,"artifacts.json", artifacts);
        plugin.getLogger().info("Saved " + artifacts.size() + " Artifacts.");
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

    public Set<Artifact> getArtifacts() {
        return Collections.unmodifiableSet(artifacts);
    }
}
