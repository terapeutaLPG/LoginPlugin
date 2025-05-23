package me.twojplugin.commands;

import me.twojplugin.utils.AuthManager;
import me.twojplugin.utils.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RegisterCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final AuthManager auth;

    public RegisterCommand(JavaPlugin plugin, AuthManager auth) {
        this.plugin = plugin;
        this.auth   = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Tylko gracze mogą użyć tej komendy.");
            return true;
        }
        Player p = (Player) sender;
        if (args.length != 2) {
            p.sendMessage("Użycie: /register <hasło> <powtórz>");
            return true;
        }
        if (!args[0].equals(args[1])) {
            p.sendMessage("Hasła się różnią.");
            return true;
        }
        if (auth.isRegistered(p.getName())) {
            p.sendMessage("Już jesteś zarejestrowany.");
            return true;
        }
        String ip = p.getAddress().getAddress().getHostAddress();
        auth.register(p.getName(), args[0], ip);
        new PlayerDataManager(plugin).savePlayer(p);
        p.sendMessage("Zarejestrowano! Data rejestracji: " + auth.getRegisteredDate(p.getName()));
        return true;
    }
}
