package rebelmythik.antivillagerlag.events;

import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

public class CancelVillagerDamage implements Listener {

    private AntiVillagerLag plugin;

    public CancelVillagerDamage(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCancelVillagerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Villager && event.getDamager() instanceof Zombie)) return;

        Villager vil = (Villager) event.getEntity();

        if (VillagerUtilities.hasMarker(vil, plugin)) {
            event.setCancelled(true);
        }
    }
}
