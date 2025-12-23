package com.lemonymc.mace;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final String versionURL = "https://raw.githubusercontent.com/LemonyMC-Dev/CustomMace/refs/heads/main/version.txt"; 
    // Buraya GitHub veya sunucunda güncel sürümü tutan dosya linki gelecek

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
    }
	
	private static final String CYAN_BRIGHT = "\u001B[96m";   // Açık Aqua
    private static final String GREEN_BRIGHT = "\u001B[92m";  // Açık Yeşil

    public void checkForUpdate() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL(versionURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                Scanner scanner = new Scanner(new InputStreamReader(connection.getInputStream()));
                if (scanner.hasNextLine()) {
                    String latestVersion = scanner.nextLine().trim();
                    String currentVersion = plugin.getDescription().getVersion();

                    if (!currentVersion.equals(latestVersion)) {
                        Bukkit.getScheduler().runTask(plugin, () -> {
                            String msg = CYAN_BRIGHT + "Yeni sürüm mevcut: " + latestVersion + GREEN_BRIGHT + " (Senin sürümün: " + currentVersion + ")";
                            plugin.getLogger().info(msg.replace("§", ""));
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.isOp()) {
                                    p.sendMessage(msg);
                                }
                            }
                        });
                    }
                }
                scanner.close();
            } catch (Exception e) {
                plugin.getLogger().warning("Güncelleme kontrolü sırasında bir hata oluştu: " + e.getMessage());
            }
        });
    }
}