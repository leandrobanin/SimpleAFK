package com.java.simpleafk;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AFKPlaceholder extends PlaceholderExpansion {

    private final JavaPlugin plugin;
    private final AFKManager afkManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private final String afkFormat = "[AFK]";

    public AFKPlaceholder(JavaPlugin plugin, AFKManager afkManager) {
        this.plugin = plugin;
        this.afkManager = afkManager;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "simpleafk";
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getPluginMeta().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "";
        }

        if (params.equalsIgnoreCase("status")) {
            if (!afkManager.isPlayerAFK(player)) {
                return "";
            }

            Component parsedComponent = miniMessage.deserialize(this.afkFormat);
            return LegacyComponentSerializer.legacySection().serialize(parsedComponent);
        }

        return null;
    }
}