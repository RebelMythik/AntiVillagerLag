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

    public static void handleAiState(Villager vil, AntiVillagerLag plugin, boolean willBeDisabled){


        if(vil.hasAI()) {
            // Check that the villager is disabled
            if (!willBeDisabled)
                return;
            VillagerUtilities.setMarker(vil, plugin);
            vil.setAI(false);
        } else {
            // Re-Enabling AI
            // Check that the villager is disabled
            if (willBeDisabled)
                return;
            if (!VillagerUtilities.hasMarker(vil, plugin)) return;
            vil.setAI(true);
        }
    }

    public void call(Villager vil, Player player, PlayerInteractEntityEvent e) {

        // Toggle Option To Disable this Class
        if (!plugin.getConfig().getBoolean("toggleableoptions.userenaming")) return;
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check that the player uses a named name-tag
        if (!item.getType().equals(Material.NAME_TAG) || !item.getItemMeta().hasDisplayName())
            return;

        // create variables
        long vilCooldown = VillagerUtilities.getCooldown(vil, plugin);
        long currentTime = System.currentTimeMillis() / 1000;
        long totalSeconds = vilCooldown - currentTime;
        long sec = totalSeconds % 60;
        long min = (totalSeconds - sec) / 60;


        // Permissions to Bypass Cooldown. If they don't have permission run to see
        // if the cooldown is over and send message if it isn't
        if (item.getType().equals(Material.NAME_TAG)) {
            plugin.getLogger().info("NameTagAI being a bitch");
            if (!player.hasPermission("avl.renamecooldown.bypass")) {
                if (vilCooldown > currentTime) {
                    String message = plugin.getConfig().getString("messages.cooldown-message");
                    if (message.contains("%avlminutes%")) {
                        message = VillagerUtilities.replaceText(message, "%avlminutes%", Long.toString(min));
                    }
                    message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
                    player.sendMessage(colorCodes.cm(message));
                    // player is trying to rename, stop them! (is safe to cancel)
                    e.setCancelled(true);
                    return;
                }
            }
        }

        // Replenish the name-tag and handle the correct AI state
        VillagerUtilities.returnItem(player, plugin);

        boolean willBeDisabled = item.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getConfig().getString("NameThatDisables"));

        // Handle the correct AI state
        handleAiState(vil, this.plugin, willBeDisabled);
        VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
    }
}
