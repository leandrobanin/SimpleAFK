package com.java.simpleafk;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class AFKManager implements Listener {

    private final JavaPlugin plugin;
    private final HashMap<UUID, Long> lastActionTime = new HashMap<>();
    private final HashSet<UUID> afkPlayers = new HashSet<>();
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private long afkDelayMillis;
    private String inactivitySetAfkMsg, inactivityUnsetAfkMsg;
    private String manualSetAfkMsg, manualUnsetAfkMsg;

    public AFKManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        loadConfigValues();
        startAFKChecker();
    }

    public void loadConfigValues() {
        plugin.reloadConfig();
        afkDelayMillis = plugin.getConfig().getLong("afk-timer-seconds", 300) * 1000;
        inactivitySetAfkMsg = plugin.getConfig().getString("messages.private-afk-notification");
        inactivityUnsetAfkMsg = plugin.getConfig().getString("messages.private-no-longer-afk");
        manualSetAfkMsg = plugin.getConfig().getString("messages.manual-set-afk");
        manualUnsetAfkMsg = plugin.getConfig().getString("messages.manual-unset-afk");
    }

    public boolean isPlayerAFK(Player player) {
        return afkPlayers.contains(player.getUniqueId());
    }

    public void toggleAfkStatus(Player player) {
        if (isPlayerAFK(player)) {
            unsetAfk(player, true);
        } else {
            setAfk(player, true);
        }
    }

    private void setAfk(Player player, boolean isManual) {
        afkPlayers.add(player.getUniqueId());
        String privateMsgStr = isManual ? manualSetAfkMsg : inactivitySetAfkMsg;
        player.sendMessage(miniMessage.deserialize(privateMsgStr));
        // AVISO GLOBAL REMOVIDO
    }

    private void unsetAfk(Player player, boolean isManual) {
        afkPlayers.remove(player.getUniqueId());
        String privateMsgStr = isManual ? manualUnsetAfkMsg : inactivityUnsetAfkMsg;
        player.sendMessage(miniMessage.deserialize(privateMsgStr));
        // AVISO GLOBAL REMOVIDO
    }

    public void updatePlayerAction(Player player) {
        lastActionTime.put(player.getUniqueId(), System.currentTimeMillis());
        if (isPlayerAFK(player)) {
            unsetAfk(player, false);
        }
    }

    private void startAFKChecker() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            long now = System.currentTimeMillis();
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (isPlayerAFK(player)) continue;
                if (now - lastActionTime.getOrDefault(player.getUniqueId(), now) > afkDelayMillis) {
                    setAfk(player, false);
                }
            }
        }, 20L * 10, 20L * 10);
    }

    @EventHandler public void onPlayerJoin(PlayerJoinEvent event) { updatePlayerAction(event.getPlayer()); }
    @EventHandler public void onPlayerQuit(PlayerQuitEvent event) {
        lastActionTime.remove(event.getPlayer().getUniqueId());
        afkPlayers.remove(event.getPlayer().getUniqueId());
    }
    @EventHandler public void onPlayerMove(PlayerMoveEvent event) { if(event.hasChangedBlock()) updatePlayerAction(event.getPlayer()); }
    @EventHandler public void onPlayerChat(AsyncChatEvent event) { updatePlayerAction(event.getPlayer()); }
    @EventHandler public void onPlayerInteract(PlayerInteractEvent event) { updatePlayerAction(event.getPlayer()); }
    @EventHandler public void onPlayerCommand(PlayerCommandPreprocessEvent event) { updatePlayerAction(event.getPlayer()); }
}