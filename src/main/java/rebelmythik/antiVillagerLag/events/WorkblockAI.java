package rebelmythik.antiVillagerLag.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antiVillagerLag.AntiVillagerLag;
import rebelmythik.antiVillagerLag.utils.VillagerUtilities;

public class WorkblockAI {

    public static boolean call(Villager villager, AntiVillagerLag plugin, Player player) {
        //check if workstation is disabled
        if (!plugin.getConfig().getBoolean("toggleableoptions.useworkstations")) return false;

        int radius = plugin.getConfig().getInt("toggleableoptions.workstationcheckradius");
        // Check for blocks within the specified radius
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location blockLocation = new Location(villager.getWorld(), villager.getLocation().getX() + x, villager.getLocation().getY() + y, villager.getLocation().getZ() + z);
                    if (VillagerUtilities.standingon_blocks.contains(blockLocation.getBlock().getType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
