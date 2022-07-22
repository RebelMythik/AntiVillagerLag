package rebelmythik.antivillagerlag.events;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;


public class NameTagAI implements Listener {
    public AntiVillagerLag plugin;
    long cooldown;

    public NameTagAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        cooldown = plugin.getConfig().getLong("cooldown");
    }


    @EventHandler
    public void rightClick(PlayerInteractEntityEvent e) {
        // Toggle Option To Disable this Class

        if (!plugin.getConfig().getBoolean("toggleableoptions.userenaming")) return;
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        ItemStack item = player.getInventory().getItemInMainHand();
        String hasAI = String.valueOf(((Villager) entity).hasAI()).toUpperCase();
        long currenttime = System.currentTimeMillis() / 1000;


        if (!(entity instanceof Villager)) return;
        Villager vil = (Villager) entity;
        long vilCooldown = VillagerUtilities.getCooldown(vil, plugin);

        if (!item.getType().equals(Material.NAME_TAG)) return;

        if (!VillagerUtilities.hasCooldown(vil, plugin)) {
            VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
        }

        switch (hasAI) {
            // Disabling AI
            case "TRUE":
                // Check if the Villager Name is null and does not have the configured name

                if (!item.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getConfig().getString("NameThatDisables"))) return;

                vil.setAI(false);
                plugin.getLogger().info("AI has been Disabled");
                break;

            // Re-Enabling AI
            case "FALSE":
                // Check if the Villager Name is not null and does not have the configured name
                vil.getName();
                if (vil.getName().equals(plugin.getConfig().getString("NameThatDisables"))) return;
                vil.setAI(true);
                plugin.getLogger().info("AI has been Re-Enabled");
                break;
        }
        return;
    }
}
