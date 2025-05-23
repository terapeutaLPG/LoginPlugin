package me.twojplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.twojplugin.utils.AuthManager;

public class AdminChangePasswordCommand implements CommandExecutor {

    private final AuthManager auth;

    public AdminChangePasswordCommand(AuthManager auth) {
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("loginplugin.admin")) {
            sender.sendMessage(ChatColor.RED + "Brak uprawnień.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Użycie: /jzmienhaslo <nick> <nowe_hasło>");
            return true;
        }
        String target = args[0], nw = args[1];
        if (!auth.isRegistered(target)) {
            sender.sendMessage(ChatColor.RED + "Gracz " + target + " nie jest zarejestrowany.");
            return true;
        }
        auth.changePassword(target, nw);
        sender.sendMessage(ChatColor.GREEN + "Hasło gracza " + target + " zmienione na: " + nw);
        return true;
    }
}
