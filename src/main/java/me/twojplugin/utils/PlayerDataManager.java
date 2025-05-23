package me.twojplugin.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDataManager {

    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration cfg;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getDataFolder().mkdirs();
        file = new File(plugin.getDataFolder(), "playerdata.yml");
        if (!file.exists()) {
            plugin.saveResource("playerdata.yml", false);
        }
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void savePlayer(Player p) {
        String path = "players." + p.getName();
        cfg.set(path + ".inventory", Arrays.asList(p.getInventory().getContents()));
        cfg.set(path + ".armor", Arrays.asList(p.getInventory().getArmorContents()));
        cfg.set(path + ".xp", p.getTotalExperience());
        cfg.set(path + ".level", p.getLevel());
        cfg.set(path + ".health", p.getHealth());
        cfg.set(path + ".food", p.getFoodLevel());
        Location loc = p.getLocation();
        cfg.set(path + ".world", loc.getWorld().getName());
        cfg.set(path + ".x", loc.getX());
        cfg.set(path + ".y", loc.getY());
        cfg.set(path + ".z", loc.getZ());
        cfg.set(path + ".yaw", loc.getYaw());
        cfg.set(path + ".pitch", loc.getPitch());
        save();
    }

    @SuppressWarnings("unchecked")
    public void loadPlayer(Player p) {
        String path = "players." + p.getName();
        if (!cfg.contains(path)) {
            return;
        }
        List<ItemStack> items = (List<ItemStack>) cfg.getList(path + ".inventory");
        p.getInventory().setContents(items.toArray(new ItemStack[0]));
        List<ItemStack> armor = (List<ItemStack>) cfg.getList(path + ".armor");
        p.getInventory().setArmorContents(armor.toArray(new ItemStack[0]));
        p.setTotalExperience(cfg.getInt(path + ".xp"));
        p.setLevel(cfg.getInt(path + ".level"));
        p.setHealth(cfg.getDouble(path + ".health"));
        p.setFoodLevel(cfg.getInt(path + ".food"));
        World w = Bukkit.getWorld(cfg.getString(path + ".world"));
        if (w != null) {
            p.teleport(new Location(
                    w,
                    cfg.getDouble(path + ".x"),
                    cfg.getDouble(path + ".y"),
                    cfg.getDouble(path + ".z"),
                    (float) cfg.getDouble(path + ".yaw"),
                    (float) cfg.getDouble(path + ".pitch")
            ));
        }
    }

    private void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
