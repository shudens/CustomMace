package com.lemonymc.mace;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;
    private double maxZiplama;
    private double maxZiplamaWind;
    private int customId;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        plugin.saveDefaultConfig(); 
        FileConfiguration config = plugin.getConfig();

        this.maxZiplama = config.getDouble("max_ziplama", 0.6);
        this.maxZiplamaWind = config.getDouble("max_ziplama_wind", 0.2);
        this.customId = config.getInt("custom_id", 1001);
    }

    public double getMaxZiplama() {
        return maxZiplama;
    }

    public double getMaxZiplamaWind() {
        return maxZiplamaWind;
    }

    public int getCustomId() {
        return customId;
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        loadConfig();
    }
}