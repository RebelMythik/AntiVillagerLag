package rebelmythik.antivillagerlag.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

import java.util.List;


public class NameTagAI {
    private final AntiVillagerLag plugin;
    ColorCode colorCodes = new ColorCode();
    private final long cooldown;

    public NameTagAI(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.cooldown = plugin.getConfig().getLong("cooldown");
    }

    public void call(Villager vil, Player player, PlayerInteractEntityEvent e) {
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check that the player uses a named name-tag
        if (!item.getType().equals(Material.NAME_TAG) || !item.getItemMeta().hasDisplayName())
            return;

        // Replenish the name-tag
        if (!VillagerUtilities.onAiToggleCooldown(vil, player, plugin, colorCodes)) {
            VillagerUtilities.returnItem(player, plugin);
        }
        List<String> namesThatDisable = plugin.getConfig().getStringList("NamesThatDisable");
        String itemName = item.getItemMeta().getDisplayName().replaceAll("(?i)[§&][0-9A-FK-ORX]", "");
        boolean willBeDisabled = namesThatDisable.contains(itemName);
        VillagerUtilities.setDisabledByNametag(vil, plugin, willBeDisabled);

        // Handle the correct AI state
        if(vil.isAware()) {
            // Check that the villager is disabled or has cooldown
            if (!willBeDisabled) {
                return;
            }
            // check if villager has AI Toggle cooldown
            if (VillagerUtilities.hasRestockCooldown(vil, plugin)) {
                return;
            }

            vil.setAware(false);
            // set all necessary flags and timers
            VillagerUtilities.setMarker(vil, plugin);
            VillagerUtilities.setNewRestockCooldown(vil, plugin, cooldown);
        } else {
            // Re-Enabling AI
            // Check that the villager is disabled
            if (willBeDisabled) {
                return;
            }
            // check if villager has AI Toggle cooldown
            if (VillagerUtilities.onAiToggleCooldown(vil, player, plugin, colorCodes)) {
                return;
            }

            // check if Villager was disabled by AVL
            // prevents breaking NPC plugins
            if (!VillagerUtilities.hasMarker(vil, plugin)) return;
            vil.setAware(true);
            VillagerUtilities.setNewRestockCooldown(vil, plugin, cooldown);
            // remove the marker again
            VillagerUtilities.removeMarker(vil, plugin);
        }
    }
}
