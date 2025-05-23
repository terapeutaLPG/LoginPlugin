package me.twojplugin.commands;

import me.twojplugin.utils.AuthManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AdminChangePasswordCommand implements CommandExecutor {
    private final AuthManager auth;

    public AdminChangePasswordCommand(AuthManager auth) {
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("loginplugin.admin.changepassword")) {
            sender.sendMessage("Brak uprawnień.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage("Użycie: /jzmienhaslo <nick> <nowe_hasło>");
            return true;
        }
        String target = args[0];
        if (!auth.isRegistered(target)) {
            sender.sendMessage("Gracz " + target + " nie jest zarejestrowany.");
            return true;
        }
        auth.changePassword(target, args[1]);
        sender.sendMessage("Hasło gracza " + target + " zostało zmienione.");
        return true;
    }
}
