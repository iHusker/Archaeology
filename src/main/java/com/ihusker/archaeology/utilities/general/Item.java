package com.ihusker.archaeology.utilities.general;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.stream.Collectors;

public class Item {

    private String name;
    private List<String> lore;
    private Material material;
    private int amount = 1;

    public Item name(String name) {
        this.name = name;
        return this;
    }

    public Item lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public Item material(Material material) {
        this.material = material;
        return this;
    }

    public Item amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return itemStack;
        itemMeta.setDisplayName(Chat.color(name));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.setLore(lore.stream().map(Chat::color).collect(Collectors.toList()));
        itemStack.setAmount(amount);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
