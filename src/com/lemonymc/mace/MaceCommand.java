package com.lemonymc.mace;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaceCommand implements CommandExecutor {

    private final MaceItemManager itemManager;

    public MaceCommand(MaceItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Bu komutu sadece oyuncular kullanabilir.");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (!player.hasPermission("custommace.admin")) {
            player.sendMessage("Bu komutu kullanma yetkin yok.");
            return true;
        }

        player.getInventory().addItem(itemManager.createMaceItem());
        player.sendMessage("Özel Mace'i aldın!");
        return true;
    }
}
