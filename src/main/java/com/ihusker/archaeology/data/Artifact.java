package com.ihusker.archaeology.data;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Artifact {

    private final String name;
    private final String description;
    private final Material material;
    private final ChatColor color;
    private final double price;
    private final int chance;

    public Artifact(String name, String description, Material material, ChatColor color, double price, int chance) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.color = color;
        this.price = price;
        this.chance = chance;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    public ChatColor getColor() {
        return color;
    }

    public double getPrice() {
        return price;
    }

    public int getChance() {
        return chance;
    }
}

