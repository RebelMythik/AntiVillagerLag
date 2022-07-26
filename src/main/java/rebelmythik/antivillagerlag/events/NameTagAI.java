package rebelmythik.antivillagerlag.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;


public class NameTagAI {
    public AntiVillagerLag plugin;
    ColorCode colorCodes = new ColorCode();
    long cooldown;

    public NameTagAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.cooldown = plugin.getConfig().getLong("cooldown");
    }

    public void call(Villager vil, Player player) {

        // Toggle Option To Disable this Class
        if (!plugin.getConfig().getBoolean("toggleableoptions.userenaming")) return;
        ItemStack item = player.getInventory().getItemInMainHand();


        // create variables
        String hasAI = String.valueOf(vil.hasAI()).toUpperCase();

        long vilCooldown = VillagerUtilities.getCooldown(vil, plugin);

        long currentTime = System.currentTimeMillis() / 1000;
        long totalSeconds = vilCooldown - currentTime;
        long sec = totalSeconds % 60;
        long min = (totalSeconds - sec) / 60;

        // Check that the player uses a name-tag
        if (!item.getType().equals(Material.NAME_TAG)) return;

        // Permissions to Bypass Cooldown. If they don't have permission run to see if the cooldown is over and send message if it isn't
        if (!player.hasPermission("avl.renamecooldown.bypass")) {
            if (vilCooldown > currentTime) {
                String message = plugin.getConfig().getString("messages.cooldown-message");
                if (message.contains("%avlminutes%")) {
                    message = VillagerUtilities.replaceText(message, "%avlminutes%", Long.toString(min));
                }
                message = VillagerUtilities.replaceText(message, "%avlseconds%", Long.toString(sec));
                player.sendMessage(colorCodes.cm(message));
                return;
            }
        }

        // Replenish the name-tag and handle the correct AI state
        VillagerUtilities.returnItem(player, plugin);
        switch (hasAI) {
            // Disabling AI
            case "TRUE":
                // Check if the name-tag has the correct name for disabling
                if (!item.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getConfig().getString("NameThatDisables"))) return;
                VillagerUtilities.setMarker(vil, plugin);
                vil.setAI(false);
                VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
                break;

            // Re-Enabling AI
            case "FALSE":
                // Check if the name-tag has the correct name for re-enabling
                if (item.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.getConfig().getString("NameThatDisables"))) return;
                if (!VillagerUtilities.hasMarker(vil, plugin)) return;

                vil.setAI(true);
                VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
                break;
        }
        return;
    }
}
