package me.twojplugin.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.twojplugin.utils.AuthManager;

public class QuitListener implements Listener {

    private final JavaPlugin plugin;
    private final AuthManager auth;

    // gracze, którzy wylogowali się w locie
    public static Set<String> fallKill = new HashSet<>();

    public QuitListener(JavaPlugin plugin, AuthManager auth) {
        this.plugin = plugin;
        this.auth = auth;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        // jeśli zalogowany (po rejestracji/loginie) → autosave
        if (auth.isLoggedIn(p.getName())) {
            new me.twojplugin.utils.PlayerDataManager(plugin).savePlayer(p);
            auth.logout(p.getName());
        }
        // jeśli nie stoi na ziemi i spada dalej niż 3 bloki → zaznacz do zabicia
        if (p.getFallDistance() > 3 && !p.isOnGround()) {
            fallKill.add(p.getName());
        }
    }
}
