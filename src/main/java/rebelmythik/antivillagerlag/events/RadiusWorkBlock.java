package rebelmythik.antivillagerlag.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

import java.util.ArrayList;
import java.util.List;

public class RadiusWorkBlock {
    private final AntiVillagerLag plugin;
    ColorCode colorCodes = new ColorCode();

    long cooldown;

    private List<Material> blocksToCheck = new ArrayList<>();

    public RadiusWorkBlock(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.cooldown = plugin.getConfig().getLong("cooldown");
    }

    public void call(Villager vil, Player player) {
        int radius = plugin.getConfig().getInt("toggleableoptions.workstationcheckradius");
        boolean willBeDisabled = false;

        for (String blockName : plugin.getConfig().getStringList("WorkstationsThatDisable")) {
            Material block = Material.getMaterial(blockName.toUpperCase());
            if (block != null) {
                blocksToCheck.add(block);
            }
        }

        // Check for blocks within the specified radius
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location blockLocation = new Location(vil.getWorld(), vil.getLocation().getX() + x, vil.getLocation().getY() + y, vil.getLocation().getZ() + z);
                    if (blocksToCheck.contains(blockLocation.getBlock().getType())) {
                        willBeDisabled = true;
                    }
                }
            }
        }

        // Handle the correct AI state
        if(vil.isAware()) {
            // Check that the villager is disabled
            if (!willBeDisabled)
                return;
            // check if villager has AI Toggle cooldown
            if(VillagerUtilities.hasCooldown(vil, player, plugin, colorCodes))
                return;
            vil.setAware(false);
            // set all necessary flags and timers
            VillagerUtilities.setMarker(vil, plugin);
            VillagerUtilities.setDisabledByWorkstation(vil, plugin, true);
            VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
        } else {
            // Re-Enabling AI
            // Check that the villager is disabled and disabled by Block
            if (willBeDisabled || !VillagerUtilities.getDisabledByWorkstation(vil, plugin))
                return;
            // check if villager has AI Toggle cooldown
            if(VillagerUtilities.hasCooldown(vil, player, plugin, colorCodes))
                return;
            // check if Villager was disabled by AVL
            // prevents breaking NPC plugins
            if (!VillagerUtilities.hasMarker(vil, plugin)) return;
            vil.setAware(true);
            VillagerUtilities.setNewCooldown(vil, plugin, cooldown);
            VillagerUtilities.setDisabledByWorkstation(vil, plugin, false);
            // remove the marker again
            VillagerUtilities.removeMarker(vil, plugin);
        }

    }
}
