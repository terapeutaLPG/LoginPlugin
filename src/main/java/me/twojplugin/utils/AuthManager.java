package me.twojplugin.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class AuthManager {

    private final JavaPlugin plugin;
    private final File file;
    private final FileConfiguration cfg;
    private final Set<String> loggedIn = new HashSet<>();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AuthManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getDataFolder().mkdirs();
        file = new File(plugin.getDataFolder(), "users.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                YamlConfiguration def = new YamlConfiguration();
                def.set("users", new HashSet<>());
                def.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public boolean isRegistered(String name) {
        return cfg.contains("users." + name + ".pass");
    }

    public void register(String name, String pass, String ip) {
        String path = "users." + name;
        String now = LocalDateTime.now().format(fmt);
        cfg.set(path + ".pass", pass);
        cfg.set(path + ".ip", ip);
        cfg.set(path + ".registered", now);
        cfg.set(path + ".lastLogin", now);
        cfg.set(path + ".history.premium", new ArrayList<>());
        cfg.set(path + ".history.joins", new ArrayList<>());
        cfg.set(path + ".history.quits", new ArrayList<>());
        save();
    }

    public void unregister(String name) {
        cfg.set("users." + name, null);
        save();
    }

    public boolean checkPassword(String name, String pass) {
        String real = cfg.getString("users." + name + ".pass");
        return real != null && real.equals(pass);
    }

    public boolean changePassword(String name, String newPass) {
        if (!isRegistered(name)) {
            return false;
        }
        cfg.set("users." + name + ".pass", newPass);
        save();
        return true;
    }

    public boolean login(String name, String pass) {
        if (checkPassword(name, pass)) {
            loggedIn.add(name);
            String now = LocalDateTime.now().format(fmt);
            cfg.set("users." + name + ".lastLogin", now);
            save();
            return true;
        }
        return false;
    }

    public void setLoggedIn(String name) {
        loggedIn.add(name);
    }

    public void logout(String name) {
        loggedIn.remove(name);
    }

    public boolean isLoggedIn(String name) {
        return loggedIn.contains(name);
    }

    public void logPremiumAction(String name, String action) {
        String path = "users." + name + ".history.premium";
        List<String> list = cfg.getStringList(path);
        list.add(LocalDateTime.now().format(fmt) + " " + action);
        cfg.set(path, list);
        save();
    }

    public void logJoin(String name) {
        String path = "users." + name + ".history.joins";
        List<String> list = cfg.getStringList(path);
        list.add(LocalDateTime.now().format(fmt));
        cfg.set(path, list);
        save();
    }

    public void logQuit(String name) {
        String path = "users." + name + ".history.quits";
        List<String> list = cfg.getStringList(path);
        list.add(LocalDateTime.now().format(fmt));
        cfg.set(path, list);
        save();
    }

    public String getIP(String name) {
        return cfg.getString("users." + name + ".ip", null);
    }

    public String getRegisteredDate(String name) {
        return cfg.getString("users." + name + ".registered", "—");
    }

    public String getLastLoginDate(String name) {
        return cfg.getString("users." + name + ".lastLogin", "—");
    }

    public List<String> getPremiumHistory(String name) {
        return cfg.getStringList("users." + name + ".history.premium");
    }

    public List<String> getJoinHistory(String name) {
        return cfg.getStringList("users." + name + ".history.joins");
    }

    public List<String> getQuitHistory(String name) {
        return cfg.getStringList("users." + name + ".history.quits");
    }

    private void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
