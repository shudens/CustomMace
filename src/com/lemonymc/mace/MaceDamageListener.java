package com.lemonymc.mace;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class MaceDamageListener implements Listener {

    private final MaceItemManager itemManager;
	
    public MaceDamageListener(MaceItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        // Gürz item kontrolü
        if (!itemManager.isMace(player.getInventory().getItemInMainHand())) return;

        // Vurucu oyuncunun düşüş mesafesini kontrol et (Minimum 3 blok düşüyor olmalı)
        float fallDistance = player.getFallDistance();
        boolean isFalling = fallDistance >= 3.0f; 

        // Kritik Vuruş kontrolü:
        // Minecraft'ta kritik vuruşlar genellikle son hasar türünün (DamageCause) 
        // ENTITY_ATTACK veya PROJECTILE olmadığı, yani özel bir hasar türü olmadığı
        // ve oyuncunun havada olduğu (düşüyor olduğu) zaman tetiklenir.
        // Basit bir kritik vuruş kontrolü için fallDistance ve hasar bilgisini kullanabiliriz.
        
        // Basitçe: Eğer düşüyorsa ve hasar türü fiziksel saldırıysa, kritik vuruş varsayımı yapıyoruz.
        // Daha kesin bir kritik kontrol için, hasar miktarının varsayılan hasardan yüksek olup 
        // olmadığına bakmak gerekir, ancak bu kodda daha basit bir kontrol yeterli olacaktır.
        // Orijinal Gürz mekaniği düşme hasarı iptali ve fırlatmayı bu şartlara bağlar.
        
        // Eğer oyuncu yeterince yükseklikten düşmüyorsa VEYA kritik vuruş (düşme hasarı mekaniği) yapmıyorsa,
        // ana Gürz mekaniğini çalıştırma.
        if (!isFalling) {
            // Sadece büyüler uygulansın ve ana fırlatma mekaniği atlanmış olsun.
            itemManager.applyEnchantEffects(player, target);
            return; 
        }

        // --- GÜRZ'ÜN ANA MEKANİĞİ: VURUCUYU FIRLATMA (Şartlar Sağlandı) ---
        
        // Breach ve Density gibi HEDEFE etki eden büyüleri uygula.
        // Bu kısım, fırlatma öncesinde/sonrasında her zaman uygulanmalıdır.
        itemManager.applyEnchantEffects(player, target);
        
        // Windburst seviyesini al
        int windburstLevel = itemManager.getCustomEnchantmentLevel(
            player.getInventory().getItemInMainHand(), "WINDBURST");
        
        // Config’ten değerleri çek
        double BASE_MACE_JUMP  = itemManager.getPlugin().getConfig().getDouble("max_ziplama", 0.6);
        double WINDBURST_JUMP_MULTIPLIER = itemManager.getPlugin().getConfig().getDouble("max_ziplama_wind", 0.2);

        // Fırlatma gücünü hesapla: Taban Fırlatma + Windburst Etkisi
        double totalJumpPower = BASE_MACE_JUMP + (windburstLevel * WINDBURST_JUMP_MULTIPLIER);
        
        // Oyuncuyu havaya fırlat
        Vector playerVel = player.getVelocity();
        // Sadece Y (dikey) koordinatını ayarla
        playerVel.setY(totalJumpPower); 
        player.setVelocity(playerVel);
        
        // Düşme hasarını iptal et (Gürz'ün temel mekaniği)
        player.setFallDistance(0);
        
        // NOT: Oyunun yerleşik kritik vuruş sistemi, oyuncunun düşüyor olmasıyla zaten ilişkilidir. 
        // Bu yüzden 'isFalling' kontrolü, Gürz'ün kritik vuruş mekaniğini simüle etmek için yeterlidir.
    }
}