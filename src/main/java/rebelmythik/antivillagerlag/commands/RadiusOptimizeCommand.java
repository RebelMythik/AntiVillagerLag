package rebelmythik.antivillagerlag.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import rebelmythik.antivillagerlag.AntiVillagerLag;
import rebelmythik.antivillagerlag.utils.ColorCode;
import rebelmythik.antivillagerlag.utils.VillagerUtilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static rebelmythik.antivillagerlag.utils.VillagerUtilities.getCooldown;

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
                int count = 0;
                for (Entity entity : Bukkit.getPlayer(playerName).getNearbyEntities(radius, radius, radius)) {
                    Entity vil = entity;
                    if (entity instanceof Villager) {
                        if(((Villager) entity).hasAI()) {
                            // If they don't have cooldown set it
                            if (!VillagerUtilities.hasCooldown((Villager) vil, plugin)) {
                                VillagerUtilities.setNewCooldown((Villager) vil, plugin, (long)0);
                            }
                            long cooldown = VillagerUtilities.getCooldown((Villager) vil, plugin);
                            long currentTime = System.currentTimeMillis() / 1000;

                            // If villager has already been disabled check if they do have a cooldown
                            // to prevent bypassing of the cooldown feature
                            if (cooldown > currentTime) return false;

                            // Set Villager Name to Optimize Name and disable the AI
                            entity.setCustomName(plugin.getConfig().getString("NameThatDisables"));
                            ((Villager) entity).setAI(false);

                            // set all necessary flags and timers
                            VillagerUtilities.setMarker((Villager) vil, plugin);
                            VillagerUtilities.setNewCooldown((Villager) vil, plugin, cooldown);
                            count++;
                        }

                    }
                }
                sender.sendMessage(colorcodes.cm("&aOptimized "+count+" villagers!"));
            }

        return true;
    }
}
