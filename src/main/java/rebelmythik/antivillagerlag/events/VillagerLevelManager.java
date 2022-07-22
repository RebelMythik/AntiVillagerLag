package rebelmythik.antivillagerlag.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.CalculateLevel;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

public class VillagerLevelManager implements Listener {
    public AntiVillagerLag plugin;
    public VillagerLevelManager(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void levelChanger(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (!(entity.getType().equals(EntityType.VILLAGER))) return;
        Villager vil = (Villager) entity;
        int vilLevel = vil.getVillagerLevel();
        long newLevel = CalculateLevel.villagerEXP(vil);
        if (!VillagerUtilities.isDisabled(vil, plugin)) return;
        if (vilLevel < newLevel) {
            e.setCancelled(true);
            vil.setAI(true);
            plugin.getLogger().info("Updating Villager Level!");
        } else return;

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            vil.setAI(false);
            plugin.getLogger().info("Disabled the AI!");
        }, 100L);
    }
}
