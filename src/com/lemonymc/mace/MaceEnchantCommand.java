package com.lemonymc.mace;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class MaceEnchantCommand implements CommandExecutor {

    private final MaceItemManager itemManager;

    public MaceEnchantCommand(MaceItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Bu komutu sadece oyuncular kullanabilir.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("custommace.enchant")) {
            player.sendMessage("Bu komutu kullanma yetkin yok.");
            return true;
        }

        if (args.length < 1) {
            player.sendMessage("§cKullanım: /macebuyu <Breach|Density|WindBurst> [level (varsayılan: 1)]");
            return true;
        }

        ItemStack mace = player.getInventory().getItemInMainHand();
        if (!itemManager.isMace(mace)) {
            player.sendMessage("§cBu komutu sadece elinde Gürz (Mace) tutarken kullanabilirsin.");
            return true;
        }
        
        String enchantName = args[0];
        String enchantNameUpper = enchantName.toUpperCase(Locale.ENGLISH);
        int level = args.length > 1 ? parseLevel(args[1]) : 1;

        if (enchantNameUpper.equals("BREACH") || enchantNameUpper.equals("DENSITY") || enchantNameUpper.equals("WINDBURST")) {
            
            itemManager.applyCustomEnchantment(mace, enchantNameUpper, level);
            
            player.sendMessage("§bGürz'e " + enchantName + " (Seviye " + level + ") büyüsü eklendi.");
            player.sendMessage("§aNot: Büyülerde sorun olursa lütfen bildirin.");

        } else {
            player.sendMessage("§cBilinmeyen büyü: " + enchantName + ". Kullanılabilir büyüler: Breach, Density, WindBurst.");
            return true;
        }
        
        return true;
    }

    private int parseLevel(String arg) {
        try {
            return Math.min(5, Math.max(1, Integer.parseInt(arg))); 
        } catch (NumberFormatException e) {
            return 1;
        }
    }
}
