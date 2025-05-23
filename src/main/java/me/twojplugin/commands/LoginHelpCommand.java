package me.twojplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LoginHelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("loginplugin.loginhelp")) {
            sender.sendMessage("Brak uprawnień.");
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "--- LoginPlugin Help ---");
        sender.sendMessage(ChatColor.YELLOW + "/register <hasło> <powtórz>" + ChatColor.WHITE + " - Zarejestruj nowe konto");
        sender.sendMessage(ChatColor.YELLOW + "/login <hasło>" + ChatColor.WHITE + " - Zaloguj się");
        sender.sendMessage(ChatColor.YELLOW + "/zmienhaslo <stare> <nowe> <powtórz>" + ChatColor.WHITE + " - Zmień własne hasło");
        sender.sendMessage(ChatColor.YELLOW + "/lastlogin [nick]" + ChatColor.WHITE + " - Pokaż daty rejestracji i logowania");
        sender.sendMessage(ChatColor.YELLOW + "/loginhelp" + ChatColor.WHITE + " - Pokaż tę pomoc");
        return true;
    }
}
