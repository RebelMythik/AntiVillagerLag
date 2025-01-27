package rebelmythik.antiVillagerLag.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antiVillagerLag.AntiVillagerLag;
import rebelmythik.antiVillagerLag.utils.VillagerUtilities;

public class UnoptimizeCommand implements CommandExecutor {

    AntiVillagerLag plugin;

    public UnoptimizeCommand(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!command.getName().equalsIgnoreCase("avlunoptimize")) return true;
        //  Make sure it's a player
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;
        //  Config access
        int radius = plugin.getConfig().getInt("RadiusLimit");
        //  Check if they have permission
        if(!player.hasPermission("avl.unoptimize")) {
            player.sendMessage(VillagerUtilities.colorcodes.cm(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }
        // Search for nearby villagers
        player.getNearbyEntities(radius, radius, radius).forEach(entity -> {
            if (entity instanceof Villager) {
                Villager villager = (Villager) entity;
                //  Setup new Villagers
                if (!VillagerUtilities.hasMarker(villager, plugin)) {
                    VillagerUtilities.setAiCooldown(villager, plugin, 0);
                    VillagerUtilities.setLevelCooldown(villager, plugin, 0);
                    VillagerUtilities.setLastRestock(villager, plugin);
                    VillagerUtilities.setMarker(villager, plugin, true);
                }
                //  Rename villager
                villager.setCustomName("");
                //  Update the marker and AI
                VillagerUtilities.setMarker(villager, plugin, true);
                villager.setAware(true);
            }
        });
        return true;
    }
}
