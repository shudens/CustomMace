package com.lemonymc.mace;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private MaceItemManager itemManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        // Config manager
        this.configManager = new ConfigManager(this);

        //-----KONTROL-----
        this.itemManager = new MaceItemManager(this);
        getServer().getPluginManager().registerEvents(new MaceDamageListener(itemManager), this);
        //-------------------------

        //-----MACE ALMA-----
        getCommand("mace").setExecutor(new MaceCommand(itemManager));
        //-------------------------

        //-----MACE BUYU-----
        getCommand("macebuyu").setExecutor(new MaceEnchantCommand(itemManager));
        itemManager.registerMaceRecipe();
        //-------------------------

        //-----RELOAD KOMUTU-----
        getCommand("macereload").setExecutor((sender, command, label, args) -> {
            configManager.reloadConfig();
            sender.sendMessage("§aCustomMace config dosyası yeniden yüklendi!");
            return true;
        });
        //-------------------------

        //-----GÜNCELLEMELERİ------
        UpdateChecker updateChecker = new UpdateChecker(this);
        updateChecker.checkForUpdate();
        //-------------------------

        //-----BAŞLAMA MESAJI-----
        PluginMessageListener messageListener = new PluginMessageListener(this);
        getServer().getPluginManager().registerEvents(messageListener, this);
        messageListener.sendStartupMessage();
        //-------------------------
    }

    @Override
    public void onDisable() {
        getLogger().info("CustomMace devre dışı bırakılıyor.");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}