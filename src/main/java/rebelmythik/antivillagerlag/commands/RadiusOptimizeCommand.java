package rebelmythik.antivillagerlag.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

import java.util.List;

public class RadiusOptimizeCommand implements CommandExecutor {

    AntiVillagerLag plugin;
    ColorCode colorcodes = new ColorCode();

    private final long cooldown;

    public RadiusOptimizeCommand(AntiVillagerLag plugin) {
        this.plugin = plugin;
        this.cooldown = plugin.getConfig().getLong("cooldown");
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String playerName = sender.getName();

        if (cmd.getName().equalsIgnoreCase("avloptimize")) {
            if(!sender.hasPermission("avl.optimize")) {
                sender.sendMessage(colorcodes.cm(plugin.getConfig().getString("messages.no-permission")));
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(colorcodes.cm(plugin.getConfig().getString("messages.correct-usage")));
                return false;
            }
            if (args.length == 1) {
                Bukkit.getPlayer(args[0]);
                try {
                    Integer.parseInt(args[0]);
                } catch (NumberFormatException numberFormatException) {
                    sender.sendMessage(colorcodes.cm(plugin.getConfig().getString("messages.correct-usage")));
                    return false;
                }
                double radius = Double.parseDouble(args[0]);

                if (radius > plugin.getConfig().getDouble("RadiusLimit")) {
                    sender.sendMessage(colorcodes.cm(plugin.getConfig().getString("messages.radius-limit")));
                    return false;
                }

                for (Entity entity : Bukkit.getPlayer(playerName).getNearbyEntities(radius, radius, radius)) {
                    Entity vil = entity;
                    if (entity instanceof Villager) {
                        if(((Villager) entity).isAware()) {
                            // If they don't have cooldown set it
                            if (!VillagerUtilities.hasRestockCooldown((Villager) vil, plugin)) {
                                VillagerUtilities.setNewRestockCooldown((Villager) vil, plugin, (long)0);
                            }
                            long cooldown = VillagerUtilities.getRestockCooldown((Villager) vil, plugin);
                            long currentTime = System.currentTimeMillis() / 1000;

                            // If villager has already been disabled check if they do have a cooldown
                            // to prevent bypassing of the cooldown feature
                            if (cooldown > currentTime) return false;

                            // Set Villager Name to Optimize Name and disable the AI
                            List<String> namesThatDisable = plugin.getConfig().getStringList("NamesThatDisable");
                            entity.setCustomName(namesThatDisable.get(0));
                            ((Villager) entity).setAware(false);

                            // set all necessary flags and timers
                            VillagerUtilities.setMarker((Villager) vil, plugin);
                            VillagerUtilities.setNewRestockCooldown((Villager) vil, plugin, cooldown);
                        }

                    }
                }
            }
        }
        return false;
    }
}
