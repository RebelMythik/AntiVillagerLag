package rebelmythik.antivillagerlag.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.CalculateLevel;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

public class VillagerLevelManager implements Listener {
    public AntiVillagerLag plugin;
    ColorCode colorCodes = new ColorCode();
    long cooldown;
    public VillagerLevelManager(AntiVillagerLag plugin) {
        this.plugin = plugin;
        cooldown = 5;
    }

    @EventHandler
    public void levelChanger(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        Player player = e.getPlayer();
        if (!(entity.getType().equals(EntityType.VILLAGER))) return;
        Villager vil = (Villager) entity;
        int vilLevel = vil.getVillagerLevel();
        long newLevel = CalculateLevel.villagerEXP(vil);
        long currentTime = System.currentTimeMillis() / 1000;

        if (!VillagerUtilities.hasLevelCooldown(vil, plugin)) VillagerUtilities.setLevelCooldown(vil, plugin, (long)0);

        long vilLevelCooldown = VillagerUtilities.getLevelCooldown(vil, plugin);
        Long totalSeconds = vilLevelCooldown - currentTime;
        Long sec = totalSeconds % 60;

        if (vilLevelCooldown >= currentTime) {
            String message = plugin.getConfig().getString("messages.cooldown-levelup-message");
            message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
            player.sendMessage(colorCodes.cm(message));
            e.setCancelled(true);
            return;
        }

        if (!VillagerUtilities.isDisabled(vil, plugin)) return;
        if (vilLevel < newLevel) {
            e.setCancelled(true);
            VillagerUtilities.setLevelCooldown(vil, plugin, cooldown);
            vil.setAI(true);
        } else return;


        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            vil.setAI(false);
        }, 100L);
    }
}
