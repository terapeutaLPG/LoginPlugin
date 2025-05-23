package me.twojplugin.commands;

import me.twojplugin.utils.AuthManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangePasswordCommand implements CommandExecutor {
    private final AuthManager auth;

    public ChangePasswordCommand(AuthManager auth) {
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player) || !sender.hasPermission("loginplugin.changepassword")) {
            sender.sendMessage("Brak uprawnień.");
            return true;
        }
        Player p = (Player) sender;
        if (args.length != 3) {
            p.sendMessage("Użycie: /zmienhaslo <stare> <nowe> <powtórz>");
            return true;
        }
        if (!auth.checkPassword(p.getName(), args[0])) {
            p.sendMessage("Błędne stare hasło.");
            return true;
        }
        if (!args[1].equals(args[2])) {
            p.sendMessage("Nowe hasła się różnią.");
            return true;
        }
        auth.changePassword(p.getName(), args[1]);
        p.sendMessage("Hasło zmienione pomyślnie.");
        return true;
    }
}
