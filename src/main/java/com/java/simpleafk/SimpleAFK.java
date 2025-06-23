package com.java.simpleafk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class SimpleAFK extends JavaPlugin {

    private AFKManager afkManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().severe("PlaceholderAPI não encontrado! Este plugin necessita dele para funcionar.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.afkManager = new AFKManager(this);
        new AFKPlaceholder(this, afkManager).register();

        getLogger().info("SimpleAFK habilitado com sucesso!");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("afk")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Este comando só pode ser usado por jogadores.");
                return true;
            }
            afkManager.toggleAfkStatus(player);
            return true;
        }

        if (command.getName().equalsIgnoreCase("simpleafk")) {
            if (args.length == 0 || args[0].equalsIgnoreCase("info")) {
                if (!sender.hasPermission("simpleafk.info")) {
                    sender.sendMessage(miniMessage.deserialize("<red>Você não tem permissão para usar este comando.</red>"));
                    return true;
                }
                sendPluginInfo(sender);
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("simpleafk.reload")) {
                    sender.sendMessage(miniMessage.deserialize("<red>Você não tem permissão para usar este comando.</red>"));
                    return true;
                }
                afkManager.loadConfigValues();
                sender.sendMessage(miniMessage.deserialize("<green>Configuração do SimpleAFK recarregada com sucesso!</green>"));
                return true;
            }

            sender.sendMessage(miniMessage.deserialize("<gold>Uso correto: /" + label + " <info|reload></gold>"));
            return true;
        }
        return false;
    }

    private void sendPluginInfo(CommandSender sender) {
        Component info = miniMessage.deserialize(
                """
                <st><dark_gray>                                                                                </st>
                <white>Informações do <gradient:#F9A825:#FFD700>SimpleAFK</gradient>
                <gray>Versão:</gray> <yellow>%version%</yellow>
                <gray>Autor:</gray> <yellow>%author%</yellow>
                <dark_gray>Um plugin simples para gerenciar status AFK.</dark_gray>
                <st><dark_gray>                                                                                </st>
                """
                        .replace("%version%", getPluginMeta().getVersion())
                        .replace("%author%", getPluginMeta().getAuthors().toString())
        );
        sender.sendMessage(info);
    }

    @Override
    public void onDisable() {
        getLogger().info("SimpleAFK desabilitado.");
    }
}