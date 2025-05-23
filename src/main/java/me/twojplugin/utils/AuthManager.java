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
                def.set("users", null);
                def.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cfg = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Czy gracz jest zarejestrowany?
     */
    public boolean isRegistered(String name) {
        return cfg.contains("users." + name + ".pass");
    }

    /**
     * Sprawdzenie hasła (do /zmienhaslo)
     */
    public boolean checkPassword(String name, String pass) {
        String real = cfg.getString("users." + name + ".pass");
        return real != null && real.equals(pass);
    }

    /**
     * Rejestracja nowego konta
     */
    public void register(String name, String pass, String ip) {
        String p = "users." + name;
        String now = LocalDateTime.now().format(fmt);

        cfg.set(p + ".pass", pass);
        cfg.set(p + ".ip", ip);
        cfg.set(p + ".registered", now);
        cfg.set(p + ".lastLogin", now);
        cfg.set(p + ".lastJoin", now);
        cfg.set(p + ".lastQuit", "–");
        cfg.set(p + ".lastLogoutLocation", "–");
        cfg.set(p + ".history.premium", new ArrayList<>());

        save();
    }

    /**
     * Logowanie (komenda /login)
     */
    public boolean login(String name, String pass) {
        if (!checkPassword(name, pass)) {
            return false;
        }
        loggedIn.add(name);
        cfg.set("users." + name + ".lastLogin", LocalDateTime.now().format(fmt));
        save();
        return true;
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

    /**
     * Zmiana hasła przez gracza
     */
    public boolean changePassword(String name, String newPass) {
        if (!isRegistered(name)) {
            return false;
        }
        cfg.set("users." + name + ".pass", newPass);
        save();
        return true;
    }

    /**
     * Usunięcie konta
     */
    public void unregister(String name) {
        cfg.set("users." + name, null);
        save();
    }

    /**
     * Premium: zapisujemy akcję (GRANT/REVOKE)
     */
    public void logPremiumAction(String name, String action) {
        String path = "users." + name + ".history.premium";
        List<String> hist = cfg.getStringList(path);
        hist.add(LocalDateTime.now().format(fmt) + " " + action);
        cfg.set(path, hist);
        save();
    }

    /**
     * Nadpisuje tylko ostatnie wejście
     */
    public void logJoin(String name) {
        cfg.set("users." + name + ".lastJoin", LocalDateTime.now().format(fmt));
        save();
    }

    /**
     * Nadpisuje ostatnie wyjście i zapisuje lokację
     */
    public void logQuit(String name, String world, double x, double y, double z) {
        String now = LocalDateTime.now().format(fmt);
        cfg.set("users." + name + ".lastQuit", now);

        String loc = world + " "
                + String.format("%.1f", x) + " "
                + String.format("%.1f", y) + " "
                + String.format("%.1f", z);
        cfg.set("users." + name + ".lastLogoutLocation", loc);

        save();
    }

    // ——— GETTERY ———
    public String getIP(String name) {
        return cfg.getString("users." + name + ".ip", "–");
    }

    public String getRegisteredDate(String name) {
        return cfg.getString("users." + name + ".registered", "–");
    }

    public String getLastLoginDate(String name) {
        return cfg.getString("users." + name + ".lastLogin", "–");
    }

    public String getLastJoinDate(String name) {
        return cfg.getString("users." + name + ".lastJoin", "–");
    }

    public String getLastQuitDate(String name) {
        return cfg.getString("users." + name + ".lastQuit", "–");
    }

    public String getLastLogoutLocation(String name) {
        return cfg.getString("users." + name + ".lastLogoutLocation", "–");
    }

    public List<String> getPremiumHistory(String name) {
        return cfg.getStringList("users." + name + ".history.premium");
    }

    /**
     * Zapis zmian do pliku
     */
    private void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
