package rebelmythik.antivillagerlag.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;


public class NameTagAI {
    private final AntiVillagerLag plugin;
    ColorCode colorCodes = new ColorCode();
    private final long cooldown;

    public NameTagAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.cooldown = plugin.getConfig().getLong("cooldown");
    }


    // automatically sends a message to the player
    private boolean hasCooldown(Villager vil, Player player, PlayerInteractEntityEvent e){

        // Permission to Bypass Cooldown.
        if (!player.hasPermission("avl.renamecooldown.bypass"))
            return false;

        // create variables
        long vilCooldown = VillagerUtilities.getCooldown(vil, plugin);
        long currentTime = System.currentTimeMillis() / 1000;

         // if the cooldown is over and send message if it isn't
        if (vilCooldown > currentTime) {

            long totalSeconds = vilCooldown - currentTime;
            long sec = totalSeconds % 60;
            long min = (totalSeconds - sec) / 60;

            String message = plugin.getConfig().getString("messages.cooldown-message");
            if (message.contains("%avlminutes%")) {
                message = VillagerUtilities.replaceText(message, "%avlminutes%", Long.toString(min));
            }
            message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
            player.sendMessage(colorCodes.cm(message));
            // player is trying to rename, stop them! (is safe to cancel)
            e.setCancelled(true);
            return true;
        }
        return false;
    }

    public void call(Villager vil, Player player, PlayerInteractEntityEvent e) {

        // Toggle Option To Disable this Class
        if (!plugin.getConfig().getBoolean("toggleableoptions.userenaming")) return;
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check that the player uses a named name-tag
        if (!item.getType().equals(Material.NAME_TAG) || !item.getItemMeta().hasDisplayName())
            return;

        // Replenish the name-tag
        VillagerUtilities.returnItem(player, plugin);

        boolean willBeDisabled = item.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getConfig().getString("NameThatDisables"));

        // Handle the correct AI state
        if(vil.hasAI()) {
            // Check that the villager is disabled or has cooldown
            if (!willBeDisabled)
                return;

            // check if villager has AI Toggle cooldown
            if (hasCooldown(vil, player, e))
                return;

            vil.setAI(false);
            // set all necessary flags and timers
            VillagerUtilities.setMarker(vil, plugin);
            VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
        } else {
            // Re-Enabling AI
            // Check that the villager is disabled
            if (willBeDisabled )
                return;

            // check if villager has AI Toggle cooldown
            if (hasCooldown(vil, player, e))
                return;

            // check if Villager was disabled by AVL
            // prevents breaking NPC plugins
            if (!VillagerUtilities.hasMarker(vil, plugin)) return;
            vil.setAI(true);
            VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
            // remove the marker again
            VillagerUtilities.removeMarker(vil, plugin);
        }
    }
}
