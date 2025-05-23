package me.twojplugin.commands;

import me.twojplugin.utils.AuthManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UnregisterCommand implements CommandExecutor {
    private final AuthManager auth;

    public UnregisterCommand(AuthManager auth) {
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("loginplugin.unregister")) {
            sender.sendMessage("Brak uprawnień.");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("Użycie: /unregister <nick>");
            return true;
        }
        String target = args[0];
        if (!auth.isRegistered(target)) {
            sender.sendMessage("Gracz " + target + " nie jest zarejestrowany.");
            return true;
        }
        auth.unregister(target);
        sender.sendMessage("Usunięto konto gracza " + target + ".");
        return true;
    }
}
