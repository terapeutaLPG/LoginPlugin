package me.twojplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.twojplugin.Main;
import me.twojplugin.utils.AuthManager;
import me.twojplugin.utils.PlayerDataManager;

public class QuitListener implements Listener {

    private final JavaPlugin plugin;
    private final AuthManager auth;

    public QuitListener(JavaPlugin plugin, AuthManager auth) {
        this.plugin = plugin;
        this.auth = auth;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();

        // zapisujemy stan jeśli zalogowany
        if (auth.isLoggedIn(name)) {
            new PlayerDataManager(plugin).savePlayer(p);
            auth.logout(name);
        }

        // logujemy wyjście + miejsce
        if (auth.isRegistered(name) || Main.premiumPlayers.contains(name)) {
            auth.logQuit(
                    name,
                    p.getWorld().getName(),
                    p.getLocation().getX(),
                    p.getLocation().getY(),
                    p.getLocation().getZ()
            );
        }
    }
}
