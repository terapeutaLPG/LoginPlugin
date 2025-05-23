package me.twojplugin.listeners;

import me.twojplugin.Main;
import me.twojplugin.utils.AuthManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class RestrictedListener implements Listener {
    private final AuthManager auth;

    public RestrictedListener(AuthManager auth) {
        this.auth = auth;
    }

    private boolean allowed(Player p) {
        return auth.isLoggedIn(p.getName()) || Main.premiumPlayers.contains(p.getName());
    }

    private void prompt(Player p) {
        if (auth.isRegistered(p.getName())) {
            p.sendMessage(ChatColor.GREEN + "Użyj: /login <hasło>");
        } else {
            p.sendMessage(ChatColor.YELLOW + "Użyj: /register <hasło> <powtórz>");
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (!allowed(p) && e.getFrom().distanceSquared(e.getTo()) > 0) {
            e.setTo(e.getFrom());
            prompt(p);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (!allowed(p)) {
            e.setCancelled(true);
            prompt(p);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!allowed(p)) {
            e.setCancelled(true);
            prompt(p);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (!allowed(p)) {
            e.setCancelled(true);
            prompt(p);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (!allowed(p)) {
            e.setCancelled(true);
            prompt(p);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player p && !allowed(p)) {
            e.setCancelled(true);
            prompt(p);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (!allowed(p)) {
            e.setCancelled(true);
            prompt(p);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player p && !allowed(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (!allowed(p)) {
            String msg = e.getMessage().toLowerCase();
            if (msg.startsWith("/login") || msg.startsWith("/register")) return;
            e.setCancelled(true);
            prompt(p);
        }
    }
}
