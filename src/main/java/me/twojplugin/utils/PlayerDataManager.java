package me.twojplugin.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDataManager {

    private final JavaPlugin plugin;
    private final FileConfiguration cfg;
    private final File file;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getDataFolder().mkdirs();
        file = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void savePlayer(Player p) {
        String path = "players." + p.getName();
        cfg.set(path + ".world", p.getWorld().getName());
        cfg.set(path + ".x", p.getLocation().getX());
        cfg.set(path + ".y", p.getLocation().getY());
        cfg.set(path + ".z", p.getLocation().getZ());
        cfg.set(path + ".health", p.getHealth());
        cfg.set(path + ".food", p.getFoodLevel());
        cfg.set(path + ".exp", p.getExp());
        cfg.set(path + ".inv", p.getInventory().getContents());
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void loadPlayer(Player p) {
        String path = "players." + p.getName();
        if (!cfg.contains(path)) {
            return;
        }
        p.teleport(new org.bukkit.Location(
                p.getServer().getWorld(cfg.getString(path + ".world")),
                cfg.getDouble(path + ".x"),
                cfg.getDouble(path + ".y"),
                cfg.getDouble(path + ".z")
        ));
        p.setHealth(cfg.getDouble(path + ".health"));
        p.setFoodLevel(cfg.getInt(path + ".food"));
        p.setExp((float) cfg.getDouble(path + ".exp"));
        p.getInventory().setContents(
                ((java.util.List<org.bukkit.inventory.ItemStack>) cfg.get(path + ".inv")).toArray(new org.bukkit.inventory.ItemStack[0])
        );
    }
}
