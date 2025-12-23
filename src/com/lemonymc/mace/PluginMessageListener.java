package com.lemonymc.mace;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginMessageListener implements Listener {

    private final JavaPlugin plugin;

    public PluginMessageListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ANSI renk kodları (console için)
    private static final String RESET = "\u001B[0m";
    private static final String CYAN_BRIGHT = "\u001B[96m";   // Açık Aqua
    private static final String GREEN_BRIGHT = "\u001B[92m";  // Açık Yeşil
    private static final String YELLOW_BRIGHT = "\u001B[93m"; // Açık Sarı
    private static final String MAGENTA_BRIGHT = "\u001B[95m";// Açık Mor
    private static final String WHITE_BRIGHT = "\u001B[97m";  // Açık Beyaz

    // Plugin başlatıldığında mesajı gönder
    public void sendStartupMessage() {
        // Konsol için renkli büyük banner
        String consoleMsg = "\n" +
                CYAN_BRIGHT + "############################################################" + RESET + "\n" +
                CYAN_BRIGHT + "#                                                          #" + RESET + "\n" +
                GREEN_BRIGHT + "#                 CustomMace Başarıyla Yüklendi!            #" + RESET + "\n" +
                CYAN_BRIGHT + "#                                                          #" + RESET + "\n" +
                YELLOW_BRIGHT + "#   " + WHITE_BRIGHT + "Plugin Sürümü: " + MAGENTA_BRIGHT + plugin.getDescription().getVersion() + RESET + "                              " + YELLOW_BRIGHT + "#" + RESET + "\n" +
                YELLOW_BRIGHT + "#   " + WHITE_BRIGHT + "Developer: " + MAGENTA_BRIGHT + "sHuDev/LemonyMC" + RESET + "                                     " + YELLOW_BRIGHT + "#" + RESET + "\n" +
                YELLOW_BRIGHT + "#   " + WHITE_BRIGHT + "Github: " + MAGENTA_BRIGHT + "github.com/LemonyMC-Dev" + RESET + "                 " + YELLOW_BRIGHT + "#" + RESET + "\n" +
                CYAN_BRIGHT + "#                                                          #" + RESET + "\n" +
                GREEN_BRIGHT + "#                İyi oyunlar dileriz!                      #" + RESET + "\n" +
                CYAN_BRIGHT + "#                                                          #" + RESET + "\n" +
                CYAN_BRIGHT + "############################################################" + RESET + "\n";

        System.out.println(consoleMsg);

        // OP oyuncular için renkli Minecraft mesajı
        String playerMsg = "\n" +
                ChatColor.AQUA + "§l############################################################\n" +
                ChatColor.GREEN + "§l#                 CustomMace Aktif!                        #\n" +
                ChatColor.YELLOW + "§l#                                                          #\n" +
                ChatColor.LIGHT_PURPLE + "§l#   Developer: LemonyMC                                    #\n" +
                ChatColor.YELLOW + "§l#   Github: github.com/LemonyMC-Dev                        #\n" +
                ChatColor.GREEN + "§l#   Keyifli oyunlar!                                       #\n" +
                ChatColor.AQUA + "§l############################################################\n";

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(playerMsg);
            }
        }
    }

    // OP girince mesaj
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            String msg = "\n" +
                    ChatColor.AQUA + "§l############################################################\n" +
                    ChatColor.GREEN + "§l#                 CustomMace Aktif!                        #\n" +
                    ChatColor.YELLOW + "§l#                                                          #\n" +
                    ChatColor.LIGHT_PURPLE + "§l#   Customizer: sHuDev                                    #\n" +
                    ChatColor.YELLOW + "§l#   Github: github.com/shudens                      #\n" +
                    ChatColor.GREEN + "§l#   Keyifli oyunlar!                                       #\n" +
                    ChatColor.AQUA + "§l############################################################\n";
            player.sendMessage(msg);
        }
    }
}