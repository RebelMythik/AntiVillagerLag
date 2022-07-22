package rebelmythik.antivillagerlag.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

public class BlockAI implements Listener {
    public AntiVillagerLag plugin;
    ColorCode colorCodes = new ColorCode();
    long cooldown;

    public BlockAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        cooldown = plugin.getConfig().getLong("cooldown");
    }

    @EventHandler
    public void rightClick(PlayerInteractEntityEvent e) {
        // Toggle Option To Disable this Class
        if (!plugin.getConfig().getBoolean("toggleableoptions.useblocks")) return;
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        String hasAI = String.valueOf(((Villager) entity).hasAI()).toUpperCase();

        // Check that the entity is a villager and create time variables
        if (!(entity instanceof Villager)) return;
        Villager vil = (Villager) entity;
        Location loc = vil.getLocation();

        // Check whether this villager has a cooldown tag
        if (!VillagerUtilities.hasCooldown(vil, plugin)) VillagerUtilities.setNewCooldown(vil, plugin, (long)0);
        long vilCooldown = VillagerUtilities.getCooldown(vil, plugin);
        long currentTime = System.currentTimeMillis() / 1000;
        Long totalSeconds = vilCooldown - currentTime;
        Long sec = totalSeconds % 60;
        Long min = (totalSeconds - sec) / 60;

        // Permissions to Bypass Cooldown. If they don't have permission run to see if the cooldown is over and send message if it isn't
        if (!player.hasPermission("avl.blockcooldown.bypass")) {
            if (vilCooldown >= currentTime) {
                String message = plugin.getConfig().getString("cooldown-block-message");
                if (message.contains("%avlminutes%")) {
                    message = VillagerUtilities.replaceText(message, "%avlminutes%", Long.toString(min));
                }
                message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
                player.sendMessage(colorCodes.cm(message));
                e.setCancelled(true);
                return;
            }
        }

        // Handle the correct AI state
        switch (hasAI) {
            // Disabling AI
            case "TRUE":
                // Check that the villager is on the correct block
                if (!vil.getWorld().getBlockAt(loc.getBlockX(), (loc.getBlockY() - 1), loc.getBlockZ()).getType().equals(Material.getMaterial(plugin.getConfig().getString("BlockThatDisables")))) return;

                vil.setAI(false);
                VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
                plugin.getLogger().info("AI has been Disabled");
                break;

            // Re-Enabling AI
            case "FALSE":
                // Check that the villager is on the correct block
                if (vil.getWorld().getBlockAt(loc.getBlockX(), (loc.getBlockY() - 1), loc.getBlockZ()).getType().equals(Material.getMaterial(plugin.getConfig().getString("BlockThatDisables")))) return;

                vil.setAI(true);
                VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
                plugin.getLogger().info("AI has been Re-Enabled");
                break;
        }
    }
}
