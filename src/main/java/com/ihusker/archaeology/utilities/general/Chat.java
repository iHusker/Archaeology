package com.ihusker.archaeology.utilities.general;

import org.bukkit.ChatColor;

public class Chat {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String prefix(String string) {
        return color("&e&lArchaeology: &7" + string);
    }
}