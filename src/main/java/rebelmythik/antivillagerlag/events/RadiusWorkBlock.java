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
        VillagerUtilities.setDisabledByWorkstation(vil, plugin, willBeDisabled);
    }
}
