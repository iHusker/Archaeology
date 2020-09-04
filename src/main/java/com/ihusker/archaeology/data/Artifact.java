package com.ihusker.archaeology.data;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Artifact {

    private final String name, type, description;
    private final Material material;
    private List<String> commands;
    private final ChatColor color;
    private int depth;
    private final double price, chance;

    public Artifact(String name, String type, String description, Material material, ChatColor color, int depth, double price, double chance) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.material = material;
        this.color = color;
        this.depth = depth;
        this.price = price;
        this.chance = chance;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Material getMaterial() {
        return material;
    }

    public List<String> getCommands() {
        if(commands == null) commands = new ArrayList<>();
        return commands;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getDepth() {
        if(depth == 0) depth = 64;
        return depth;
    }

    public double getPrice() {
        return price;
    }

    public double getChance() {
        return chance;
    }


    @Override
    public String toString() {
        return WordUtils.capitalize(name.toLowerCase().replace("_", " "));
    }
}

