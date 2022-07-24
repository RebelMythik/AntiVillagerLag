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

        // Check that the entity is a villager and create time variables
        if (!(entity instanceof Villager)) return;
        String hasAI = String.valueOf(((Villager) entity).hasAI()).toUpperCase();
        Villager vil = (Villager) entity;
        Location loc = vil.getLocation();

        // Check whether this villager has a cooldown tag
        if (!VillagerUtilities.hasCooldown(vil, plugin)) VillagerUtilities.setNewCooldown(vil, plugin, (long)0);
        if (!VillagerUtilities.hasLevelCooldown(vil, plugin)) VillagerUtilities.setLevelCooldown(vil, plugin, (long)0);
        long vilCooldown = VillagerUtilities.getCooldown(vil, plugin);
        long currentTime = System.currentTimeMillis() / 1000;
        Long totalSeconds = vilCooldown - currentTime;
        Long sec = totalSeconds % 60;
        Long min = (totalSeconds - sec) / 60;
        long vilLevelCooldown = VillagerUtilities.getLevelCooldown(vil, plugin);

        // Permissions to Bypass Cooldown. If they don't have permission run to see if the cooldown is over and send message if it isn't
        if (!player.hasPermission("avl.blockcooldown.bypass")) {
            if (vilCooldown > currentTime) {
                String message = plugin.getConfig().getString("messages.cooldown-block-message");
                if (message.contains("%avlminutes%")) {
                    message = VillagerUtilities.replaceText(message, "%avlminutes%", Long.toString(min));
                }
                message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
                player.sendMessage(colorCodes.cm(message));
                return;
            }
        }

        // Check if the villager is disabled for leveling
        if (vilLevelCooldown > currentTime) {
            e.setCancelled(true);
            return;
        }

        // Handle the correct AI state
        switch (hasAI) {
            // Disabling AI
            case "TRUE":
                // Check that the villager is on the correct block
                if (!vil.getWorld().getBlockAt(loc.getBlockX(), (loc.getBlockY() - 1), loc.getBlockZ()).getType().equals(Material.getMaterial(plugin.getConfig().getString("BlockThatDisables")))) return;
                VillagerUtilities.setMarker(vil, plugin);

                vil.setAI(false);
                VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
                break;

            // Re-Enabling AI
            case "FALSE":
                // Check that the villager is on the correct block
                if (vil.getWorld().getBlockAt(loc.getBlockX(), (loc.getBlockY() - 1), loc.getBlockZ()).getType().equals(Material.getMaterial(plugin.getConfig().getString("BlockThatDisables")))) return;
                if (!VillagerUtilities.hasMarker(vil, plugin)) return;

                vil.setAI(true);
                VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
                break;
        }
    }
}
