package rebelmythik.antiVillagerLag.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rebelmythik.antiVillagerLag.AntiVillagerLag;
import rebelmythik.antiVillagerLag.utils.CalculateLevel;
import rebelmythik.antiVillagerLag.utils.VillagerUtilities;

public class VillagerLevelManager {

    public static void call(Villager vil, AntiVillagerLag plugin, Player player) {
        int cooldown = 5;
        int vilLevel = vil.getVillagerLevel();
        long newLevel = CalculateLevel.villagerEXP(vil);
        long currentTime = System.currentTimeMillis() / 1000;

        long vilLevelCooldown = VillagerUtilities.getLevelCooldown(vil, plugin);
        long totalSeconds = vilLevelCooldown - currentTime;
        long sec = totalSeconds % 60;

        if (vilLevelCooldown > currentTime) {
            String message = plugin.getConfig().getString("messages.cooldown-levelup-message");
            message = message.replaceAll("%avlseconds%", Long.toString(sec));
            player.sendMessage(VillagerUtilities.colorcodes.cm(message));
            return;
        }

        if (vilLevel < newLevel) {
            VillagerUtilities.setLevelCooldown(vil, plugin, cooldown);
            // make villager immovable while AI is disabled
            vil.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)(cooldown * 20)+20, 120, false, false));
            vil.setAware(true);
        } else return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            vil.setAware(false);
        }, 100L);
    }
}
