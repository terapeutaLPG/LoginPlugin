package me.twojplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.twojplugin.Main;
import me.twojplugin.utils.AuthManager;
import me.twojplugin.utils.PlayerDataManager;

public class JoinListener implements Listener {

    private final AuthManager auth;
    private final PlayerDataManager pdm;

    public JoinListener(AuthManager auth, PlayerDataManager pdm) {
        this.auth = auth;
        this.pdm = pdm;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        String name = p.getName();

        // logowanie wejścia
        if (auth.isRegistered(name) || Main.premiumPlayers.contains(name)) {
            auth.logJoin(name);
        }

        // premium autologin
        if (Main.premiumPlayers.contains(name)) {
            auth.setLoggedIn(name);
            p.sendMessage(ChatColor.BLUE + "Automatyczne logowanie (premium).");
            pdm.loadPlayer(p);
            p.removePotionEffect(PotionEffectType.BLINDNESS);
            p.setGameMode(GameMode.SURVIVAL);
            return;
        }

        // zablokowane dopóki nie zaloguje się normalnie
        auth.logout(name);
        Location spawn = p.getWorld().getSpawnLocation();
        p.teleport(spawn);
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(20);
        p.setSaturation(20f);
        p.getInventory().clear();
        p.setLevel(0);
        p.setTotalExperience(0);
        p.setGameMode(GameMode.ADVENTURE);
        // poprawne wywołanie
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false));
        p.sendMessage(ChatColor.YELLOW + "Witaj! Zaloguj się (/login) lub zarejestruj (/register).");
    }
}
