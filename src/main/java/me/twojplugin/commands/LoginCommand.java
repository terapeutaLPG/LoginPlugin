package me.twojplugin.commands;

import me.twojplugin.utils.AuthManager;
import me.twojplugin.utils.PlayerDataManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class LoginCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final AuthManager auth;

    public LoginCommand(JavaPlugin plugin, AuthManager auth) {
        this.plugin = plugin;
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tylko gracze mogą użyć tej komendy.");
            return true;
        }
        Player p = (Player) sender;
        if (auth.isLoggedIn(p.getName())) {
            p.sendMessage(ChatColor.GRAY + "Jesteś już zalogowany.");
            return true;
        }
        if (args.length != 1) {
            p.sendMessage("Użycie: /login <hasło>");
            return true;
        }
        if (!auth.isRegistered(p.getName())) {
            p.sendMessage("Nie jesteś zarejestrowany.");
            return true;
        }
        if (auth.login(p.getName(), args[0])) {
            // Pomyślne logowanie
            p.sendMessage(ChatColor.BLUE + "Zalogowano pomyślnie!");

            // Przywróć stan gracza
            new PlayerDataManager(plugin).loadPlayer(p);

            // Usuń efekt krótkowzroczności i odblokuj wszystko
            p.removePotionEffect(PotionEffectType.BLINDNESS);
            p.setGameMode(GameMode.SURVIVAL);
        } else {
            p.sendMessage(ChatColor.RED + "Błędne hasło.");
        }
        return true;
    }
}
