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

        // 1) jeśli był w locie przy wylogu → zabij go natychmiast
        if (QuitListener.fallKill.remove(p.getName())) {
            p.setHealth(0);
            return;
        }

        // 2) premium autologin + restore
        if (Main.premiumPlayers.contains(p.getName())) {
            auth.setLoggedIn(p.getName());
            p.sendMessage(ChatColor.BLUE + "Automatyczne logowanie (premium).");
            pdm.loadPlayer(p);
            p.removePotionEffect(PotionEffectType.BLINDNESS);
            p.setGameMode(GameMode.SURVIVAL);
            return;
        }

        // 3) normalne wejście: reset stanu + efekt ślepoty
        auth.logout(p.getName());
        Location spawn = p.getWorld().getSpawnLocation();
        p.teleport(spawn);
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(20);
        p.setSaturation(20f);
        p.getInventory().clear();
        p.setLevel(0);
        p.setTotalExperience(0);
        p.setGameMode(GameMode.ADVENTURE);
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1, false, false));

        p.sendMessage(ChatColor.YELLOW + "Witaj! Zaloguj się (/login) lub zarejestruj (/register).");
    }
}
