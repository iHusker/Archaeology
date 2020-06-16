package com.ihusker.archaeology.data;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

public class  Rarity {

    private final String name;
    private final int chance;
    private final ChatColor chatColor;

    public Rarity(String name, int chance, ChatColor chatColor) {
        this.name = name;
        this.chance = chance;
        this.chatColor = chatColor;
    }

    public String getName() {
        return name;
    }

    public int getChance() {
        return chance;
    }

    public ChatColor getColor() {
        return chatColor;
    }

    @Override
    public String toString() {
        return "" + chatColor + ChatColor.BOLD + WordUtils.capitalizeFully(name.toLowerCase());
    }
}
