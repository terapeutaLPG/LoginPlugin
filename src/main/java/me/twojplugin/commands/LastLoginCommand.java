package me.twojplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    public boolean onCommand(CommandSender snd, Command cmd, String lbl, String[] args) {
        String t = args.length == 1 ? args[0] : snd.getName();
        if (!auth.isRegistered(t)) {
            snd.sendMessage(ChatColor.RED + "Gracz " + t + " nie jest zarejestrowany.");
            return true;
        }
        boolean on = Bukkit.getPlayerExact(t) != null;
        snd.sendMessage(ChatColor.GOLD + "=== /lastlogin " + t + " ===");
        snd.sendMessage(ChatColor.YELLOW + "Zarejestrowany:     " + auth.getRegisteredDate(t));
        snd.sendMessage(ChatColor.YELLOW + "Ost. logowanie:     " + auth.getLastLoginDate(t));
        snd.sendMessage(ChatColor.YELLOW + "Ost. wejście:       " + auth.getLastJoinDate(t));
        snd.sendMessage(ChatColor.YELLOW + "Ost. wyjście:       " + auth.getLastQuitDate(t));
        snd.sendMessage(ChatColor.YELLOW + "Online: " + (on ? "TAK" : "NIE"));

        String loc = auth.getLastLogoutLocation(t);
        if (snd instanceof Player && !loc.equals("–")) {
            String[] p = loc.split(" ");
            if (p.length == 4) {
                String w = p[0], x = p[1], y = p[2], z = p[3];
                TextComponent tc = new TextComponent(
                        ChatColor.YELLOW + "Teleport do: " + ChatColor.AQUA + loc
                );
                tc.setClickEvent(new ClickEvent(
                        Action.RUN_COMMAND,
                        "/execute in " + w + " run tp @s " + x + " " + y + " " + z
                ));
                tc.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Kliknij by teleportować").create()
                ));
                ((Player) snd).spigot().sendMessage(tc);
                return true;
            }
        }
        snd.sendMessage(ChatColor.YELLOW + "Miejsce wylogowania: " + ChatColor.AQUA + loc);
        return true;
    }
}
