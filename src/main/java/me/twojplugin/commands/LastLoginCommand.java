package me.twojplugin.commands;

import me.twojplugin.utils.AuthManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LastLoginCommand implements CommandExecutor {
    private final AuthManager auth;

    public LastLoginCommand(AuthManager auth) {
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String target = args.length == 1 ? args[0] : sender.getName();
        if (!auth.isRegistered(target)) {
            sender.sendMessage(ChatColor.RED + "Gracz " + target + " nie jest zarejestrowany.");
            return true;
        }
        boolean online = Bukkit.getPlayerExact(target) != null;
        sender.sendMessage(ChatColor.GOLD + "=== /lastlogin " + target + " ===");
        sender.sendMessage(ChatColor.YELLOW + "Zarejestrowany: " + auth.getRegisteredDate(target));
        sender.sendMessage(ChatColor.YELLOW + "Ostatnie logowanie: " + auth.getLastLoginDate(target));
        sender.sendMessage(ChatColor.YELLOW + "Online: " + (online ? "TAK" : "NIE"));
        return true;
    }
}
