package com.lemonymc.mace;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MaceItemManager {

    private final JavaPlugin plugin;
    public static final Material MACE_MATERIAL = Material.NETHERITE_AXE;
    public static final NamespacedKey MACE_KEY = new NamespacedKey("custommace117", "is_mace"); 
    public static final NamespacedKey BREACH_KEY = new NamespacedKey("custommace117", "breach");
    public static final NamespacedKey DENSITY_KEY = new NamespacedKey("custommace117", "density");
    public static final NamespacedKey WINDBURST_KEY = new NamespacedKey("custommace117", "windburst");
    int customId;

    public MaceItemManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.customId = plugin.getConfig().getInt("custom_id", 1001);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public ItemStack createMaceItem() {
        ItemStack mace = new ItemStack(MACE_MATERIAL);
        ItemMeta meta = mace.getItemMeta();

        meta.setDisplayName("§6Gürz"); 
        meta.setCustomModelData(customId);
        
        List<String> lore = new ArrayList<>();
        lore.add("§7");
        meta.setLore(lore);
        
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(MACE_KEY, PersistentDataType.BYTE, (byte) 1);
        
        mace.setItemMeta(meta);
        
        return mace;
    }
    
    public boolean isMace(ItemStack item) {
        if (item == null || item.getType() != MACE_MATERIAL || !item.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().has(MACE_KEY, PersistentDataType.BYTE);
    }
    
    public void applyCustomEnchantment(ItemStack mace, String enchantName, int level) {
        ItemMeta meta = mace.getItemMeta();
        NamespacedKey key;
        String displayName;
        
        switch (enchantName.toUpperCase(Locale.ENGLISH)) {
            case "BREACH": key = BREACH_KEY; displayName = "§eBreach"; break;
            case "DENSITY": key = DENSITY_KEY; displayName = "§eDensity"; break;
            case "WINDBURST": key = WINDBURST_KEY; displayName = "§eWind Burst"; break;
            default: return;
        }
        
        meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, level);
        
        List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
        lore.removeIf(line -> line.contains(displayName));
        lore.add(3, displayName + " " + toRoman(level)); 
        
        meta.setLore(lore);
        mace.setItemMeta(meta);
    }
    
    public int getCustomEnchantmentLevel(ItemStack item, String enchantName) {
        if (!isMace(item)) return 0;
        
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key;
        
        switch (enchantName.toUpperCase(Locale.ENGLISH)) {
            case "BREACH": key = BREACH_KEY; break;
            case "DENSITY": key = DENSITY_KEY; break;
            case "WINDBURST": key = WINDBURST_KEY; break;
            default: return 0;
        }
        
        if (meta.getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
            return meta.getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
        }
        return 0;
    }

    public void applyEnchantEffects(Player player, LivingEntity target) {
        ItemStack item = player.getInventory().getItemInMainHand();

        int breachLevel = getCustomEnchantmentLevel(item, "BREACH");
        if (breachLevel > 0) {
            target.setFireTicks(20 * breachLevel);
        }

        int densityLevel = getCustomEnchantmentLevel(item, "DENSITY");
        if (densityLevel > 0) {
            Vector back = player.getLocation().getDirection().multiply(-0.2 * densityLevel);
            back.setY(0);
            target.setVelocity(target.getVelocity().add(back));
        }
    }

    public void registerMaceRecipe() {
        ItemStack maceItem = createMaceItem();
        
        ShapedRecipe recipe = new ShapedRecipe(MACE_KEY, maceItem);
        recipe.shape(" T ", " B ", " S "); 
        recipe.setIngredient('T', Material.TRIDENT); 
        recipe.setIngredient('B', Material.NETHERITE_BLOCK);
        recipe.setIngredient('S', Material.STICK);
        
        plugin.getServer().addRecipe(recipe);
    }
    
    private String toRoman(int num) {
        if (num < 1) return "";
        
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        
        StringBuilder roman = new StringBuilder();
        
        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                roman.append(symbols[i]);
                num -= values[i];
            }
        }
        return roman.toString();
    }
}