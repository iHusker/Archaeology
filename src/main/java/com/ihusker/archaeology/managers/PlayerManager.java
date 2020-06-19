package com.ihusker.archaeology.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, Double> chances = new HashMap<>();

    public void deserialize(UUID uuid) {
        double chance = Double.MAX_VALUE;
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) return;

        for(PermissionAttachmentInfo permissionAttachmentInfo : player.getEffectivePermissions()) {
            String permission = permissionAttachmentInfo.getPermission();
            if(permission.startsWith("archaeology.chance.")) {
                String[] split = permission.split("\\.");
                double newChance = Double.parseDouble(split[2]);
                if(newChance < chance) {
                    chance = newChance;
                    chances.put(uuid, (chance == 0) ? 1000 : chance);
                }
            }
        }
    }

    public void serialize(UUID uuid) {
        chances.remove(uuid);
    }

    public double getChance(UUID uuid) {
        return chances.get(uuid);
    }
}
