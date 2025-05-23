package me.twojplugin;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import me.twojplugin.commands.AdminChangePasswordCommand;
import me.twojplugin.commands.ChangePasswordCommand;
import me.twojplugin.commands.LastLoginCommand;
import me.twojplugin.commands.LoginCommand;
import me.twojplugin.commands.LoginHelpCommand;
import me.twojplugin.commands.PremiumCommand;
import me.twojplugin.commands.RegisterCommand;
import me.twojplugin.commands.UnpremiumCommand;
import me.twojplugin.commands.UnregisterCommand;
import me.twojplugin.listeners.JoinListener;
import me.twojplugin.listeners.QuitListener;
import me.twojplugin.listeners.RestrictedListener;
import me.twojplugin.utils.AuthManager;
import me.twojplugin.utils.PlayerDataManager;

public class Main extends JavaPlugin {

    public static Set<String> premiumPlayers = new HashSet<>();
    private AuthManager auth;
    private PlayerDataManager pdm;

    @Override
    public void onEnable() {
        // zasoby
        saveResource("users.yml", false);
        saveResource("playerdata.yml", false);
        saveDefaultConfig();
        getConfig().getStringList("premium").forEach(premiumPlayers::add);

        // init
        auth = new AuthManager(this);
        pdm = new PlayerDataManager(this);

        // rejestracja komend
        getCommand("register").setExecutor(new RegisterCommand(this, auth));
        getCommand("login").setExecutor(new LoginCommand(this, auth));
        getCommand("zmienhaslo").setExecutor(new ChangePasswordCommand(auth));
        getCommand("jzmienhaslo").setExecutor(new AdminChangePasswordCommand(auth));
        getCommand("unregister").setExecutor(new UnregisterCommand(auth));
        getCommand("premium").setExecutor(new PremiumCommand(this, auth));
        getCommand("unpremium").setExecutor(new UnpremiumCommand(this, auth));
        getCommand("lastlogin").setExecutor(new LastLoginCommand(auth));
        getCommand("loginhelp").setExecutor(new LoginHelpCommand());

        // listenery
        getServer().getPluginManager().registerEvents(new QuitListener(this, auth), this);
        getServer().getPluginManager().registerEvents(new JoinListener(auth, pdm), this);
        getServer().getPluginManager().registerEvents(new RestrictedListener(auth), this);
    }
}
