package me.twojplugin.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.twojplugin.Main;
import me.twojplugin.utils.AuthManager;

public class PremiumCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final AuthManager auth;

    public PremiumCommand(JavaPlugin plugin, AuthManager auth) {
        this.plugin = plugin;
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("loginplugin.premium")) {
            sender.sendMessage("Brak uprawnień.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("Użycie: /premium <nick>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("Gracz nie jest online.");
            return true;
        }

        Main.premiumPlayers.add(target.getName());
        plugin.getConfig().set("premium", new ArrayList<>(Main.premiumPlayers));
        plugin.saveConfig();

        auth.logPremiumAction(target.getName(), "GRANT");
        sender.sendMessage("Dodano " + target.getName() + " do premium.");
        return true;
    }
}
