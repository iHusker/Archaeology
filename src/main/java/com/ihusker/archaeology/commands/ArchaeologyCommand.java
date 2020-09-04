package com.ihusker.archaeology.commands;

import com.ihusker.archaeology.Archaeology;
import com.ihusker.archaeology.data.Artifact;
import com.ihusker.archaeology.managers.ArtifactManager;
import com.ihusker.archaeology.utilities.command.AbstractCommand;
import com.ihusker.archaeology.utilities.storage.data.Config;
import com.ihusker.archaeology.utilities.storage.data.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ArchaeologyCommand extends AbstractCommand implements TabCompleter {

    private final Archaeology archaeology;
    private final ArtifactManager artifactManager;

    public ArchaeologyCommand(Archaeology archaeology) {
        super("archaeology");
        this.archaeology = archaeology;
        this.artifactManager = archaeology.getArtifactManager();
    }

    @Override
    public void execute(Player player, String[] args) {
        if(args.length < 1) {
            PluginDescriptionFile descriptionFile = archaeology.getDescription();
            player.sendMessage(Message.PREFIX.toString());
            player.sendMessage("Author(s): " + descriptionFile.getAuthors());
            player.sendMessage("Version: " + descriptionFile.getVersion());
            return;
        }

        if(args[0].equalsIgnoreCase("version")) {
            PluginDescriptionFile descriptionFile = archaeology.getDescription();
            player.sendMessage(Message.PREFIX.toString());
            player.sendMessage("Author(s): " + descriptionFile.getAuthors());
            player.sendMessage("Version: " + descriptionFile.getVersion());
            return;
        }

        if(args[0].equalsIgnoreCase("reload")) {
            artifactManager.deserialize();
            archaeology.reloadConfig();
            archaeology.getDataManager().deserialize(archaeology);
            player.sendMessage(Message.PREFIX.toString() + Message.RELOAD.toString());
            return;
        }

        if(args[0].equalsIgnoreCase("give") && args.length == 3) {
            Player target = archaeology.getServer().getPlayer(args[1]);

            if(target == null) {
                player.sendMessage(Message.PREFIX.toString() + Message.OFFLINE.toString().replace("{player}", args[2]));
                return;
            }

            Artifact artifact = artifactManager.getArtifact(args[2]);
            if (artifact == null) {
                player.sendMessage(Message.PREFIX.toString() + Message.NON_EXISTENT.toString().replace("{name}", args[1]));
                return;
            }

            target.getInventory().addItem(artifactManager.artifactItem(artifact));
            target.sendMessage(Message.PREFIX.toString() + Message.RECEIVED.toString().replace("{name}", artifact.toString()));
            player.sendMessage(Message.PREFIX.toString() + Message.SENT.toString()
                    .replace("{name}", artifact.toString())
                    .replace("{player}", target.getName())
            );
        }

        if(args[0].equalsIgnoreCase("redeem")) {
            if (artifactManager.redeem(player)) {
                String[] strings = (String[]) Config.MESSAGES;
                if(strings.length <= 0) return;
                player.sendMessage(Message.NPC_NAME.toString() + strings[new Random().nextInt(strings.length)]);
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 1) return Arrays.asList("reload", "redeem", "version", "give");
        if(strings.length == 3) return artifactManager.getArtifacts().stream().map(Artifact::getName).collect(Collectors.toList());
        return null;
    }
}
