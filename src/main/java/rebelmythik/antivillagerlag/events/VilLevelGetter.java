package rebelmythik.antivillagerlag.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import rebelmythik.antivillagerlag.AntiVillagerLag;

public class VilLevelGetter implements Listener {

    public AntiVillagerLag plugin;

    public VilLevelGetter(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void rightCLick(PlayerInteractEntityEvent e) {

        Entity entity = e.getRightClicked();
        if (!(entity.getType().equals(EntityType.VILLAGER))) return;
        Villager vil = (Villager) entity;
        int vilEXP = vil.getVillagerExperience();
        plugin.getLogger().info(String.valueOf(vilEXP));

    }
}
