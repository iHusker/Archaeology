package com.ihusker.archaeology.data;

import org.bukkit.Material;

public class Artifact {

    private final String name, description;
    private final Rarity rarity;
    private final Material material;
    private final double price;

    public Artifact(String name, String description, Rarity rarity, Material material, int price) {
        this.name = name;
        this.description = description;
        this.rarity = rarity;
        this.material = material;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public Rarity getRarity() {
        return rarity;
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rarity=" + rarity +
                ", material=" + material +
                ", price=" + price +
                '}';
    }
}

