package com.ihusker.archaeology.data;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Artifact {

    private final String name, description;
    private final Material material;
    private List<String> commands;
    private final ChatColor color;
    private final double price, chance;

    public Artifact(String name, String description, Material material, List<String> commands, ChatColor color, double price, double chance) {
        this.name = name;
        this.description = description;
        this.material = material;
        this.commands = commands;
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

    public List<String> getCommands() {
        if(commands == null) commands = new ArrayList<>();
        return commands;
    }

    public ChatColor getColor() {
        return color;
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

