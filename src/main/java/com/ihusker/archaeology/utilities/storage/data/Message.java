package com.ihusker.archaeology.utilities.storage.data;

public class Message {

    public static Object PERMISSIONS = "&7You do not seem to have permissions for that command.";
    public static Object PREFIX = "&e&lArchaeology: &7";
    public static Object NPC_NAME = "&e&lArchaeologist: &7";
    public static Object NON_EXISTENT = "The &f{name} &7artifact does not seem to exist.";
    public static Object FOUND = "You have found a &f{name} &7artifact.";
    public static Object RECEIVED = "You have received a &f{name} &7artifact.";
    public static Object OFFLINE = "The player &f{player} &7does not seem to be online.";
    public static Object RELOAD = "You have reloaded the configuration files.";
    public static Object SENT = "You have sent a &f{name} &7artifact to &f{player}&7.";
    public static Object REDEEM = "You have received &a${amount} &7from the artifact.";
    public static Object ARTIFACT_NAME = "{color}&l{name} &7[Artifact]";
    public static Object ARTIFACT_LORE = new String[] {
            "&7{description}",
            "&7Chance: &a{chance}%",
            "",
            "&f&oRedeem at the Archaeologist."
    };
}
