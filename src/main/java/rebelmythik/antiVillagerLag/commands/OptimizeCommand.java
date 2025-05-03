package rebelmythik.antiVillagerLag.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import rebelmythik.antiVillagerLag.AntiVillagerLag;
import rebelmythik.antiVillagerLag.events.BlockAI;
import rebelmythik.antiVillagerLag.events.NameTagAI;
import rebelmythik.antiVillagerLag.events.WorkblockAI;
import rebelmythik.antiVillagerLag.utils.ColorCode;
import rebelmythik.antiVillagerLag.utils.VillagerUtilities;

public class OptimizeCommand implements CommandExecutor {

    AntiVillagerLag plugin;

    public OptimizeCommand(AntiVillagerLag plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!command.getName().equalsIgnoreCase("avloptimize")) return true;
        //  Make sure it's a player
        if (!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;

        //  Check if they have permission
        if(!player.hasPermission("avl.optimize")) {
            player.sendMessage(VillagerUtilities.colorcodes.cm(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        //searchable radius based on first argument specified, if null defaults to config
        int radius;
        try{
            radius = (strings != null && strings.length > 0) ? Integer.parseInt(strings[0]) : plugin.getConfig().getInt("RadiusDefault");
        } catch (NumberFormatException e) {
            player.sendMessage(VillagerUtilities.colorcodes.cm(plugin.getConfig().getString("messages.radius-invalid")));
            return true;
        }
        boolean canSearchRadius = radius <= plugin.getConfig().getInt("RadiusLimit");
        if(!canSearchRadius){
            player.sendMessage(VillagerUtilities.colorcodes.cm(plugin.getConfig().getString("messages.radius-limit")).replace("%avlradiuslimit%", plugin.getConfig().getString("RadiusLimit")));
            return true;
        }
        player.sendMessage(VillagerUtilities.colorcodes.cm(plugin.getConfig().getString("messages.searching-radius")).replace("%avlradius%", String.valueOf(radius)));

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
                villager.setCustomName(VillagerUtilities.disabling_names.getFirst());
                //  Update the marker and AI
                VillagerUtilities.setMarker(villager, plugin, false);
                villager.setAware(false);

            }
        });
        return true;
    }
}
