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
        if (!item.getType().equals(Material.NAME_TAG) || !item.getItemMeta().hasDisplayName()) return;

        // don't rename if on cooldown
        if (VillagerUtilities.onAiToggleCooldown(vil, player, plugin, colorCodes)) {
            e.setCancelled(true);
            return;
        }
        
        // Replenish the name-tag
        VillagerUtilities.returnItem(player, plugin);

        // enable or disable the villager
        List<String> namesThatDisable = plugin.getConfig().getStringList("NamesThatDisable");
        String itemName = item.getItemMeta().getDisplayName().replaceAll("(?i)[§&][0-9A-FK-ORX]", "");
        boolean willBeDisabled = namesThatDisable.contains(itemName);
        VillagerUtilities.setDisabledByNametag(vil, plugin, willBeDisabled);
    }
}
