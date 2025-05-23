package me.twojplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.twojplugin.utils.AuthManager;

public class ChangePasswordCommand implements CommandExecutor {

    private final AuthManager auth;

    public ChangePasswordCommand(AuthManager auth) {
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Tylko gracze mogą zmienić hasło.");
            return true;
        }
        if (args.length != 3) {
            p.sendMessage(ChatColor.RED + "Użycie: /zmienhaslo <stare> <nowe> <powtórz>");
            return true;
        }
        String old = args[0], nw = args[1], rep = args[2];
        if (!nw.equals(rep)) {
            p.sendMessage(ChatColor.RED + "Nowe hasła nie są identyczne.");
            return true;
        }
        if (!auth.checkPassword(p.getName(), old)) {
            p.sendMessage(ChatColor.RED + "Stare hasło jest niepoprawne.");
            return true;
        }
        auth.changePassword(p.getName(), nw);
        p.sendMessage(ChatColor.GREEN + "Hasło zostało zmienione pomyślnie.");
        return true;
    }
}
