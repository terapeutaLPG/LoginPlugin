package me.twojplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.twojplugin.utils.AuthManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
        sender.sendMessage(ChatColor.YELLOW + "Zarejestrowany:     " + auth.getRegisteredDate(target));
        sender.sendMessage(ChatColor.BLUE + "Ostatnie logowanie: " + auth.getLastLoginDate(target));
        sender.sendMessage(ChatColor.YELLOW + "Ostatnie wejście:   " + auth.getLastJoinDate(target));
        sender.sendMessage(ChatColor.RED + "Ostatnie wyjście:   " + auth.getLastQuitDate(target));
        sender.sendMessage(ChatColor.GREEN + "Online: " + (online ? "TAK" : "NIE"));

        String loc = auth.getLastLogoutLocation(target);
        if ("–".equals(loc)) {
            sender.sendMessage(ChatColor.YELLOW + "Miejsce wylogowania: " + ChatColor.AQUA + loc);
            return true;
        }

        String[] parts = loc.split(" ");
        if (parts.length != 4) {
            sender.sendMessage(ChatColor.RED + "Miejsce wylogowania: " + ChatColor.AQUA + loc);
            return true;
        }

        String worldName = parts[0];
        String x = parts[1], y = parts[2], z = parts[3];

        // mapowanie środowisk na dimension IDs
        String dimID;
        World wObj = Bukkit.getWorld(worldName);
        if (wObj != null) {
            switch (wObj.getEnvironment()) {
                case NORMAL ->
                    dimID = "minecraft:overworld";
                case NETHER ->
                    dimID = "minecraft:the_nether";
                case THE_END ->
                    dimID = "minecraft:the_end";
                default ->
                    dimID = worldName;
            }
        } else {
            dimID = worldName;
        }

        if (sender instanceof Player p) {
            // zamiast @s – jawnie nick, żeby Essentials go poprawnie parsowało
            String cmdTp = "/execute in " + dimID
                    + " run tp " + p.getName()
                    + " " + x + " " + y + " " + z;

            TextComponent tc = new TextComponent(
                    ChatColor.YELLOW + "Teleport do: "
                    + ChatColor.AQUA + x + " " + y + " " + z
                    + ChatColor.GRAY + " [" + worldName + "]"
            );
            tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, cmdTp));
            tc.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("Kliknij, aby teleportować do ostatniego miejsca wylogowania").create()
            ));
            p.spigot().sendMessage(tc);
        } else {
            sender.sendMessage(ChatColor.YELLOW + "Miejsce wylogowania: "
                    + ChatColor.AQUA + x + " " + y + " " + z
                    + ChatColor.GRAY + " [" + worldName + "]");
        }

        return true;
    }
}
